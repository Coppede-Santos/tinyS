//Este test es para comprobar que las cadenas funcionan correctamente
start{

Str a,b,c;
IO io;


a = "esta";
b = " funcionando";
c = "Exito";


io = new IO();
(io.out_str(a));
(io.out_str("\n"));
(io.out_str("Tamanio de la cadena a:"));
(IO.out_int(a.length()));
(IO.out_str("\n"));

(io.out_str(b));
(io.out_str("\n"));
(io.out_str("Tamanio de la cadena b: "));
(IO.out_int(b.length()));
(IO.out_str("\n"));

(io.out_str("Concat: "));
(io.out_str(a + b));
(io.out_str("\n"));
(IO.out_str(a.concat(b)));
(IO.out_str("\n"));
(io.out_str("Tamanio de la cadena ab: "));
(IO.out_int((a+b).length()));
(IO.out_str("\n"));


if("hola" == "hola"){
(IO.out_str("bien"));
(IO.out_str("\n"));
}else{
(IO.out_str("problemon"));
}

if(("hola" == "hola") && (a == "esta" && b != "nada que ver")){
(IO.out_str("funcionandooooo"));
}else{
(IO.out_str("problemon"));
}


(IO.out_str("\n"));
(io.out_str(c + " " + c + " "+c));
}