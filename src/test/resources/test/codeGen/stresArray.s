// Este test es para probar el uso de arrays en casos limites
//Salida:
//i: 0
//v[i]: 0
//acc: 0
//i: 1
//v[i]: -1
//acc: -1

class StressArray {
    pub Array Int v;
    pub Int i, acc;
}

impl StressArray {

    fn Int run(Int n){
        v = new Int[n];
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
            (IO.out_int(v[i]));
            (IO.out_str("\n"));

            acc = acc + v[i];

            (IO.out_str("acc: "));
            (IO.out_int(acc));
            (IO.out_str("\n"));


            (++i);
            (IO.out_array_int(v));
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

    (IO.out_int(s.run(n)));
    (IO.out_str("\n"));
}