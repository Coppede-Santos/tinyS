impl ValorMaxArreglo {
    /* Retorna el valor maximo en un arreglo de enteros
    *  Recibe: Int[] arr - arreglo de enteros
    *           Int len - longitud del arreglo
    *  Retorna: Int - valor maximo en el arreglo
    */
    fn Int valor_max ( Array arr, Int len ) {
        Int max;
        Int i;
        max = arr[0];
        i = 1;

        while (i < len) {
            if (arr[i] > max) {
                max = arr[i];
            }
            (++i);
        }
        ret max;
    }
}

start {
    ValorMaxArreglo vma;
    Array arr;
    Int len;
    Int max_value;

    vma = new ValorMaxArreglo();
    len = 5;

    arr = new Int[len];
    arr[0] = 10;
    arr[1] = 25;
    arr[2] = 7;
    arr[3] = 30;
    arr[4] = 15;

    max_value = vma.valor_max(arr, len);
    (IO.out_int(max_value));
}