class D{
 pub Int x;
}
impl D{
 .(){
   x=0;
 }
 fn Int inc(){
   x=x+1;
   ret x;
 }
}
start{
 D d;
 d=new D();
 ((d.inc()));
}
