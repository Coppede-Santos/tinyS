// Este test es para probar el uso de arrays en casos limites
//Salida:
//i: 0
//v[i]: 0
//acc: 0
//i: 1
//v[i]: -1
//acc: -1

class StressArray {
    pub Array Double v;
    pub Int i;
    pub Double acc;
}

impl StressArray {

    fn Double run(Int n){
        v = new Double[n];
        i = 0;
        acc = 0;

        while (i < n){
            (IO.out_str("i: "));
            (IO.out_int(i));
            (IO.out_str("\n"));

            if (i % 2 == 0){
                v[i] = i;
            }
            else{
                v[i] = -i;
            }

            (IO.out_str("v[i]: "));
            (IO.out_double(v[i]));
            (IO.out_str("\n"));

            acc = acc + v[i];

            (IO.out_str("acc: "));
            (IO.out_double(acc));
            (IO.out_str("\n"));


            (++i);
            (IO.out_array_double(v));
            (IO.out_str("\n"));

            (IO.out_str("--------------------------------\n"));
        }
        ret acc;
    }

    .(){
        i = 0;
        acc = 0;
    }

}

start{
    StressArray s;
    Int n;

    (IO.out_str("Ingrese el tamaño del array: "));
    s = new StressArray();
    n = IO.in_int();

    (IO.out_double(s.run(n)));
    (IO.out_str("\n"));
}