class SecuenciaPares {
 pub Int actual;
 pub Int limite;
}
impl SecuenciaPares {
 fn Int generar(Int n){
 actual = 0; limite = n;
 while(actual <= limite){
   if(actual % 2 == 0){
     (imprimir(actual));
   }
   (++actual);
 }
 ret 0;
 }
 .(){
 actual = 0;
 limite = 0;
 }
 fn imprimir(Int valor){
 (IO.out_str("par: "));
 (IO.out_int(valor));
 (IO.out_str("\n"));
 }
}
start {
 SecuenciaPares gen;
 Int num;
 gen = new SecuenciaPares();
 num = IO.in_int();
 (gen.generar(num));
}
