package analizadorLexico.Errores;

public class DoubleInvalidoError extends ErrorLex {
    public DoubleInvalidoError(int line, int column, String lexema){
        super (line, column, "DOUBLE INVALIDO ", lexema);}
}
