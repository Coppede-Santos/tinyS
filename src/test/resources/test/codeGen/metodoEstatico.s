// Test de metodo estatico
// Salida esperada para -5:
// 25
// 7
class MathUtils {
}

impl MathUtils {
.(){}

st fn Int cuadrado(Int x){
ret x * x;
}

st fn Int suma(Int a, Int b){
ret a + b;
}

}

start{
Int x;

x = IO.in_int();

(IO.out_int(MathUtils.cuadrado(x)));
(IO.out_str("\n"));

(IO.out_int(MathUtils.suma(3, 4)));
(IO.out_str("\n"));
}
