package ast.Errores;

import analizadorSemantico.Posicion;

public class RetInvalidoError extends ErrorSentencia {
    public RetInvalidoError(Posicion posicion, String lexema) {
        super(posicion, "No se permite ret para este metodo ", lexema);
    }
}
