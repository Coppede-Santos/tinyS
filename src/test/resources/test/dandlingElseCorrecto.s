class B{}
impl B{
 fn Int g(Int a, Int b){
   if(a>0)
     if(b>0)
       ret 1;
     else
       ret 2;
 }
}
start{
 B b;
 b=new B();
 (b.g(1,2));
}
