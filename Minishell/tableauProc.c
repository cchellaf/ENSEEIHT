#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <unistd.h>
#include <string.h>
#include <signal.h>
#include <sys/wait.h>
#include "tableauProc.h"


/* initialiser un processus */ 
void init_proc(proc* processus, int id, pid_t pid, char *etat, char *ligne_cmd, bool fini) {
   processus->id = id; 
   processus->pid = pid; 
   processus->etat = etat; 
   processus->ligne_cmd = ligne_cmd; 
   processus->fini = false; 
}



/*initialiser un tableau de processus */ 
void init_tab(tableau* tableau) {
   tableau->tab_proc = malloc(TAILLE*sizeof(proc));
   tableau->N = 0; 
}



/* ajouter un processus dans le tableau de processus */ 
void ajouter_proc(proc processus, tableau* tableau) {
   proc* tab_proc = tableau->tab_proc; 
   if ((tableau->N) < TAILLE) {
      tab_proc[tableau->N] = processus; 
      tableau->N = tableau->N + 1;   
   } else {
      printf("espace saturé \n"); 
   } 
}


/* tester l'égalité de deux processus */
bool egal(proc proc1, proc proc2) {
    return (proc1.id==proc2.id) & (proc1.pid==proc2.pid) & (proc1.ligne_cmd==proc2.ligne_cmd); 
}

   

/* afficher la liste des processus */ 
void afficher_liste(tableau tableau) {
   proc* tab_proc = tableau.tab_proc;
   int N = tableau.N; 
   for (int i = 0; i< N; i ++) {
       if (!(tab_proc[i].fini)) {
          printf("id [%d]  |  pid [%d]  |  etat [%s]  |  commande [%s] \n", tab_proc[i].id, tab_proc[i].pid, tab_proc[i].etat, tab_proc[i].ligne_cmd);
       }
   }
}


/* chercher le pid d'un processus à partir de son id */
pid_t trouver_pid(int id, tableau tableau) {
   int i = 0; 
   int N = tableau.N; 
   proc* tab_proc = tableau.tab_proc; 
   while ( (i<N) && (tab_proc[i].id != id) ) {
      i++;
   }
   if (tab_proc[i].id == id) {
      return tab_proc[i].pid; 
   } else {
      return -1;     
   }
}


/* écraser le processus par le même processus mais d'état différent */
void remplacer_proc(proc processus, tableau* tableau) {
   int i = 0; 
   int N = tableau->N;
   proc* tab_proc = tableau->tab_proc; 
   while ( (i<N) && (!(egal(tab_proc[i], processus))) ) {
      i++;
   }
   if (egal(tab_proc[i], processus)) {
      tab_proc[i] = processus ;    
   }
}



