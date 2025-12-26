package analizadorSemantico.Errores;

public class HerenciaInvalidaError extends ErrorSemantico {
    public HerenciaInvalidaError(int line,int column,String lexema) {
        super(line,column, "No se puede heredar de la clase: ",lexema);
    }
}
