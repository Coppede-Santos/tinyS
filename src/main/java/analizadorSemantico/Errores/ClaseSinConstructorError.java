package analizadorSemantico.Errores;

/** Clase que representa un error semántico de clase sin constructor */
public class ClaseSinConstructorError extends ErrorSemantico {
    public ClaseSinConstructorError(int line,int column,String lexema) {
        super(line,column, "La clase no tiene un constructor declarado: ",lexema);
    }
}
