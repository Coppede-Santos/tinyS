class Clase {
    Str a;
}

impl Clase {
    .(Str b){
        a = b;
    }

    fn Str getStr() {
        ret a;
    }
}

start{
    Str texto;
    Clase c;
    texto = "Hola Mundo";
    c = new Clase(texto);
    (IO.out_str(c.getStr().length().getDouble()));
}