#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <unistd.h>
#include <string.h>
#include <signal.h>
#include <sys/wait.h>
#include "readcmd.c"


int proc_avant_plan;  

/* traitant du signal SIGCHDL */
void suivi_fils (int sig) {
    int etat_fils, pid_fils;
    do {
        pid_fils = (int) waitpid(-1, &etat_fils, WNOHANG | WUNTRACED | WCONTINUED);
        if ((pid_fils == -1) && (errno != ECHILD)) {
            perror("waitpid");
            exit(EXIT_FAILURE);
        } else if (pid_fils > 0) {
            if (WIFSTOPPED(etat_fils)) {
                /* traiter la suspension */
                printf("le fils de pid %d est suspendu \n", getpid()); 
            } else if (WIFCONTINUED(etat_fils)) {
                /* traiter la reprise */
                printf("le fils de pid %d est repris \n", getpid()); 
            } else if (WIFEXITED(etat_fils)) {
                /* traiter exit */
                proc_avant_plan = 0; 
            } else if (WIFSIGNALED(etat_fils)) {
                /* traiter signal */
                printf("le fils de pid %d a été tué par un signal \n", getpid()); 
            }
        }
    } while (pid_fils > 0);
}


int main () {

   int retour, dir; 
   struct cmdline *commande; 

   /* associer un traitant au signal SIGCHLD */
   signal(SIGCHLD, suivi_fils); 

   while (1) { 
      printf(">>> commande : ");   
      commande = readcmd(); 

      if ((commande != NULL) && (commande->seq[0] != NULL)) { 
      /* commande exit */
      if (strcmp(commande->seq[0][0], "exit") == 0) {
         exit(0); 

      /*commande cd */
      } else if (strcmp(commande->seq[0][0], "cd") == 0) {
         if ((commande->seq[0][1] == NULL) || (strcmp(commande->seq[0][1], "~") == 0)) {
            dir = chdir(getenv("HOME"));
         } else { 
            dir = chdir(commande->seq[0][1]); 
         }
         if (dir == -1) {
            printf("répertoire non trouvé \n"); 
         }
    
     /*les autres commandes */
      } else { 
         proc_avant_plan = 1;
         retour = fork(); 

         /* echec du fork */
         if (retour < 0) {   
            printf("Erreur fork \n");
            exit(1); 

         /* fils */ 
         } else if (retour == 0 ) {
            execvp(commande->seq[0][0], commande->seq[0]); 
            printf("Echec dans l'exécution de la commande \n");   

         /* père */
         } else {
            if (commande->backgrounded == NULL) {
               while (proc_avant_plan) {
                  pause(); 
               } 
            }
         } 
      }
      } else {
         printf("Veuillez entrer une commande \n"); 
      } 
   } 
   return EXIT_SUCCESS;
}
