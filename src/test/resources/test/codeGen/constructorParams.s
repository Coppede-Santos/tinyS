class A {
    Int a;
    Int b;
}

impl A {
    .(Int a, Int b) {
        self.a = a;
        self.b = b;
    }

    fn toStr() {
        (IO.out_int(a));
        (IO.out_str(", "));
        (IO.out_int(b));
        (IO.out_str("\n"));
    }
}

start {
    A a;
    a = new A(10, 20);
    (a.toStr());
}