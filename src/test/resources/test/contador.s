class Contador {
 pub Int contador;
}
impl Contador {
 fn cuenta_regresiva(Int desde){
 contador = desde;
 while(contador >= 0){
   (IO.out_int(contador));
   (IO.out_str("...\n"));
   (--contador);
 }
 ret 0;
 }
 .(){
 contador = 0;
 }
}
start {
 Contador c;
 Int inicio;
 c = new Contador();
 inicio = IO.in_int();
 (c.cuenta_regresiva(inicio));
}
