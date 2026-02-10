class Forma {
    Int a;
}

impl Forma {
    .(Int a){
        self.a = a;
    }
}



class Circulo : Forma{

}

impl Circulo {
    .(){
        (IO.out_int(self.a)); // Error: 'a' no es accesible
    }

    fn Int area(){
        B bObj;
        ret 0;
    }
}

class B {}
impl B {.(){}}


start{

}