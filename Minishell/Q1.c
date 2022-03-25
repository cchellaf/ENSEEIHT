#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <unistd.h>
#include <string.h>
#include <signal.h>
#include <sys/wait.h>
#include "readcmd.c"

int main () {
   int retour;  
   struct cmdline *commande; 

   while (1) {
      commande = readcmd(); 
      if ((commande != NULL) && (commande->seq[0] != NULL)) {

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
            //Dans cette question, le père ne fait rien 
         } 
      } else {
         printf("Veuillez entrer une commande \n"); 
      } 
   }  
   return EXIT_SUCCESS; 
}

