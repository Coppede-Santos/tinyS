package analizadorLexico.Errores;

public class IntegerInvalidoError extends ErrorLex {
    public IntegerInvalidoError(int line, int column, String lexema){
        super (line, column, "INTEGER INVALIDO ", lexema);}
}
