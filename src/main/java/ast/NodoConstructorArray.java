package ast;

import ErrorManage.ErrorTiny;
import analizadorSemantico.EntradaMetodo;
import analizadorSemantico.SymbolTable;
import analizadorSemantico.Errores.ErrorSemantico;
import ast.Errores.EncadenadoInvalido;
import ast.Errores.TipoIndiceInvalidoError;
import ast.Errores.TipoInvalidoError;

import static ast.AstJsonBuilder.*;


public class NodoConstructorArray extends NodoOperando{
    String subtipo;
    NodoExp dimension;

    public NodoConstructorArray(String subtipo, int linea, int columna) {
        super("Array", linea, columna);
        // Convertir la primera letra a mayuscula y el resto a minuscula (PascalCase)
        this.subtipo = subtipo.substring(0, 1).toUpperCase() + subtipo.substring(1).toLowerCase();
    }

    public void setDimension(NodoExp nodoExp){
        this.dimension = nodoExp;
    }

    @Override
    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st, int profundidad) throws ErrorTiny {
        String salida = "";

        salida += tabs(profundidad + 1) + claveJson("tipoNodo") + valorJson("NodoConstructorArray") + ",\n";
        salida += tabs(profundidad + 1) + claveJson("subtipo") + valorJson(subtipo) + ",\n";
        salida += tabs(profundidad + 1) + claveJson("dimension") + "{\n";
        salida += dimension.chequeoDeSentencias(entradaMetodo, st, profundidad + 1);
        salida += tabs(profundidad + 1) + "},\n";

        if (!dimension.getTipo().equals("Int")){
            throw new TipoInvalidoError(posicion, "Array", dimension.getTipo());
        }

        // a = (new Array Int[dim]).length();

        if (encadenado != null){
            salida += this.encadenado.chequeoDeSentencias(
                    entradaMetodo,
                    st,
                    this.tipo,
                    profundidad + 1
            );
            this.tipo = encadenado.getTipo();
        }

        salida += tabs(profundidad + 1) + claveJson("tipo") + valorJson(this.tipo) + ",\n";
        salida += tabs(profundidad + 1) + claveJson("posicion") + "{\n";
        salida += tabs(profundidad + 2) + claveJson("linea") + valorJson(String.valueOf(posicion.getLinea())) + ",\n";
        salida += tabs(profundidad + 2) + claveJson("columna") + valorJson(String.valueOf(posicion.getColumna())) + "\n";
        salida += tabs(profundidad + 1) + "}\n";

        return salida;
    }

    @Override
    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st, String tipoEncadenadoPrev, int profundidad) throws ErrorTiny {
        throw new EncadenadoInvalido(posicion, tipoEncadenadoPrev);
    }

}
