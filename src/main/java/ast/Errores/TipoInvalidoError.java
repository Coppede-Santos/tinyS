package ast.Errores;

import analizadorSemantico.Posicion;

public class TipoInvalidoError extends ErrorSentencia {
    public TipoInvalidoError(Posicion posicion, String lexema,String tipo) {
        super(posicion, "El tipo " + tipo +" es invalido: ", lexema);
    }
}
