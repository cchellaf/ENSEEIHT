(*open Compilateur
open Exceptions

exception ErreurNonDetectee

let%test_unit "test1"= 
  let _ = compiler "../../fichiersRat/src-rat-enregistrement-test/test1.rat" in ()

  let%test_unit "test2"= 
  let _ = compiler "../../fichiersRat/src-rat-enregistrement-test/test2.rat" in ()

  let%test_unit "test3"= 
  let _ = compiler "../../fichiersRat/src-rat-enregistrement-test/test3.rat" in ()


  let%test_unit "test4"=   
  try 
    let _ = compiler "../../fichiersRat/src-rat-enregistrement-test/test4DoubleDecl.rat"
    in raise ErreurNonDetectee 
  with
  | DoubleDeclaration("x") -> ()

  let%test_unit "test5"=   
  try 
    let _ = compiler "../../fichiersRat/src-rat-enregistrement-test/test5DoubleDecl.rat"
    in raise ErreurNonDetectee 
  with
  | DoubleDeclaration("x") -> () 

  let%test_unit "test6"= 
  let _ = compiler "../../fichiersRat/src-rat-enregistrement-test/test6Masquage.rat" in ()
(*)
   let%test_unit "test7"=   
  try 
    let _ = compiler "../../fichiersRat/src-rat-enregistrement-test/test7MauvaiseUtilID.rat"
    in raise ErreurNonDetectee 
  with
  | MauvaiseUtilisationIdentifiant("x") -> ()*)

   *)