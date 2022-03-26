
open Compilateur


exception ErreurNonDetectee

(* Changer le chemin d'accès du jar. *)
let runtamcmde = "java -jar ../../runtam.jar"
(* let runtamcmde = "java -jar /mnt/n7fs/.../tools/runtam/runtam.jar" *)

(* Execute the TAM code obtained from the rat file and return the ouptut of this code *)
let runtamcode cmde ratfile =
  let tamcode = compiler ratfile in
  (*let (tamfile, chan) = Filename.open_temp_file "test" ".tam" in*)
  let tamfile = Filename.chop_extension ratfile ^ ".tam" in 
  let chan = open_out tamfile in 
  output_string chan tamcode;
  close_out chan;
  let ic = Unix.open_process_in (cmde ^ " " ^ tamfile) in
  let printed = input_line ic in 
  close_in ic;
  (*Sys.remove tamfile; *)   (* à commenter si on veut étudier le code TAM. *)
  String.trim printed
 
(* Compile and run ratfile, then print its output *)
let runtam ratfile =
  print_string (runtamcode runtamcmde ratfile)



let%expect_test "testaddTam" =
  runtam "../../fichiersRat/src-rat-addition-test/testadd1.rat";
  [%expect{| 7[37/40] |}]

let%test_unit "testAdd1"= 
  let _ = compiler "../../fichiersRat/src-rat-addition-test/testadd1.rat" in ()


