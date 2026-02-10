class A{
    Array Str s;
}

impl A {
    fn Int getLengthOfElement(Int index){
        ret s[index].length();
    }

    st fn Int getStaticLength(A object, Int index){
        ret object.s[index].length();
    }

    .(Array Str array){
        s = array;
    }
}

start{
    Array Int arr;
    A a;
    Array Str strArr;
    Str nullStr;

    arr = new Int[5 + 3];
    arr[0] = 10;
    arr[3 * 2] = 30;
    strArr = new Str[4];
    strArr[0] = "hello";
    (IO.out_int(A.getStaticLength(a, 0)));
    nullStr = nil;
}