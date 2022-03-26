
(* Module de la passe de gestion des identifiants *)
module PassePlacementRat 
: Passe.Passe with type t1 = Ast.AstType.programme and type t2 = Ast.AstPlacement.programme 
=
struct

  open Tds
  open Ast
  open Type

  type t1 = Ast.AstType.programme
  type t2 = Ast.AstPlacement.programme



let rec analyse_placement_instruction base reg i =
  match i with
  |AstType.Declaration(info,_)->(match info_ast_to_info info with
                               |InfoVar(_,t,_,_)-> modifier_adresse_info base reg info; 
                                                    (Type.getTaille t) + base  
                               |_ -> assert false)  
                                 
 |AstType.TantQue(_,b) -> analyse_placement_bloc base reg b;
                          base 
| AstType.Conditionnelle (_,b1,b2) ->analyse_placement_bloc base reg b1;
                                      analyse_placement_bloc base reg b2;
                                      base
|_ -> base     

and analyse_placement_bloc base reg li =  
List.fold_left (fun base i -> analyse_placement_instruction base reg i) base li
|> ignore
                                           
  


let analyse_placement_parametre infos = List.fold_right (fun info base -> match info_ast_to_info info with
                                                                          |InfoVar (_,t,_,_)-> let basePrime = base - getTaille t in
                                                                                                modifier_adresse_info basePrime "LB" info;
                                                                                                basePrime
                                                                         |_ -> assert false ) infos 0
                                       |> ignore



let analyse_placement_fonction (AstType.Fonction(info,infoLp,li))  = 
   analyse_placement_parametre infoLp;
   analyse_placement_bloc 3 "LB" li;
   AstPlacement.Fonction (info, infoLp,li) 



let analyser (AstType.Programme (_,fonctions,prog)) =
  let fonctionsPrime = List.map analyse_placement_fonction fonctions in 
  analyse_placement_bloc 0 "SB" prog;
  AstPlacement.Programme (fonctionsPrime, prog) 
end
