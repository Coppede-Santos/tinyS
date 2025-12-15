class A{
 pub Int x;
}
impl A{
 fn Int f(Int n){
   if(n>0){
     x=1;
   }
   { { { x=2; } } }   // bloques sueltos anidados
   ret x;
 }
}
start{
 A a;
 a=new A();
 (a.f(3));
}
