class StrUtils {
}

impl StrUtils {
    .(){}

    st fn Str repeatStr(Str s, Int n) {
        Str result;
        result = "";
        while (n > 0) {
            result = result + s;
            n = n - 1;
        }
        ret result;
    }

}

start{
    Str texto;
    texto = "Hola Mundo";

    (IO.out_str(StrUtils.repeatStr(3, texto)));
}