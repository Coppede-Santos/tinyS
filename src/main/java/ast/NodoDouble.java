package ast;

import analizadorSemantico.EntradaMetodo;
import analizadorSemantico.Errores.ErrorSemantico;
import analizadorSemantico.SymbolTable;

import static ast.AstJsonBuilder.*;
import static ast.AstJsonBuilder.claveJson;
import static ast.AstJsonBuilder.tabs;
import static ast.AstJsonBuilder.valorJson;

public class NodoDouble extends NodoNum{
    double valor;

    public NodoDouble(Double literal, int linea, int columna) {
        super("Double", linea, columna);
        valor = literal;
    }

    @Override
    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st, String tipoEncadenadoPrev, int profundidad) throws ErrorSemantico {
        throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(), "prohibido encadenar una expresion","");
    }

    @Override
    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st, int profundidad) throws ErrorSemantico {
        String salida = "";

        salida += tabs(profundidad + 1) + claveJson("tipoNodo") + valorJson("NodoDouble") + ",\n";
        salida += tabs(profundidad + 1) + claveJson("valor") + valorJson(String.valueOf(valor)) + ",\n";
        salida += tabs(profundidad + 1) + claveJson("tipo") + valorJson("Double") + ",\n";
        salida += tabs(profundidad + 1) + claveJson("posicion") + "{\n";
        salida += tabs(profundidad + 2) + claveJson("linea") + valorJson(String.valueOf(posicion.getLinea())) + ",\n";
        salida += tabs(profundidad + 2) + claveJson("columna") + valorJson(String.valueOf(posicion.getColumna())) + "\n";
        salida += tabs(profundidad + 1) + "}\n";

        if (encadenado != null){
            throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(),"","");
        }

        return salida;
    }
}
