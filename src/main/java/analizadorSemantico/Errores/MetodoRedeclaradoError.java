package analizadorSemantico.Errores;

public class MetodoRedeclaradoError extends ErrorSemantico {
    public MetodoRedeclaradoError(int line,int column,String lexema) {
        super(line,column, "El metodo ya ha sido declarado anteriormente: ",lexema);
    }
}
