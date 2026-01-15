class A {}
impl A {
    .() {}
    st fn B retB(Int x) {
        B b;
        if (x > 5) {
            ret new B(0);
        } else {
            ret new B(1);
        }
    }
}

class B {
    Bool x;
}
impl B {
    .(Int x) {
        if (x > 5) {
            self .x = false ;
        } else {
            self .x = true ;
        }
    }
}

start{}