class A{
    Int a; Int b;
}

impl A{
    .(){}
    fn m1(){}
    fn m2(){}
}

class B : A{
    Int c; Int d;
}

impl B{
    .(){}
    fn m3(){}
    fn m4(){}
}

class C : B{
    Int e; Int f;
}

impl C{
    .(){}
    fn m5(){}
    fn m6(){}
}

class D : A {
    Int g; Int h;
}

impl D {
    .(){}
    fn m7(){}
    fn m8(){}
}

start{}