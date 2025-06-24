class Factorial {
 pub Int resultado;
}
impl Factorial {
 fn Int calcular(Int n){
 Int i;
 resultado = 1;
 i = 2;
 while(i <= n){
   resultado = resultado * i;
   (++i);
 }
 (mostrar(resultado));
 ret resultado;
 }
 .(){
 resultado = 1;
 }
 fn mostrar(Int val){
 (IO.out_str("Resultado: "));
 (IO.out_int(val));
 (IO.out_str("\n"));
 }
}
start {
 Factorial fact;
 Int num;
 fact = new Factorial();
 num = IO.in_int();
 (fact.calcular(num));
}
