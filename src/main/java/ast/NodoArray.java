package ast;

import analizadorSemantico.*;
import analizadorSemantico.Errores.ErrorSemantico;

import java.util.Objects;

import static ast.AstJsonBuilder.*;

public class NodoArray extends NodoVar{
    NodoExp indice;

    public NodoArray(String lexema, int linea, int columna) {
        super(lexema, linea, columna);
    }

    @Override
    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st, int profundidad) throws ErrorSemantico {
        String salida = "";

        EntradaVariables variable = entradaMetodo.buscarVariableLocal(lexema);
        if (variable == null) {
            variable = entradaMetodo.buscarParametro(lexema);
        }

        if (variable == null) {
            variable = st.getClassActual().buscarAtributo(lexema);
        }

        if (variable == null) {
            throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(), "la variable " + lexema + " no existe en el metodo " + entradaMetodo.getLexema(), "");
        }

        EntradaClase subtipo = variable.getSubtipo();

        this.tipo = subtipo.getLexema();

        salida += tabs(profundidad + 1) + claveJson("tipoNodo") + valorJson("NodoArray") + ",\n";
        salida += tabs(profundidad + 1) + claveJson("lexema") + valorJson(lexema) + ",\n";
        salida += tabs(profundidad + 1) + claveJson("indice") + "{\n";
        salida += indice.chequeoDeSentencias(entradaMetodo, st, profundidad + 1);
        salida += tabs(profundidad + 1) + "},\n";

        if (!Objects.equals(indice.getTipo(), "Int")) {
            throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(), "el indice de un array debe ser de tipo Int","");
        }

        if (encadenado != null){
            salida += tabs(profundidad + 1) + claveJson("encadenado") + "{\n";
            salida += this.encadenado.chequeoDeSentencias(
                    entradaMetodo,
                    st,
                    this.tipo,
                    profundidad + 1
            );
            salida += tabs(profundidad + 1) + "},\n";
            this.tipo = encadenado.getTipo();
        }

        salida += tabs(profundidad + 1) + claveJson("tipo") + valorJson(this.tipo) + "\n";

        return salida;
    }

    @Override
    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st, String tipoEncadenadoPrev, int profundidad) throws ErrorSemantico {
        String salida = "";

        // Fibonacci.a[2]

        EntradaClase entradaClase = st.buscarClase(tipoEncadenadoPrev);
        if (entradaClase == null) {
            throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(), "la clase " + tipoEncadenadoPrev + " no existe","");
        }

        EntradaAtributos atributo = entradaClase.buscarAtributo(lexema);
        if (atributo == null) {
            throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(), "el atributo " + lexema + " no existe en la clase " + tipoEncadenadoPrev,"");
        }

        if (!Objects.equals(atributo.getTipo().getLexema(), "Array")) {
            throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(), "el atributo " + lexema + " no es un array","");
        }

        EntradaClase subtipo = atributo.getSubtipo();

        this.tipo = subtipo.getLexema();

        salida += tabs(profundidad + 1) + claveJson("tipoNodo") + valorJson("NodoArray") + ",\n";
        salida += tabs(profundidad + 1) + claveJson("lexema") + valorJson(lexema) + ",\n";
        salida += tabs(profundidad + 1) + claveJson("indice") + "{\n";
        salida += indice.chequeoDeSentencias(entradaMetodo, st, profundidad + 1);
        salida += tabs(profundidad + 1) + "},\n";

        if (!Objects.equals(indice.getTipo(), "Int")) {
            throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(), "el indice de un array debe ser de tipo Int","");
        }

        if (encadenado != null){
            salida += tabs(profundidad + 1) + claveJson("encadenado") + "{\n";
            salida += this.encadenado.chequeoDeSentencias(
                    entradaMetodo,
                    st,
                    this.tipo,
                    profundidad + 1
            );
            salida += tabs(profundidad + 1) + "},\n";
            this.tipo = encadenado.getTipo();
        }

        salida += tabs(profundidad + 1) + claveJson("tipo") + valorJson(this.tipo) + "\n";

        return salida;
    }

    public void setIndice(NodoExp indice) {
        this.indice = indice;
    }
}
