class A{
    Int a;
}

class B : A{
    Int b;
    Double a;
}

impl A{
    .(Int a){
        self.a=a;
    }
}

impl B{
    .(Int b, Double a){
        self.b=b;
        self.a=a;
    }
}

start{}