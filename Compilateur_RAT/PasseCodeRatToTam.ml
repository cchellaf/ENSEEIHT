
(* Module de la passe de gestion des identifiants *)
module PasseCodeRatToTam 
: Passe.Passe with type t1 = Ast.AstPlacement.programme and type t2 = string 
=
struct

  open Tds
  open Ast
  open Type
  open Code
 
  type t1 = Ast.AstPlacement.programme
  type t2 = string


(** le paramètre affect permet de savoir si on veut acceder à la valeur d'un pointeur ou bien on veut la modifier *)
let rec analyse_code_affectable a affect = 
match a with 
| AstType.Deref(a) -> let s, t = analyse_code_affectable a affect in
                      let s1 = if affect then "STOREI (" ^ (string_of_int(getTaille t)) ^ ") \n" else "LOADI (" ^ (string_of_int(getTaille t)) ^ ") \n" in 
                       (s^ s1, t)
| AstType.Ident(info) -> (match info_ast_to_info info with 
                         |InfoVar(_, t, d, r) -> ("LOAD ("^(string_of_int(getTaille t))^") "^(string_of_int d)^"["^r^"]"^"\n", t)  
                         |InfoConst(_,v) -> ("LOADL " ^ (string_of_int v) ^ "\n", Int) 
                         |_ -> assert false  )



let rec analyse_code_expression e = 
match e with
 |AstType.AppelFonction(info, liste) -> (match info_ast_to_info info with 
                                             |InfoFun(s, _, _) -> (String.concat "" (List.map(analyse_code_expression) liste)) ^ 
                                                                  "CALL (SB) " ^ s ^ "\n"       
                                             | _ -> assert false  )
 |AstType.Affectable(a) -> let s, _ = analyse_code_affectable a false in s 
 |AstType.Booleen(b) -> (match b with 
                             |true -> "LOADL 1 \n"
                             |false -> "LOADL 0 \n" ) 
 |AstType.Entier(n) -> "LOADL " ^ (string_of_int n) ^ "\n"
 |AstType.Unaire(opu, e) -> let s = analyse_code_expression e in
                                (match opu with 
                                 |Numerateur -> s ^ "POP (0) 1 \n" 
                                 |Denominateur -> s ^ "POP (1) 1 \n" )  
 |AstType.Binaire(opb, e1, e2) -> let s1 = analyse_code_expression e1 in
                                        let s2 = analyse_code_expression e2 in 
                                        (match opb with 
                                        | AstType.PlusInt -> s1 ^ s2 ^ "SUBR IAdd \n"
                                        | AstType.PlusRat -> s1 ^ s2 ^ "CALL (SB) RAdd\n "  
                                        | AstType.MultInt -> s1 ^ s2 ^ "SUBR IMul \n"
                                        | AstType.MultRat -> s1 ^ s2 ^ "CALL (SB) RMul \n"
                                        | AstType.EquInt -> s1 ^ s2 ^ "SUBR IEq \n" 
                                        | AstType.EquBool -> s1 ^ "SUBR B2I \n" ^ s2 ^ "SUBR B2I \n" ^ "SUBR IEq \n" 
                                        | AstType.Inf -> s1 ^ s2 ^ "SUBR ILss \n"
                                        | AstType.Fraction -> s1 ^ s2 ^  "CALL (SB) norm \n"  )
|AstType.New t -> "LOADL " ^ (string_of_int(getTaille t)) ^ "\n" ^ "SUBR Malloc \n"
| AstType.Null -> ""
|AstType.Adresse info -> (match info_ast_to_info info with
                         |InfoVar(_, _, d, r) -> "LOADA " ^ (string_of_int d) ^ "[" ^ r ^ "]" ^ "\n"   
                         | _ -> assert false)




let rec typePointeur t = match t with
| Pointeur (Int) -> Int
| Pointeur (Rat) -> Rat
| Pointeur (t1) -> typePointeur t1
| _ -> assert false


let rec analyse_code_instruction i =
  match i with
  |AstType.Declaration(info,e)->(match info_ast_to_info info with
                               |InfoVar(_,t,d,r)-> "PUSH "^ string_of_int(getTaille t)^"\n"
                                 ^ analyse_code_expression e
                                 ^ "STORE ("^ string_of_int(getTaille t) ^ ") " ^ string_of_int d ^ "[" ^ r ^ "]" ^ "\n" 
                               |_ -> assert false)  
  |AstType.Affectation(a, e) -> (match a with 
                                | AstType.Ident info -> 
                                      (match info_ast_to_info info with
                                        | InfoVar(_,t,d,r) -> analyse_code_expression e ^ 
                                                                "STORE (" ^ string_of_int(getTaille t)^") "^string_of_int d ^"["^r^"]"^"\n" 
                                        | _ -> assert false)   
                                | AstType.Deref _ ->let s1 = analyse_code_expression e in 
                                                     let s2, _ = analyse_code_affectable a true in 
                                                     s1 ^ s2)                    
  |AstType.Conditionnelle(e,b1,b2) -> let ne = analyse_code_expression e in 
                                      let s1, taille1 = analyse_code_bloc b1 in
                                      let s2, taille2 = analyse_code_bloc b2 in 
                                      let etiq_else = getEtiquette() in 
                                      let etiq_fin = getEtiquette() in 
                                      ne^"JUMPIF (0) "^etiq_else^"\n"^s1^"POP(0) "^(string_of_int taille1)^"\n"^"JUMP "^etiq_fin^"\n"
                                      ^etiq_else^"\n"^s2^"POP (0) "^(string_of_int taille2)^"\n"^etiq_fin^"\n"
  |AstType.TantQue(e,b) -> let ne = analyse_code_expression e in
                           let s, taille = analyse_code_bloc b in 
                           let whileDebut = getEtiquette() in
                           let whileFin = getEtiquette() in 
                           whileDebut^"\n"^ne^"JUMPIF (0) "^whileFin^"\n"^
                           s^"POP (0) "^(string_of_int taille)^"\n"^"JUMP "^whileDebut^"\n"^whileFin^"\n"
  |AstType.Empty -> ""   
  |AstType.AffichageInt e -> (analyse_code_expression e) ^ 
                              "SUBR IOut " ^ "\n"      
  |AstType.AffichageBool e -> (analyse_code_expression e) ^ 
                              "SUBR BOut " ^ "\n" 
  |AstType.AffichageRat e -> (analyse_code_expression e) ^ 
                              "CALL (SB) ROut" ^ "\n" 
  |AstType.Retour(e) -> analyse_code_expression e 
  |AstType.AssignAdd(a,e) -> (match a with 
                                | AstType.Ident info -> 
                                      (match info_ast_to_info info with
                                        | InfoVar(_,t,d,r) ->let s = if est_compatible t Int then "SUBR IAdd\n " else "CALL (SB) RAdd\n " in "LOAD (" ^ string_of_int(getTaille t)^") "^string_of_int d ^"["^r^"]"^"\n" ^
                                                               analyse_code_expression e ^  s ^        
                                                                "STORE (" ^ string_of_int(getTaille t)^") "^string_of_int d ^"["^r^"]"^"\n" 
                                        | _ -> assert false)   
                                | AstType.Deref _ ->let s1 = analyse_code_expression e in 
                                                     let s2, _ = analyse_code_affectable a false in
                                                      let s3, pt3 = analyse_code_affectable a true in
                                                      let s = if est_compatible (typePointeur pt3) Int then "SUBR IAdd\n " else "CALL (SB) RAdd\n " in
                                                     s1 ^ s2 ^ s ^ s3  )   
| _ -> "" 
and analyse_code_bloc li =     
(String.concat "" (List.map(analyse_code_instruction) li)), calcul_taille_bloc li


(** calcul la taille total des variables déclarées dans un bloc *)
and calcul_taille_bloc li = List.fold_right (fun i tq -> (calcul_taille_instruction i) + tq ) li 0


(**calcule la taille d'une variable déclarée *)
 and calcul_taille_instruction i = 
 match i with 
 | AstType.Declaration(info, _)-> (match info_ast_to_info info with 
                                    | InfoVar(_,t,_,_) -> getTaille t                               
                                    |__-> 0 
                                    ) 
 | _ -> 0 
    

(*calcule la taille des paramètres *)
let getTailleParametres tp = List.fold_right (fun t qt -> qt + (getTaille t)) tp 0 


(**verifie l'existence d'un retour dans un bloc, elle est utilisé pour gerer les fonctions qui n'ont pas l'instruction retour *)
let rec verifRetourDansBloc li  = match li with
                               |[]-> false
                               | t::q -> (match t with
                                         |AstType.Retour(_) -> true                                          
                                         |_ ->verifRetourDansBloc q )


let  analyse_code_fonction (AstPlacement.Fonction(info,_,li))  = 
   match info_ast_to_info info with
  |InfoFun(s,t,tl)-> let s1,taille = analyse_code_bloc li in
                    let pop = let b = verifRetourDansBloc li in if b then getTaille t else 0 in  
                     s ^ "\n" ^ s1 ^"POP (" ^ (string_of_int (pop)) ^ ") "^(string_of_int taille)^"\n"^ "RETURN (" ^ string_of_int(getTaille t) ^ ") " ^ string_of_int(getTailleParametres tl) ^ "\n\n"
  |_-> assert false  



let analyser (AstPlacement.Programme (fonctions,prog)) =
let s,taille = analyse_code_bloc prog in 
let entete = getEntete () in 
  entete ^
 (String.concat "" (List.map analyse_code_fonction fonctions)) ^ "main \n" ^
  s ^"POP (0) "^(string_of_int taille)^"\nHALT"
end
