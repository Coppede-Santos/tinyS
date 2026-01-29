package analizadorSemantico.Errores;

/** Clase que representa un error semántico de clase no declarada */
public class ClaseNoDeclaradaError extends ErrorSemantico {
    public ClaseNoDeclaradaError(int line,int column,String lexema) {
        super(line,column, "La clase no ha sido declarada anteriormente: ",lexema);
    }
}
