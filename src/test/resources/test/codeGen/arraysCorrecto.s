class A {}

impl A {
    .() {}

    st fn printArray(Array Int arr) {
        Int i;
        i = 0;
        while (i < arr.length()) {
            (IO.out_int(arr[i]));
            (IO.out_str("\n"));
            (++i);
        }

    }
}

start {
    Array Int myArray;
    myArray = new Int[3];
    myArray[0] = 4;
    myArray[1] = 16;
    myArray[2] = 64;
    (A.printArray(myArray));
}