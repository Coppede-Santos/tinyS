package ast.Errores;

import analizadorSemantico.Posicion;

public class ParametrosCantidadError extends ErrorSentencia{
    public ParametrosCantidadError(Posicion posicion, String lexema) {
        super(posicion, "Cantidad de parametros incorrecta: ", lexema);
    }
}
