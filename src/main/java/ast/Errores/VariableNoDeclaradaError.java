package ast.Errores;

import analizadorSemantico.Posicion;

public class VariableNoDeclaradaError extends ErrorSentencia {
    public VariableNoDeclaradaError(Posicion posicion, String lexema) {
        super(posicion, "La variable no ha sido declarado: ", lexema);
    }
}
