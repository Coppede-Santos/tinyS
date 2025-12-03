package analizadorLexico.Errores;

public class StringSinCerrarError extends ErrorLex {
    public StringSinCerrarError(int line, int column, String lexema){
        super (line, column, "STRING SIN TERMINAR ", lexema);}
}
