package analizadorSemantico.Errores;

public class RedefinirMetodoError extends ErrorSemantico {
    public RedefinirMetodoError(int line,int column,String lexema, String claseActual) {
        super(line,column, "La clase "+ claseActual+ " redefine un metodo heredado con firma distinta: ",lexema);
    }
}
