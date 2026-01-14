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

class B {
    Int c;
    Int d;
}

impl B {
    .(){
        A aObj;
        aObj = new A(1,2);
        c = aObj.a + 1;
        d = aObj.b + 1;
    }
}

start{
    B bObj;
    bObj = new B();

}