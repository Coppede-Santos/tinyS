class VerificadorPrimo {
 pub Int numero;
 pub Bool esPrimo;
}
impl VerificadorPrimo {
 fn Bool verificar(Int n){
 Int d;
 esPrimo = true;
 numero = n;
 d = 2;
 while (d < numero) {
   if(numero % d == 0){
     esPrimo = false;
     (mensaje_no_primo());
     ret false;
   }
   (++d);
 }
 (mensaje_primo());
 ret true;
 }
 .(){
 numero = 0;
 esPrimo = true;
 }

 fn mensaje_primo(){
 (IO.out_str("Es primo\n"));
 }
 fn mensaje_no_primo(){
 (IO.out_str("No es primo\n"));
 }
}
start {
 VerificadorPrimo vp;
 Int entrada;
 vp = new VerificadorPrimo();
 entrada = IO.in_int();
 (vp.verificar(entrada));
}
