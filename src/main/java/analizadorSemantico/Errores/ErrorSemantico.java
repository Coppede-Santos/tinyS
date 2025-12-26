package analizadorSemantico.Errores;

import ErrorManage.ErrorTiny;

public class ErrorSemantico extends ErrorTiny {
    public ErrorSemantico(int line, int column, String descripcion, String lexema){
        super (line, column, descripcion,lexema);
    }
}
