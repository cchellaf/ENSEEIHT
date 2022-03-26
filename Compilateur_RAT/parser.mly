/* Imports. */

%{

open Type
open Ast.AstSyntax
%}


%token <int> ENTIER
%token <string> ID
%token <string> TID
%token RETURN
%token PV
%token PT
%token AO
%token AF
%token PF
%token PO
%token EQUAL
%token CONST
%token PRINT
%token IF
%token ELSE
%token WHILE
%token BOOL
%token INT
%token RAT
%token CALL 
%token CO
%token CF
%token SLASH
%token NUM
%token DENOM
%token TRUE
%token FALSE
%token PLUS
%token MULT
%token INF
%token EOF
%token NEW
%token ADDR
%token NULL
%token TYPEDEF
%token STRUCT

(* Type de l'attribut synthétisé des non-terminaux *)
%type <programme> prog
%type <instruction list> bloc
%type <fonction> fonc
%type <instruction list> is
%type <instruction> i
%type <typ> typ
%type <(typ*string) list> dp
%type <expression> e 
%type <expression list> cp
%type <affectable> affect
%type <(string*typ) list> td

(* Type et définition de l'axiome *)
%start <Ast.AstSyntax.programme> main

%%

main : lfi = prog EOF     {lfi}

prog :
| td1 = td lf = fonc  lfi = prog   {let (Programme (_,lf1,li))=lfi in (Programme (td1,lf::lf1,li))}  
| ID li = bloc            {Programme ([],[],li)}

fonc : t=typ n=ID PO p=dp PF AO li=is AF {Fonction(t,n,p,li)}

bloc : AO li = is AF      {li}

td :
|                         {[]}
| TYPEDEF n=TID EQUAL t=typ PV lp=td        {(n,t)::lp}


is :
|                         {[]}
| i1=i li=is              {i1::li}

i :
| t=typ n=ID EQUAL e1=e PV          {Declaration (t,n,e1)}
| a=affect EQUAL e1=e PV            {Affectation (a,e1)}
| CONST n=ID EQUAL e=ENTIER PV      {Constante (n,e)}
| PRINT e1=e PV                     {Affichage (e1)}
| IF exp=e li1=bloc ELSE li2=bloc   {Conditionnelle (exp,li1,li2)}
| WHILE exp=e li=bloc               {TantQue (exp,li)}
| RETURN exp=e PV                   {Retour (exp)}
| a=affect PLUS EQUAL e1=e PV       {AssignAdd (a,e1)}
| TYPEDEF n=TID EQUAL t=typ PV     {DeclTypeNomme (n,t)} 


dp :
|                         {[]}
| t=typ n=ID lp=dp        {(t,n)::lp}

typ :
| BOOL                 {Bool}
| INT                  {Int}
| RAT                  {Rat}
| t=typ MULT           {Pointeur t} 
| t=TID                {TNomme t} 
| STRUCT AO p=dp AF    {Struct p}  


e : 
| CALL n=ID PO lp=cp PF   {AppelFonction (n,lp)}
| CO e1=e SLASH e2=e CF   {Binaire(Fraction,e1,e2)}
| a=affect                    {Affectable a}
| TRUE                    {Booleen true}
| FALSE                   {Booleen false}
| e=ENTIER                {Entier e}
| NUM e1=e                {Unaire(Numerateur,e1)}
| DENOM e1=e              {Unaire(Denominateur,e1)}
| PO e1=e PLUS e2=e PF    {Binaire (Plus,e1,e2)}
| PO e1=e MULT e2=e PF    {Binaire (Mult,e1,e2)}
| PO e1=e EQUAL e2=e PF   {Binaire (Equ,e1,e2)}
| PO e1=e INF e2=e PF     {Binaire (Inf,e1,e2)}
| PO exp=e PF             {exp}
| PO NEW t=typ PF         {New t}
| ADDR n=ID               {Adresse n}
| NULL                    {Null}  
| AO lp=cp AF             {StructAffect (lp)}        

cp :
|               {[]}
| e1=e le=cp    {e1::le}

affect : 
| n=ID                    {Ident n}
| PO MULT a=affect PF     {Deref a}
| PO a=affect PT n=ID PF  {AccesChamp (a,n)}    

