class Sort {}

impl Sort {
    .() {}
}

class BubbleSort : Sort {}

impl BubbleSort {
    .() {}

    st fn void sort(Array Int arrNotSorted) {
        Int n;
        Int i;
        Int j;
        Int temp;
        Array Int arr;
        arr = arrNotSorted;

        n = (arr.length());

        i = 0;
        while (i < n) {
            j = 0;
            while (j < n - i - 1) {
                if ((arr[j]) > (arr[j + 1])) {
                    temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
                j = j + 1;
            }
            i = i + 1;
        }
    }
}

start{
    Array Int arr;

    arr = new Int[5];
    arr[0] = 64;
    arr[1] = 34;
    arr[2] = 25;
    arr[3] = 12;
    arr[4] = 22;

    (IO.out_str("Arreglo original: "));
    (IO.out_array_int(arr));

    (IO.out_str("\n"));
    (BubbleSort.sort(arr));

    (IO.out_str("Arreglo ordenado: "));
    (IO.out_array_int(arr));
}