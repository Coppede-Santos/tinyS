package analizadorSemantico.Errores;

/** Clase que representa un error semántico de clase redeclarada */
public class ClaseRedeclaradaError extends ErrorSemantico {
    public ClaseRedeclaradaError(int line,int column,String lexema) {
        super(line,column, "La clase ya ha sido declarado anteriormente: ",lexema);
    }
}
