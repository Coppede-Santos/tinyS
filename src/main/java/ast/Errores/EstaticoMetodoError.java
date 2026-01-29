package ast.Errores;

import analizadorSemantico.Posicion;

public class EstaticoMetodoError extends ErrorSentencia {
    public EstaticoMetodoError(Posicion posicion, String lexema) {
        super(posicion, "El metodo no fue declarado como estatico: ", lexema);
    }
}
