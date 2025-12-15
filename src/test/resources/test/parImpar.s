class ParImpar {
}

impl ParImpar {

    /*
    *  Determina si un numero es par
    *  Recibe: Int n - numero a evaluar
    *  Retorna: Bool - true si es par, false si es impar
    */
    fn Bool esPar ( Int n ) {
        ret (n % 2) == 0;
    }

    /*
    *  Imprime si un numero es par o impar
    *  Recibe: Int n - numero a evaluar
    *  Retorna: void
    */
    fn void esParImpar ( Int n ) {
        if (self.esPar(n)) {
            (IO.out_str("El numero es par\n"));
        } else if (!self.esPar(n)) {
            (IO.out_str("El numero es impar\n"));
        } else {
            (IO.out_str("Error: No se pudo determinar si el numero es par o impar\n"));
        }
    }
}

start {
    ParImpar pi;
    Int n;
    pi = new ParImpar();
    n = IO.in_int();
    (pi.esParImpar(n));
}