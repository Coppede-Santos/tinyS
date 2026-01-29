package ast.Errores;

import ErrorManage.ErrorTiny;
import analizadorSemantico.Posicion;

public class ErrorSentencia extends ErrorTiny {
    public ErrorSentencia(Posicion posicion, String descripcion, String lexema) {
        super(posicion.getLinea(), posicion.getColumna(), descripcion, lexema);
    }
}

