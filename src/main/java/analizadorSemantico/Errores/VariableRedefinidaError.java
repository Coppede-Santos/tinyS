package analizadorSemantico.Errores;

/** Clase que representa un error semántico de variable redefinida */
public class VariableRedefinidaError extends ErrorSemantico {
    public VariableRedefinidaError(int line,int column,String lexema) {
        super(line,column, "La variable ya ha sido declarado anteriormente en este contexto: ",lexema);
    }
}
