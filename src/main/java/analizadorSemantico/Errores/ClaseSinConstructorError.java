package analizadorSemantico.Errores;

public class ClaseSinConstructorError extends ErrorSemantico {
    public ClaseSinConstructorError(int line,int column,String lexema) {
        super(line,column, "La clase no tiene un constructor declarado: ",lexema);
    }
}
