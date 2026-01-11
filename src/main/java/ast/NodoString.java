package ast;

import analizadorSemantico.EntradaMetodo;
import analizadorSemantico.Errores.ErrorSemantico;
import analizadorSemantico.SymbolTable;

import static ast.AstJsonBuilder.*;

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

        salida += tabs(profundidad + 1) + claveJson("tipoNodo") + valorJson("NodoString") + ",\n";
        salida += tabs(profundidad + 1) + claveJson("valor") + valorJson(valor) + ",\n";
        salida += tabs(profundidad + 1) + claveJson("tipo") + valorJson("String") + ",\n";
        salida += tabs(profundidad + 1) + claveJson("posicion") + "{\n";
        salida += tabs(profundidad + 2) + claveJson("linea") + valorJson(String.valueOf(posicion.getLinea())) + ",\n";
        salida += tabs(profundidad + 2) + claveJson("columna") + valorJson(String.valueOf(posicion.getColumna())) + "\n";
        salida += tabs(profundidad + 1) + "}\n";

        if (encadenado != null){
            salida += ",\n";
            salida += tabs(profundidad + 1) + claveJson("encadenado") + "\n";
            salida += tabs(profundidad + 2) + "{\n";
            this.encadenado.chequeoDeSentencias(
                entradaMetodo,
                st,
                this.tipo,
                profundidad + 1
            );
            salida += tabs(profundidad + 2) + "}\n";
            this.tipo = encadenado.getTipo();
        }

        return salida;
    }

}
