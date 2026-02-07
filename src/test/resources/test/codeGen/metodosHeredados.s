// Salida esperada:
// m1 de A
// m1 de A
// m2 de B
// m3 de B
class A {

}

impl A {
    .(){}

    fn m1 () {
        (IO.out_str("m1 de A\n"));
    }

    fn m2 () {
        (IO.out_str("m2 de A\n"));
    }
}

class B : A {

}

impl B {
    .(){}

    fn m2 () {
        (IO.out_str("m2 de B\n"));
    }
    fn m3 () {
        (IO.out_str("m3 de B\n"));
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
    (b.m3());
}