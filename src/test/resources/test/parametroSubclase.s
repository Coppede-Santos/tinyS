class Figura{
}

impl Figura{
    st fn dibujar(Figura f){
        (IO.out_str("Dibujando Figura\n"));
    }
    .(){}
}

class Circulo : Figura{
}

impl Circulo{
    .(){}
}

start{
    Circulo c;
    c = new Circulo();
    (Figura.dibujar(c));
}