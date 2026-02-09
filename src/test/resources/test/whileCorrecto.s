class C{
 pub Int i;
}
impl C{
 fn Int h(Int n){
   i=0;
   while(i<n){
     if(i==2){ i=i+1; }
     { i=i+1; }   // bloque siempre ejecutado
   }
   ret i;
 }
}
start{
 C c;
 c=new C();
 (c.h(3));
}
