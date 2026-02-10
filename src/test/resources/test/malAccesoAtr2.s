class A {
    pub Int a;
    Int b;
}

impl A {
    .(Int a, Int b){
        self.a = a;
        self.b = b;
    }
}

class B : A{
    Int c;
    Int d;
}

impl B {
    .(){
        A aObj;
        aObj = new A(1,2);
        c = a + 1;
        d = b + 1; // Error: 'b' no es accesible
    }
}

start{
    B bObj;
    bObj = new B();

}