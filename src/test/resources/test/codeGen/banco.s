//Salida esperada:
//100
//60
//60
class Banco {
pub Int saldo;
}

impl Banco {

fn Int depositar(Int x){
saldo = saldo + x;
ret saldo;
}

fn Int extraer(Int x){
if (x <= saldo){
saldo = saldo - x;
}
ret saldo;
}

.(){
saldo = 0;
}

}

start{
Banco b;

b = new Banco();

(IO.out_int(b.depositar(100)));
(IO.out_str("\n"));
(IO.out_int(b.extraer(40)));
(IO.out_str("\n"));
(IO.out_int(b.extraer(100)));
(IO.out_str("\n"));
}
