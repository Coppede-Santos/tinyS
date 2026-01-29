package ast.Errores;


import analizadorSemantico.Posicion;

public class AtributoNoDeclaradoError extends ErrorSentencia {
    public AtributoNoDeclaradoError(Posicion posicion, String lexema) {
        super(posicion, "El atributo no fue declarado: ", lexema);
    }
}
