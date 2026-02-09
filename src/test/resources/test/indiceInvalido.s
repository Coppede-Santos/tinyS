class Clase {
    Array Str a;
}

impl Clase {
    .(Array Str a){
        self.a = a;
    }

    fn Array Str getA() {
        ret a;
    }

}

start{
    Int dimension;
    Array Str textos;
    Clase c;

    dimension = ((Int) 3.5);
    textos = new Str[dimension];
    c = new Clase(textos);
    (IO.out_str(textos["4"]));
}