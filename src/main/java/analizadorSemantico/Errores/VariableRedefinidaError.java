package analizadorSemantico.Errores;

public class VariableRedefinidaError extends ErrorSemantico {
    public VariableRedefinidaError(int line,int column,String lexema) {
        super(line,column, "La varable ya ha sido declarado anteriormente en este contexto: ",lexema);
    }
}
