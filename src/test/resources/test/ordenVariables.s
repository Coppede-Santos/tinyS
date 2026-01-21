class A{
    Int a; Int b;
}

impl A{
    .(Int a, Int b){
        self.a = a;
        self.b = b;
    }
    
    fn Int m1(Int c, Int d){
        Int e;
        Int f;
        Int g;

        e = c + d;
        f = a + b;
        g = e + f;
        ret g;
    }
}

start{
    A a;
    Int b;
    Int c;
    Int resultado;

    b = 5;
    c = 10;
    a = new A(b, c);

    resultado = a.m1(3, 7);
}