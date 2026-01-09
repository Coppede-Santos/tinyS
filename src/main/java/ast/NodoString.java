package ast;

import analizadorSemantico.EntradaMetodo;
import analizadorSemantico.Errores.ErrorSemantico;
import analizadorSemantico.SymbolTable;

public class NodoString extends NodoOperando{
    String valor;

    public NodoString(String valor, int linea, int columna) {
        super("Str", linea, columna);
        this.valor = valor;
    }

    @Override
    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st, String tipoEncadenadoPrev, int profundidad) throws ErrorSemantico {
        throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(), "prohibido encadenar una expresion","");
    }

    @Override
    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st, int profundidad) throws ErrorSemantico {
        String salida = "";

        salida += "\"" + valor + "\"";

        if (encadenado != null){
            this.encadenado.chequeoDeSentencias(
                entradaMetodo,
                st,
                this.tipo,
                profundidad + 1
            );
            this.tipo = encadenado.getTipo();
        }

        return salida;
    }

}
