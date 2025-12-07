package analizadorLexico.Errores;

public class IdentificadorInvalidoError extends ErrorLex {
    public IdentificadorInvalidoError(int line, int column, String lexema) {
        super(line, column, "IDENTIFICADOR INVALIDO ", lexema);
    }
}
