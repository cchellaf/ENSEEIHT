
(* Module de la passe de gestion des Types *)

module PasseTypeRat : Passe.Passe with type t1 = Ast.AstTds.programme and type t2 = Ast.AstType.programme  =
struct

  open Tds
  open Exceptions
  open Ast
  open AstType
  open Type
  
  type t1 = Ast.AstTds.programme
  type t2 = Ast.AstType.programme 


(**chercherTypeTnomme : tds -> typ -> types  *)
(** permet de trouver le type effectif d'un type nomme depuis une tds contenat le sinfo des types nommes *)
(* pour les autres types on les renvoies directement *)
let rec chercherTypeTnomme tds t = match t with 
                                   | TNomme (s) -> ( 
                                                      match chercherGlobalement tds s with 
                                                          | None -> failwith "type nomme non defini" 
                                                          | Some a -> (match info_ast_to_info a with 
                                                                        |InfoVar(_,t1,_,_) -> chercherTypeTnomme tds t1
                                                                        |_ -> failwith "les types nommes sont des infovar "  ))
                                   | Pointeur t2 -> Pointeur (chercherTypeTnomme tds t2)  
                                   | _ -> t  

(** la tds est utilisée pour la gestion des types nomme *)
let rec analyse_type_affectable tds  a =
match a with 
| AstTds.Ident info ->  ( match info_ast_to_info info with
                          |InfoVar(_,t,_,_) -> Ident(info),(chercherTypeTnomme tds t)
                          |InfoConst _ -> Ident(info), Int
                          |InfoFun(s,_,_)-> raise (MauvaiseUtilisationIdentifiant s))
| AstTds.Deref a1 -> let a2, t = analyse_type_affectable tds a1 in 
                      ( match t with 
                      | Pointeur t1 -> Deref(a2), (chercherTypeTnomme tds t1)
                      | _ -> assert false )                                          
| AstTds.AccesChamp (_, _) -> assert false (**traitement des enregistrement non fini *)(*match info_ast_to_info info with 
                                 |InfoVar(_,t,_,_) ->  AccesChamp (analyse_type_affectable tds a, info) ,t*)    



(** la tds est utilisée pour la gestion des types nomme *)
let rec analyse_type_expression tds  e = 
  match e with
  |AstTds.AppelFonction (info,expList) ->( match info_ast_to_info info with                                            
                                          |InfoFun (_,tr,ts) -> (let es1, ts1 = List.split (List.map (analyse_type_expression tds) expList) in                                                
                                                                if (est_compatible_list ts ts1) then 
                                                                   (AppelFonction(info,es1), (chercherTypeTnomme tds tr)) 
                                                                else raise (TypesParametresInattendus (ts1 ,ts) ))
                                          |_ -> assert false
                                                              )
                                        
      
  |AstTds.Affectable a -> let a1, t1 = analyse_type_affectable tds a in Affectable(a1), t1 
  |AstTds.Booleen(b) ->Booleen(b),Bool
  |AstTds.Entier(n) ->Entier(n),Int
  |AstTds.Unaire(opu,exp) -> let exp1,t1 = analyse_type_expression tds  exp in 
                              (match opu with
                              |Numerateur ->  if est_compatible t1 Rat then Unaire(Numerateur,exp1),Int else raise (TypeInattendu (t1, Rat))
                              |Denominateur ->  if est_compatible t1 Rat then Unaire(Denominateur,exp1),Int else raise (TypeInattendu (t1, Rat)))
                              
  |AstTds.Binaire(opb, e1, e2) -> let exp1, t1 = analyse_type_expression tds e1 in 
                                  let exp2, t2 = analyse_type_expression tds e2 in 
                                   ( match opb, t1, t2 with 
                                    |Equ,Bool, Bool -> Binaire(EquBool,exp1,exp2),Bool
                                    |Equ,Int, Int ->  Binaire(EquInt,exp1,exp2),Bool
                                    |Fraction,Int, Int-> Binaire(Fraction,exp1,exp2),Rat
                                    |Fraction,Rat, Rat-> Binaire(Fraction,exp1,exp2),Rat
                                    |Plus, Int, Int-> Binaire(PlusInt,exp1,exp2),Int
                                    |Plus, Rat, Rat-> Binaire(PlusRat,exp1,exp2),Rat
                                    |Mult, Int, Int-> Binaire(MultInt,exp1,exp2),Int
                                    |Mult, Rat, Rat-> Binaire(MultRat,exp1,exp2),Rat
                                    |Inf, Int, Int-> Binaire(Inf,exp1,exp2),Bool
                                    |_,_ , _-> raise (TypeBinaireInattendu (opb, t1, t2))
                                   )
|AstTds.New t -> New t, Pointeur (chercherTypeTnomme tds t)
|AstTds.Null -> Null, Pointeur Undefined
| AstTds.Adresse info -> ( match info_ast_to_info info with
                          |InfoVar(_,t,_,_) -> Adresse(info), Pointeur (chercherTypeTnomme tds t)
                          | _ -> assert false ) 
|AstTds.StructAffect _ -> assert false (**traitement des enregistrement non fini *) 
 

 (** la tds est utilisée pour la gestion des types nomme *)
 (** tr est le type de retour utilisé par les fonctions *)
let rec analyse_type_instruction tds tr i =
  match i with
  | AstTds.Declaration (t, info, e) ->  let t2 = chercherTypeTnomme tds t in 
                                        let e1,t1 = analyse_type_expression tds e in                                        
                                        if est_compatible t1 t2 then 
                                            (modifier_type_info t2 info;                                            
                                            Declaration (info, e1))                                
                                        else
                                            raise (TypeInattendu (t1, t2))
  | AstTds.Affectation(a, e) -> let a1, t1 = analyse_type_affectable tds a in 
                                let e2, t2 = analyse_type_expression tds e in
                                if est_compatible t1 t2 then Affectation(a1, e2) 
                                else raise(TypeInattendu (t2, t1))
 | AstTds.Affichage (exp) -> let e1,t1 = analyse_type_expression tds exp in 
                            (match t1 with
                           |Int -> AffichageInt(e1)
                           |Rat -> AffichageRat(e1)
                           |Bool -> AffichageBool(e1)
                           |_ -> raise (TypeInattenduAffichage t1)) 
 |AstTds.Conditionnelle (exp, b1 ,b2) -> let e1,t1 = analyse_type_expression tds exp in
                                         let b11 = analyse_type_bloc tds tr b1 in
                                         let b22 = analyse_type_bloc tds tr b2 in 
                                         (match t1 with
                                        |Bool -> Conditionnelle(e1,b11,b22) 
                                        |_-> raise (TypeInattendu (t1, Bool))
                                         )
 |AstTds.TantQue (exp,b) -> let e1,t1 = analyse_type_expression tds exp in
                            if est_compatible t1 Bool then 
                              let b1 = analyse_type_bloc tds  tr b in                              
                                TantQue (e1,b1)
                            else
                                raise (TypeInattendu (t1,Bool))
 |AstTds.Retour(exp) -> let e1,t1 = analyse_type_expression tds exp in
                        if est_compatible t1  tr then 
                          Retour(e1)
                        else 
                          raise (TypeInattendu (t1,tr)) 
 |AstTds.Empty -> Empty                                                                                                          
 |AstTds.AssignAdd(a,e) -> let a1, t1 = analyse_type_affectable tds a in 
                                let e2, t2 = analyse_type_expression tds e in
                                (match t2 with
                               | Int| Rat->  if est_compatible t1 t2 then AssignAdd(a1, e2) 
                                              else raise(TypeInattendu (t2, t1)) 
                               | _-> failwith "L'operateur d'assignation d'addition prend un type Int ou Rat" )
 |AstTds.DeclTypeNomme(info,t) ->  (modifier_type_info (chercherTypeTnomme tds t) info;
                                match info_ast_to_info info with
                                |InfoVar(s,_,_,_)-> ajouter tds s info; DeclTypeNomme(info)     (** on ajoute dans la tds les types nommes déclarés *)                                        
                                |_ -> assert false )


and analyse_type_bloc tds tr li =
  let tdsfille = creerTDSFille tds in (**tds utilisée pour trouver le type effectif d'un type nomme *)
   let nli = (List.map (analyse_type_instruction tdsfille  tr ) li) in
   nli


(** fonction qui renvoie true si un bloc contient une instruction Retour, pour verifier qu'il n'y a pas e retour dans le main *)
let rec verifRetourDansBloc li = match li with
                               |[]-> false
                              | t::q -> (match t with
                                         |AstTds.Retour(_) -> true
                                         |_ ->verifRetourDansBloc q )  





let analyse_type_fonction tds  (AstTds.Fonction(t,info,lp,li))  = 
    let lps = List.map (fun (_,info) -> match info_ast_to_info info with 
                                         |InfoVar (_,t,_,_) -> modifier_type_info (chercherTypeTnomme tds t) info; info
                                         | _ -> assert false  ) lp in
    let _ = (match info_ast_to_info info with 
            |InfoFun (_,tr,tp) -> let tpbase = List.map (chercherTypeTnomme tds) tp in modifier_type_fonction_info (chercherTypeTnomme tds tr) tpbase info;
           | _ -> assert false) in  
    let b1 = analyse_type_bloc tds (chercherTypeTnomme tds t) li in   
    Fonction (info,lps,b1)




let analyser (AstTds.Programme (ltypnom,fonctions,prog)) = 
  let tdstypenom = creerTDSMere() in (**cette tds ne sert qu' à stocker les infos associées aux types nommes pour pouvoir récuperer leur type effectif *)
  (** on associe à chaque type nomme une info_ast *)
  let lpn = List.map (fun (info,_) -> match info_ast_to_info info with
                                       |InfoVar(s,_,_,_)-> ajouter  tdstypenom s info;
                                                            info
                                      |_ -> assert false  ) ltypnom in
  let nf = (List.map (analyse_type_fonction tdstypenom ) fonctions) in
  let  b = verifRetourDansBloc prog in 
  if b then raise (RetourDansMain) else
  let nb = analyse_type_bloc tdstypenom Undefined prog in
  Programme (lpn,nf,nb)

end 
 