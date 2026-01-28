class A {

}

impl A {
    .(){}

    fn m1 () {
        (IO.out_str("m1 de A\n"));
    }
}

class B : A {

}

impl B {
    .(){}

    fn m2 () {
        (IO.out_str("m2 de B\n"));
    }
}

start {
    A a;
    B b;
    a = new A();
    b = new B();
    (a.m1());
    (b.m1());
    (b.m2());
}