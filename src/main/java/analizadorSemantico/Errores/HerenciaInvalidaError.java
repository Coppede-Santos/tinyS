package analizadorSemantico.Errores;

/** Clase que representa un error semántico de herencia inválida */
public class HerenciaInvalidaError extends ErrorSemantico {
    public HerenciaInvalidaError(int line,int column,String lexema) {
        super(line,column, "No se puede heredar de la clase: ",lexema);
    }
}
