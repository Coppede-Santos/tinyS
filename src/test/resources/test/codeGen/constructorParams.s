class A {
    Int a;
    Int b;
    Int c;
}

impl A {
    .(Int a, Int b, Int c) {
        self.a = a;
        self.b = b;
        self.c = c;
    }

    fn toStr() {
        (IO.out_int(a));
        (IO.out_str(", "));
        (IO.out_int(b));
        (IO.out_str(", "));
        (IO.out_int(c));
        (IO.out_str(", "));
        (IO.out_int(a));
        (IO.out_str(", "));
        (IO.out_int(b));
        (IO.out_str(", "));
        (IO.out_int(c));
        (IO.out_str("\n"));
    }
}

start {
    A a;
    a = new A(10, 20, 30);

    (a.toStr());
}