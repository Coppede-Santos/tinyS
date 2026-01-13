package ast.Errores;

import analizadorSemantico.Posicion;

public class TipoIndiceInvalidoError extends ErrorSentencia{
    public TipoIndiceInvalidoError(Posicion posicion, String tipo) {
        super(posicion, "El tipo del indice es invalido: ", tipo);
    }
}
