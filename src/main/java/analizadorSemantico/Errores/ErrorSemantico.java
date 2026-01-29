package analizadorSemantico.Errores;

import ErrorManage.ErrorTiny;

/** Clase que representa un error semántico genérico */
public class ErrorSemantico extends ErrorTiny {
    public ErrorSemantico(int line, int column, String descripcion, String lexema){
        super (line, column, descripcion,lexema);
    }
}
