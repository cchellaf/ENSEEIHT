#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <unistd.h>
#include <string.h>
#include <signal.h>
#include <sys/wait.h>
#include <errno.h>
#include "readcmd.c"
#include "tableauProc.c"
#include <fcntl.h>


//---------------------------------------------------------------------------------------------------------------------//
struct cmdline *commande; 
tableau tab; 
proc* processus; 
int pid_fils;
int proc_avant_plan;  // vaut 1 s'il y a un processus en avant plan et 0 sinon
int pid_fg = -1;      //le pid du processus en avant plan  (vaut le pid du processus en avant plan s'il existe, et -1 sinon)
int k = 0;            //l'identifiant associé à chaque processus
//---------------------------------------------------------------------------------------------------------------------//



/* convertir la ligne de commande en chaine de caractères */
char* toString(struct cmdline *ligne) {
   int i = 0; 
   char* current = ligne->seq[0][i];
   char* retour = malloc(50); 
   while (current != NULL) {
      retour = strncat(retour, current, 50);
      retour = strncat(retour, " ", 1); 
      i ++;
      current = ligne->seq[0][i]; 
   }
   if (ligne->backgrounded != NULL) {
      retour = strncat(retour, "&", 1); 
   }
   return retour; 
}

//---------------------------------------------------------------------------------------------------------------------//

/* Commande cd */
void commande_cd(struct cmdline *commande) {
   int dir;
   if ((commande->seq[0][1] == NULL) || (strcmp(commande->seq[0][1], "~") == 0)) {
      dir = chdir(getenv("HOME"));
   } else { 
      dir = chdir(commande->seq[0][1]); 
   }
   if (dir == -1) {
      printf("répertoire non trouvé \n"); 
   }
}

//---------------------------------------------------------------------------------------------------------------------//

/* Commande stop */
void commande_stop(struct cmdline *commande) {
   if (commande->seq[0][1] == NULL) {
      printf("Veuillez saisir la commande sous la forme : stop [identifiant] \n");
   } else {
      int id = atoi(commande->seq[0][1]); 
      if (id == 0) {
         printf("L'identifiant doit être un entier. \n"); 
      } else {
          pid_t pid_proc = trouver_pid(id, tab); 
          if (pid_proc == -1) {
             printf("Cet identifiant n'existe pas. \n");
          } else {
             kill(pid_proc, SIGSTOP);  
          } 
      }
   } 
}

//---------------------------------------------------------------------------------------------------------------------//

/* Commande bg */
void commande_bg(struct cmdline *commande) {
   if (commande->seq[0][1] == NULL) {
      printf("Veuillez saisir la commande sous la forme : bg [identifiant] \n");
   } else {
      int id = atoi(commande->seq[0][1]); 
      if (id == 0) {
         printf("L'identifiant doit être un entier. \n"); 
      } else {
         pid_t pid_proc = trouver_pid(id, tab); 
         if (pid_proc == -1) {
            printf("Cet identifiant n'existe pas. \n");
         } else {
            kill(pid_proc, SIGCONT);
         }
      }
   }
}

//---------------------------------------------------------------------------------------------------------------------//

/* Commande fg */
void commande_fg(struct cmdline *commande) {
   if (commande->seq[0][1] == NULL) {
      printf("Veuillez saisir la commande sous la forme : fg [identifiant] \n");
   } else {
      int id = atoi(commande->seq[0][1]); 
      if (id == 0) {
         printf("L'identifiant doit être un entier. \n"); 
      } else {
         int attente; 
         pid_t pid_proc = trouver_pid(id, tab); 
         if (pid_proc == -1) {
            printf("Cet identifiant n'existe pas. \n");
         } else {
            kill(pid_proc, SIGCONT);
         }
         wait(&attente); 
      }
   }
}

//---------------------------------------------------------------------------------------------------------------------//

/* Gestion de redirections */

void rediriger_entree(struct cmdline *commande) {
   int desc_fich = open(commande->in, O_RDONLY); 
   if (desc_fich < 0) {
      perror("Erreur d'ouverture"); 
      exit(1); 
   }
   dup2(desc_fich, 0);
   if (close(desc_fich) < 0) {
      perror("Erreur de fermeture");
      exit(2);
   }
}
   

void rediriger_sortie(struct cmdline *commande) {
   int desc_fich = open(commande->out, O_WRONLY | O_TRUNC | O_CREAT, S_IRUSR|S_IWUSR); 
   if (desc_fich < 0) {
      perror("Erreur d'ouverture"); 
      exit(1); 
   }
   dup2(desc_fich, 1);
   if (close(desc_fich) < 0) {
      perror("Erreur de fermeture");
      exit(2);
   }
}

//---------------------------------------------------------------------------------------------------------------------//

