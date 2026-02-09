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
    fn set_a(Int b){
        a = b;
    }
}


impl B{
    .(){
        (self.set_a(10));
        b = 0;
        c = 0;
        d = 0;
    }
}


start{}