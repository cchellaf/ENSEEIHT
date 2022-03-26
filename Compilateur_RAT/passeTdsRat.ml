
(* Module de la passe de gestion des identifiants *)
module PasseTdsRat : Passe.Passe with type t1 = Ast.AstSyntax.programme and type t2 = Ast.AstTds.programme =
struct

  open Tds
  open Exceptions
  open Ast
  open AstTds
  open Type

  type t1 = Ast.AstSyntax.programme
  type t2 = Ast.AstTds.programme


(* analyse_tds_type : tds -> typ -> AstTds.types*)
(* Paramètre tds : la table des symboles courante *)
(* Paramètre t : le type à analyser *) 
(* pour le type enregistrement, on met à jour la tds en y mettant les identifiants déclarés dans l'enregistrement, pour les autres types on les retournes*)
 let rec analyse_tds_types tds t = match t with
                                  | Struct (l)-> let l1 =  List.map (fun (t,n) -> begin
                                                                                match chercherLocalement tds n with
                                                                                | None ->                                                                   
                                                                                    (* Création de l'information associée à l'identfiant *)
                                                                                    let info = InfoVar (n,Undefined, 0, "") in
                                                                                    (* Création du pointeur sur l'information *)
                                                                                    let ia = info_to_info_ast info in
                                                                                    (* Ajout de l'information (pointeur) dans la tds *)
                                                                                    ajouter tds n ia;
                                                                                     let _ = analyse_tds_types tds t in  
                                                                                     (t,ia)                                                                             
                                                                                | Some _ ->
                                                                                    (* L'identifiant est trouvé dans la tds locale, 
                                                                                    il a donc déjà été déclaré dans le bloc courant *) 
                                                                                    raise (DoubleDeclaration n)
                                                                              end ) l in 
                                                 Enregistr (l1)
                                  | _-> TypeBase (t) 

(* analyse_tds_affectable : AstSyntax.affectable -> AstTds.affectable *)
(* Paramètre tds : la table des symboles courante *)
(* Paramètre a : l'affectable à analyser *)
(* Paramètre affect : boolean indiquant si l'affectable est modifié *) 
(* Vérifie la bonne utilisation des identifiants et tranforme l'affectable
en une affectable de type AstTds.affectable *)
(* Erreur si l'identifiant est non déclaré *)
 let rec analyse_tds_affectable tds a affect = 
  match a with
  | AstSyntax.Ident n -> (match chercherGlobalement tds n with
                          | None ->  raise (IdentifiantNonDeclare n) 
                          | Some a -> (match info_ast_to_info a with 
                                      | InfoVar _ -> Ident(a)
                                      | InfoConst _ -> if affect then raise (MauvaiseUtilisationIdentifiant n)
                                                       else Ident a
                                      | InfoFun _ -> raise (MauvaiseUtilisationIdentifiant n)))
  | AstSyntax.Deref a1 -> Deref(analyse_tds_affectable tds a1 affect)
  | AstSyntax.AccesChamp (a,n) ->  let a1 = (analyse_tds_affectable tds a affect) in 
                                        (match chercherGlobalement tds n with 
                                        | None ->  raise (IdentifiantNonDeclare n)
                                        | Some info -> (match info_ast_to_info info with 
                                                | InfoVar _ -> AccesChamp (a1,info)
                                                | _ -> raise (MauvaiseUtilisationIdentifiant n) ) ) 
                                  

(* analyse_tds_expression : AstSyntax.expression -> AstTds.expression *)
(* Paramètre tds : la table des symboles courante *)
(* Paramètre e : l'expression à analyser *)
(* Vérifie la bonne utilisation des identifiants et tranforme l'expression
en une expression de type AstTds.expression *)
(* Erreur si mauvaise utilisation des identifiants *)
let rec analyse_tds_expression tds e = 
  match e with
  |AstSyntax.AppelFonction (id,expList) ->
    begin
      match chercherGlobalement tds id with
        |None -> raise (IdentifiantNonDeclare id)
        |Some a ->match info_ast_to_info a with
                  |InfoFun (_,_,_) ->
                                let el =  (List.map (analyse_tds_expression tds) expList) in 
                                AppelFonction (a, el)
                  |_ -> raise (MauvaiseUtilisationIdentifiant id)              
    end
  |AstSyntax.Affectable (a) -> 
                          Affectable(analyse_tds_affectable tds a false)
  |AstSyntax.Adresse x -> (match chercherGlobalement tds x with
                          | None -> raise (IdentifiantNonDeclare x)
                          | Some info -> (match info_ast_to_info info with 
                                         | InfoVar _ -> Adresse info
                                         | _ -> raise (MauvaiseUtilisationIdentifiant x) ) )
  | AstSyntax.New t ->(let _ = analyse_tds_types tds t in  New t )
  | AstSyntax.Null -> Null   
  |AstSyntax.Booleen (b) -> Booleen (b)
  |AstSyntax.Entier (i) ->Entier (i)
  |AstSyntax.Unaire (opu, exp) -> Unaire (opu, analyse_tds_expression tds exp)
  |AstSyntax.Binaire (opb, exp1, exp2) -> Binaire (opb,analyse_tds_expression tds exp1,analyse_tds_expression tds exp2)
   |AstSyntax.StructAffect (le) -> let le1 = List.map (analyse_tds_expression tds) le in 
                                    StructAffect(le1)


(* analyse_tds_instruction : AstSyntax.instruction -> tds -> AstTds.instruction *)
(* Paramètre tds : la table des symboles courante *)
(* Paramètre i : l'instruction à analyser *)
(* Vérifie la bonne utilisation des identifiants et tranforme l'instruction
en une instruction de type AstTds.instruction *)
(* Erreur si mauvaise utilisation des identifiants *)
let rec analyse_tds_instruction tds i =
  match i with
  | AstSyntax.Declaration (t, n, e) ->
      begin
        match chercherLocalement tds n with
        | None ->
            let _ = analyse_tds_types tds t in 
            (* L'identifiant n'est pas trouvé dans la tds locale, 
            il n'a donc pas été déclaré dans le bloc courant *)
            (* Vérification de la bonne utilisation des identifiants dans l'expression *)
            (* et obtention de l'expression transformée *) 
            let ne = analyse_tds_expression tds e in
            (* Création de l'information associée à l'identfiant *)
            let info = InfoVar (n,Undefined, 0, "") in
            (* Création du pointeur sur l'information *)
            let ia = info_to_info_ast info in
            (* Ajout de l'information (pointeur) dans la tds *)
            ajouter tds n ia;
            (* Renvoie de la nouvelle déclaration où le nom a été remplacé par l'information 
            et l'expression remplacée par l'expression issue de l'analyse *)
            Declaration (t, ia, ne) 
        | Some _ ->
            (* L'identifiant est trouvé dans la tds locale, 
            il a donc déjà été déclaré dans le bloc courant *) 
            raise (DoubleDeclaration n)
      end
  | AstSyntax.Affectation (a,e) -> let a1 = analyse_tds_affectable tds a true in
                                   let e1 = analyse_tds_expression tds e in
                                    Affectation(a1, e1)
  | AstSyntax.Constante (n,v) -> 
      begin
        match chercherLocalement tds n with
        | None -> 
        (* L'identifiant n'est pas trouvé dans la tds locale, TO
        il n'a donc pas été déclaré dans le bloc courant *)
        (* Ajout dans la tds de la constante *)
        ajouter tds n (info_to_info_ast (InfoConst (n,v))); 
        (* Suppression du noeud de déclaration des constantes devenu inutile *)
        Empty
        | Some _ ->
          (* L'identifiant est trouvé dans la tds locale, 
          il a donc déjà été déclaré dans le bloc courant *) 
          raise (DoubleDeclaration n)
      end
  | AstSyntax.Affichage e -> 
      (* Vérification de la bonne utilisation des identifiants dans l'expression *)
      (* et obtention de l'expression transformée *)
      let ne = analyse_tds_expression tds e in
      (* Renvoie du nouvel affichage où l'expression remplacée par l'expression issue de l'analyse *)
      Affichage (ne)
  | AstSyntax.Conditionnelle (c,t,e) -> 
      (* Analyse de la condition *)
      let nc = analyse_tds_expression tds c in
      (* Analyse du bloc then *)
      let tast = analyse_tds_bloc tds t in
      (* Analyse du bloc else *)
      let east = analyse_tds_bloc tds e in
      (* Renvoie la nouvelle structure de la conditionnelle *)
      Conditionnelle (nc, tast, east)
  | AstSyntax.TantQue (c,b) -> 
      (* Analyse de la condition *)
      let nc = analyse_tds_expression tds c in
      (* Analyse du bloc *)
      let bast = analyse_tds_bloc tds b in
      (* Renvoie la nouvelle structure de la boucle *)
      TantQue (nc, bast)
  | AstSyntax.Retour (e) -> 
      (* Analyse de l'expression *)
      let ne = analyse_tds_expression tds e in
      Retour (ne)
  |AstSyntax.AssignAdd (a,e)->  let a1 = analyse_tds_affectable tds a true in
                                   let e1 = analyse_tds_expression tds e in
                                    AssignAdd(a1, e1)
     (** déclaration d'un type nomme *)                               
  | AstSyntax.DeclTypeNomme (n,t) ->(let _ = analyse_tds_types tds t in
    (
        match chercherLocalement tds n with 
        | None ->   
            (* Création de l'information associée à l'identfiant du type nomme *)
            let info = InfoVar (n,Undefined, 0, "") in
            (* Création du pointeur sur l'information *)
            let ia = info_to_info_ast info in
            (* Ajout de l'information  dans la tds *)
            ajouter tds n ia;
            DeclTypeNomme (ia, t) 
        | Some _ ->
            (* L'identifiant est trouvé dans la tds locale, 
            il a donc déjà été déclaré dans le bloc courant *) 
            raise (DoubleDeclaration n)
    ) ) 
                               
      
(* analyse_tds_bloc : AstSyntax.bloc -> AstTds.bloc *)
(* Paramètre tds : la table des symboles courante *)
(* Paramètre li : liste d'instructions à analyser *)
(* Vérifie la bonne utilisation des identifiants et tranforme le bloc
en un bloc de type AstTds.bloc *)
(* Erreur si mauvaise utilisation des identifiants *)
and analyse_tds_bloc tds li =
  (* Entrée dans un nouveau bloc, donc création d'une nouvelle tds locale 
  pointant sur la table du bloc parent *)
  let tdsbloc = creerTDSFille tds in
  (* Analyse des instructions du bloc avec la tds du nouveau bloc 
  Cette tds est modifiée par effet de bord *)
   let nli = List.map (analyse_tds_instruction tdsbloc) li in
   (* afficher_locale tdsbloc ; *) (* décommenter pour afficher la table locale *)
   nli


(* analyse_tds_fonction : AstSyntax.fonction -> AstTds.fonction *)
(* Paramètre tds : la table des symboles courante *)
(* Paramètre : la fonction à analyser *)
(* Vérifie la bonne utilisation des identifiants et tranforme la fonction
en une fonction de type AstTds.fonction *)
(* Erreur si mauvaise utilisation des identifiants *)
let analyse_tds_fonction maintds (AstSyntax.Fonction(t,n,lp,li))  = 
  match chercherLocalement maintds n with
   |Some _ -> raise (DoubleDeclaration n)
   |None ->let _ = analyse_tds_types maintds t in (* on met à jour la tds au cas où le type de retour est un enregistrement, *)
           let tdsfct = creerTDSFille maintds in (*on crée un tds fille pour les paramètres *)
           (**on transforme l'identifiant de chaque paramètre en une info_ast et on l'ajoute à la tds *)
            let lpTds = List.map (fun (t,s) -> match chercherLocalement tdsfct s with
                                               |Some _ ->  raise (DoubleDeclaration s)
                                               |None -> let _ = analyse_tds_types tdsfct t in
                                                      let infoAst =info_to_info_ast ( InfoVar (s,t, 0, ""))in 
                                                          ajouter tdsfct s infoAst;
                                                          (t,infoAst)) lp in 
              
            (**on récupère la liste des types des paramètres  *)                                           
            let ltp = List.map (fun (a,_) -> a ) lp in
            (**on crée l'info_ast associée à la fonction et on l'ajoute à la tds *)
            let ia =  info_to_info_ast (InfoFun (n,t,ltp)) in 
              ajouter maintds n ia ; 
              Fonction (t,ia,lpTds,analyse_tds_bloc tdsfct li)


(* analyser : AstSyntax.ast -> AstTds.ast *)
(* Paramètre : le programme à analyser *)
(* Vérifie la bonne utilisation des identifiants et tranforme le programme
en un programme de type AstTds.ast *)
(* Erreur si mauvaise utilisation des identifiants *)
let analyser (AstSyntax.Programme (typenommes,fonctions,prog)) =
  let tds = creerTDSMere () in
  (**on associe à chaque type nomme une info_ast et on l'ajoute à la tds *)
  let ltn = List.map (fun (s,t) -> match chercherLocalement tds s with
                                               |Some _ ->  raise (DoubleDeclaration s)
                                               |None -> let _ = analyse_tds_types tds t in
                                                      let infoAst =info_to_info_ast ( InfoVar (s,t, 0, ""))in 
                                                          ajouter tds s infoAst;
                                                         (infoAst,t)) typenommes in                                                       
  let nf = List.map (analyse_tds_fonction tds) fonctions in 
  let nb = analyse_tds_bloc tds prog in
  Programme (ltn,nf,nb)

end
