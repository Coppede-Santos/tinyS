package analizadorSemantico.Errores;

/** Clase que representa un error semántico de redefinición de atributo */
public class RedefinirAtributoError extends ErrorSemantico {
    public RedefinirAtributoError(int line,int column,String lexema, String claseActual) {
        super(line,column, "La clase "+ claseActual+ " redefine un atributo heredado: ",lexema);
    }
}
