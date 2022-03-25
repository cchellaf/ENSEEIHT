
#ifndef TABLEAUPROC_H
#define TABLEAUPROC_H

#define TAILLE 50



/* définir un processus */
typedef struct {
    int id; 
    pid_t pid; 
    char *etat; 
    char *ligne_cmd; 
    bool fini;       //fini = true si le processus est fini 
} proc;


/* définir un tableau de processus */
typedef struct {
   proc* tab_proc; 
   int N;           //Le nombre d'éléments du tableau  (N<=TAILLE)
} tableau; 



/* initialiser un processus */ 
void init_proc(proc* processus, int id, pid_t pid, char *etat, char *ligne_cmd, bool fini);


/* initialiser un tableau de processus */ 
void init_tab(tableau* tableau) ;


/* ajouter un processus dans le tableau de processus */ 
void ajouter_proc(proc processus, tableau* tableau);


/* tester l'égalité de deux processus */
bool egal(proc proc1, proc proc2);


/* afficher la liste des processus */ 
void afficher_liste(tableau tableau);


/* chercher le pid d'un processus à partir de son id */
pid_t trouver_pid(int id, tableau tableau);


/* écraser le processus par le même processus mais d'état différent */
void remplacer_proc(proc processus, tableau* tableau);


#endif


