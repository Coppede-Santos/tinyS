package ast.Errores;

import analizadorSemantico.Posicion;

public class AsignacionInvalidaError extends ErrorSentencia {
    public AsignacionInvalidaError(Posicion posicion, String lexema) {
        super(posicion, "La asignacion es invalida: ", lexema);
    }
}
