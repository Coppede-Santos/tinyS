package ast.Errores;

import analizadorSemantico.Posicion;

public class MetodoNoDeclaradoError extends ErrorSentencia {
    public MetodoNoDeclaradoError(Posicion posicion, String lexema) {
        super(posicion, "El metodo no ha sido declarado: ", lexema);
    }
}
