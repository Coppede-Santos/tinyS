class Calculadora {
pub Int a,b;
}

impl Calculadora {

fn Int max(Int x, Int y){
if (x > y){
ret x;
}
else{
ret y;
}
}

.(){
a = 0;
b = 0;
}

}

start{
Calculadora c;
Int x,y;

c = new Calculadora();
x = IO.in_int();
y = IO.in_int();

(IO.out_str("Maximo: "));
(IO.out_int(c.max(x,y)));
(IO.out_str("\n"));
}