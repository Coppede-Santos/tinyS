package ast.Errores;

import analizadorSemantico.Posicion;

public class ExpresionInvalidaError extends ErrorSentencia {
    public ExpresionInvalidaError(Posicion posicion, String lexema) {
        super(posicion, "Expresion invalida: ", lexema);
    }
}
