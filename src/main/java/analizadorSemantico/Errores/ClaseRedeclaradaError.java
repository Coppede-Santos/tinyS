package analizadorSemantico.Errores;

public class ClaseRedeclaradaError extends ErrorSemantico {
    public ClaseRedeclaradaError(int line,int column,String lexema) {
        super(line,column, "La clase ya ha sido declarado anteriormente: ",lexema);
    }
}
