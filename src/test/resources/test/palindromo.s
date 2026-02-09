class Palindromo {
}

impl Palindromo {
    fn Bool es_palindromo ( Str s, Int len ) {
        Int inicio;
        Int fin;
        inicio = 0;
        fin = (len - 1);

        while (inicio < fin) {
            if ( (s[inicio] != s[fin]) ) {
                ret false;
            }
            (++inicio);
            (--fin);
        }
        ret true;
    }
}

start {
    Palindromo p;
    Str s;
    Int len;
    p = new Palindromo();
    s = "neuquen";
    len = s.length();
    if (p.es_palindromo(s, len)) {
        (IO.out_str("Es palindromo\n"));
    } else {
        (IO.out_str("No es palindromo\n"));
    }
}