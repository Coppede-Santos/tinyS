class FizzBuzz {
}

impl FizzBuzz {
    fn void fizzbuzz ( Int n ) {
        Int i;
        i=1;

        while (i<=n) {
            if ( ( (i % 3) == 0 ) && ( (i % 5) == 0 ) ) {
                (IO.out_str("FizzBuzz\n"));
            } else {
                if ( (i % 3) == 0 ) {
                    (IO.out_str("Fizz\n"));
                } else {
                    if ( (i % 5) == 0 ) {
                        (IO.out_str("Buzz\n"));
                    }
                }
            }
            (++i);
        }
    }
}

start {
    FizzBuzz fb;
    Int n;
    fb = new FizzBuzz();
    n = IO.in_int();
    (fb.fizzbuzz(n));
}