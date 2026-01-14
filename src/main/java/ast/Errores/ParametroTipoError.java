package ast.Errores;

import analizadorSemantico.Posicion;

public class ParametroTipoError extends ErrorSentencia {
    public ParametroTipoError(Posicion posicion, String lexema) {
        super(posicion, "Tipo de paramatro incorrecto: ", lexema);
    }
}
