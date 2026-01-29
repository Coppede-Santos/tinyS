package ast.Errores;

import analizadorSemantico.Posicion;

public class EncadenadoInvalido extends ErrorSentencia{
    public EncadenadoInvalido(Posicion posicion, String lexema) {
        super(posicion, "El encadenado es invalido: ", lexema);
    }
}
