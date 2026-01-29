class Contador {
pub Int valor;
}

impl Contador {

fn Int inc(){
valor = valor + 1;
ret valor;
}

fn Int dec(){
valor = valor - 1;
ret valor;
}

.(){
valor = 0;
}

}

start{
Contador c;

c = new Contador();

(IO.out_int(c.inc()));
(IO.out_str("\n"));
(IO.out_int(c.inc()));
(IO.out_str("\n"));
(IO.out_int(c.dec()));
(IO.out_str("\n"));
}