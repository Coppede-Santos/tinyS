package ast.Errores;

import analizadorSemantico.Posicion;

public class VisibilidadError extends ErrorSentencia {
    public VisibilidadError(Posicion posicion, String lexema) {
        super(posicion, "El atributo no es accesible: ", lexema);

    }
}
