// Test atributo privado

class Cuenta {
Int saldo;        // privado
}

impl Cuenta {

fn Int depositar(Int x){
saldo = saldo + x;
ret saldo;
}

fn Int get_saldo(){
ret saldo;}


fn prueba(){
}



.(){
saldo = 57;
}

}

start{
Cuenta c;

c = new Cuenta();

(IO.out_int(c.depositar(100)));
(IO.out_str("\n"));
(IO.out_int(c.get_saldo()));
(IO.out_str("\n"));
}
