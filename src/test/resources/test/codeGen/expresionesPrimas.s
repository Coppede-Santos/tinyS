start{

    (IO.out_str("Expresiones Primas\n"));

    (IO.out_str("Expresion 1: "));
    (IO.out_str("1 + 2 * 3 - 4 / 2 = "));
    (IO.out_int(1 + 2 * 3 - 4 div 2));
    (IO.out_str("\n"));

    (IO.out_str("Expresion 2: "));
    (IO.out_str("10 + 10 + 10 = "));
    (IO.out_int(10 + 10 + 10));
    (IO.out_str("\n"));

    (IO.out_str("Expresion 3: "));
    (IO.out_str("10 - 5 - 2 = "));
    (IO.out_int(10 - 5 - 2));
    (IO.out_str("\n"));

    (IO.out_str("Expresion 4: "));
    (IO.out_str("10 * 10 * 10 = "));
    (IO.out_int(10 * 10 * 10));
    (IO.out_str("\n"));

    (IO.out_str("Expresion 5: "));
    (IO.out_str("10 > 2 && 5 < 10 = "));
    (IO.out_bool(10 > 2 && 5 < 10));
    (IO.out_str("\n"));

    (IO.out_str("Expresion 6: "));
    (IO.out_str("10 > 2 || 5 > 10 || 4 == 7 = "));
    (IO.out_bool(10 > 2 || 5 > 10 || 4 == 7));
    (IO.out_str("\n"));
}