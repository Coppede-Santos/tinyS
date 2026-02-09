// Salida esperada:
// 1
// 2
// 3
// 4

class A{
    Int a;
    pub Int b;
}

class B : A{
    Int c;
    pub Int d;
}

impl A{
    .(){
        a = 0;
        b = 0;
    }
    fn set_a(Int as){
        a = as;
    }

    fn Int get_a(){
        ret a;
    }

}


impl B{
    .(){
        (self.set_a(1));
        b = 2;
        c = 3;
        d = 4;
    }
    fn print(){

        (IO.out_int(get_a()));
        (IO.out_str("\n"));
        (IO.out_int(b));
        (IO.out_str("\n"));
        (IO.out_int(c));
        (IO.out_str("\n"));
        (IO.out_int(d));

    }
}


start{
    B b;


    b = new B();

    (b.print());

}