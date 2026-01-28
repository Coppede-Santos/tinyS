start{
    (IO.out_str("Resultados de operaciones aritmeticas:\n"));
    (IO.out_str("Suma (4 + 5): "));
    (IO.out_int(4 + 5));
    (IO.out_str("\n"));

    (IO.out_str("Resta (10 - 3): "));
    (IO.out_int(10 - 3));
    (IO.out_str("\n"));

    (IO.out_str("Multiplicacion (2 * 6): "));
    (IO.out_int(2 * 6));
    (IO.out_str("\n"));

    (IO.out_str("Division (8 / 2): "));
    (IO.out_int(8 / 2));
    (IO.out_str("\n"));

    (IO.out_str("Modulo (7 % 3): "));
    (IO.out_int(7 % 3));
    (IO.out_str("\n"));

    (IO.out_str("\nResultados de operaciones unarias:\n"));
    (IO.out_str("Decremento (--5): "));
    (IO.out_int(--5));
    (IO.out_str("\n"));

    (IO.out_str("Incremento (++10): "));
    (IO.out_int(++10));
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
    (IO.out_str("Mayor que (5 > 3): "));
    (IO.out_bool(5 > 3));
    (IO.out_str("\n"));

    (IO.out_str("Menor que (2 < 4): "));
    (IO.out_bool(2 < 4));
    (IO.out_str("\n"));

    (IO.out_str("Igual a (6 == 6): "));
    (IO.out_bool(6 == 6));
    (IO.out_str("\n"));

    (IO.out_str("Diferente de (7 != 5): "));
    (IO.out_bool(7 != 5));
    (IO.out_str("\n"));

}