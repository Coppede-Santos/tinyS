package analizadorSemantico.Errores;

/** Clase que representa un error semántico de redefinición de método */
public class RedefinirMetodoError extends ErrorSemantico {
    public RedefinirMetodoError(int line,int column,String lexema, String claseActual) {
        super(line,column, "La clase "+ claseActual+ " redefine un metodo heredado con firma distinta: ",lexema);
    }
}