/* Les autres commandes */ 
void commandes(struct cmdline *commande) {
   int retour; 
   k++; 
   processus = malloc(sizeof(proc)); 
   char* commande_str = toString(commande); 

   retour = fork(); 

   /* echec du fork */
   if (retour < 0) {   
      printf("Erreur fork \n");
      exit(1); 

    /* fils */ 
    } else if (retour == 0 ) {
       //On masque les signaux SIGTSTP et SIGINT pour tous les fils, afin que le père en a seul accès
       sigset_t ens_signaux; 
       sigemptyset(&ens_signaux);

       /* ajouter SIGINT et SIGTSTP à ens_signaux*/
       sigaddset(&ens_signaux, SIGINT); 
       sigaddset(&ens_signaux, SIGTSTP);

       /* masquer les signaux SIGINT et SIGTSTP */
       sigprocmask(SIG_SETMASK, &ens_signaux, NULL);

       if (!(commande->in == NULL) ) {
          rediriger_entree(commande); 
       } 
       if (!(commande->out == NULL) ) {
          rediriger_sortie(commande);
       }

       execvp(commande->seq[0][0], commande->seq[0]); 
       printf("Echec dans l'exécution de la commande \n");   
       exit(1);
         
     /* père */
     } else {
       if (commande->backgrounded == NULL) {
          proc_avant_plan = 1;
          pid_fg = retour;     //le pid du processus en avant plan 
          init_proc(processus, k, retour, "ACTIF", commande_str, false);
          ajouter_proc(*processus, &tab);  
          while (proc_avant_plan) {
             pause(); 
          } 
       } else {
          proc_avant_plan = 0;
          init_proc(processus, k, retour, "ACTIF", commande_str, false); 
          ajouter_proc(*processus, &tab); 
       }
    } 
}

//---------------------------------------------------------------------------------------------------------------------//

/* traitant du signal SIGCHDL */
void suivi_fils (int sig) {
    int etat_fils;
    do {
        pid_fils = (int) waitpid(-1, &etat_fils, WNOHANG | WUNTRACED | WCONTINUED);
        if ((pid_fils == -1) && (errno != ECHILD)) {
            perror("waitpid");
            exit(EXIT_FAILURE);
        } else if (pid_fils > 0) {

            if (WIFSTOPPED(etat_fils)) {
                /* traiter la suspension */
                pid_fg = -1; 
                proc_avant_plan = 0;
                processus->etat = "SUSPENDU"; 
                processus->fini = false; 
                remplacer_proc(*processus, &tab);
                printf("\nle fils de pid %d est suspendu \n", pid_fils); 

            } else if (WIFCONTINUED(etat_fils)) {
                /* traiter la reprise */
                proc_avant_plan = 1; 
                pid_fg = pid_fils; 
                processus->etat = "REPRIS"; 
                processus->fini = true;  
                remplacer_proc(*processus, &tab);
                printf("\nle fils de pid %d est repris \n", pid_fils); 

            } else if (WIFEXITED(etat_fils)) {
                /* traiter exit */
                proc_avant_plan = 0;
                pid_fg = -1; 
                processus->etat = "TERMINE"; 
                processus->fini = true; 
                remplacer_proc(*processus, &tab);
                printf("\nle fils de pid %d est terminé \n", pid_fils); 

            } else if (WIFSIGNALED(etat_fils)) {
                /* traiter signal */
                proc_avant_plan = 0;
                pid_fg = -1; 
                processus->etat = "TUE"; 
                processus->fini = true;
                remplacer_proc(*processus, &tab);
                printf("\nle fils de pid %d a été tué par un signal \n", pid_fils); 
            }
        }
    } while (pid_fils > 0);
}

//---------------------------------------------------------------------------------------------------------------------//

/* traitant associé à SIGTSTP */
void handler_ctrlZ(int sig) {
   if (pid_fg != -1) {
      kill(pid_fg, SIGSTOP);
   } else {
      printf("\nAucun processus n'est en avant plan \n"); 
   }
}

//---------------------------------------------------------------------------------------------------------------------//

/* traitant associé à SIGINT */
void handler_ctrlC(int sig) {
   if (pid_fg != -1) {
      kill(pid_fg, SIGKILL);
   } else {
      printf("Aucun processus n'est en avant plan \n"); 
   }
}



//-------------------------------------------PROGRAMME PRINCIPAL-------------------------------------------------------//
//---------------------------------------------------------------------------------------------------------------------//

int main () {
 
   init_tab(&tab); 

   /* associer un traitant au signal SIGCHLD */
   signal(SIGCHLD, suivi_fils);

   /* associer le traitant */
   signal(SIGTSTP, handler_ctrlZ); 
   signal(SIGINT, handler_ctrlC); 

   while (1) { 
      printf(">>> commande : ");   
      commande = readcmd();

      if ((commande != NULL) && (commande->seq[0] != NULL)) { 
      /* commande exit */
      if (strcmp(commande->seq[0][0], "exit") == 0) {
         exit(0); 

      /*commande cd */
      } else if (strcmp(commande->seq[0][0], "cd") == 0) {
         commande_cd(commande);

      /* commande jobs */
      } else if (strcmp(commande->seq[0][0], "jobs") == 0) {
         afficher_liste(tab);   
   
      /* commande stop */
      } else if (strcmp(commande->seq[0][0], "stop") == 0) {
         commande_stop(commande);
    
      /* commande bg */
      } else if (strcmp(commande->seq[0][0], "bg") == 0) {
         commande_bg(commande);

      /* commande fg */
      } else if (strcmp(commande->seq[0][0], "fg") == 0) {
         commande_fg(commande);

     /*les autres commandes */
      } else { 
         commandes(commande);
      }
      } else {
         printf("Veuillez entrer une commande \n"); 
      } 
   } 
   return EXIT_SUCCESS;
}
