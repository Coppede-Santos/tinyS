start{
    (IO.out_str("Resultados de operaciones aritmeticas:\n"));
    (IO.out_str("Suma (4.2 + 5.4): "));
    (IO.out_double(4.2 + 5.4));
    (IO.out_str("\n"));

    (IO.out_str("Resta (10.2 - 3.2): "));
    (IO.out_double(10.2 - 3.2));
    (IO.out_str("\n"));

    (IO.out_str("Multiplicacion (2.1 * 6.5): "));
    (IO.out_double(2.1 * 6.5));
    (IO.out_str("\n"));

    (IO.out_str("Division (9 / 2): "));
    (IO.out_double(9 / 2.0));
    (IO.out_str("\n"));

    (IO.out_str("\nResultados de operaciones unarias:\n"));
    (IO.out_str("Decremento (--5.2): "));
    (IO.out_double(--5.2));
    (IO.out_str("\n"));

    (IO.out_str("Incremento (++10.4): "));
    (IO.out_double(++10.4));
    (IO.out_str("\n"));

    (IO.out_str("\nResultados de operaciones booleanas:\n"));
    (IO.out_str("AND (1 && 0): "));
    (IO.out_bool(true && false));
    (IO.out_str("\n"));

    (IO.out_str("OR (1 || 0): "));
    (IO.out_bool(true || false));
    (IO.out_str("\n"));

    (IO.out_str("NOT (!1): "));
    (IO.out_bool(!true));
    (IO.out_str("\n"));

    (IO.out_str("\nResultados de operaciones relacionales:\n"));
    (IO.out_str("Mayor que (5.2 > 3.4): "));
    (IO.out_bool(5.2 > 3.4));
    (IO.out_str("\n"));

    (IO.out_str("Menor que (2.1 < 4.1): "));
    (IO.out_bool(2.1 < 4.1));
    (IO.out_str("\n"));

    (IO.out_str("Igual a (6.5 == 6.7): "));
    (IO.out_bool(6.5 == 6.5));
    (IO.out_str("\n"));

    (IO.out_str("Diferente de (7.2 != 7.21): "));
    (IO.out_bool(7.2 != 7.21));
    (IO.out_str("\n"));

}