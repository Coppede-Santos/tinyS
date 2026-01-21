package ast;

import ErrorManage.ErrorTiny;
import analizadorSemantico.*;
import analizadorSemantico.Errores.ClaseNoDeclaradaError;
import ast.Errores.TipoIndiceInvalidoError;
import ast.Errores.TipoInvalidoError;
import ast.Errores.VariableNoDeclaradaError;
import generacionDeCodigo.MethodBodyVisitor;

import java.util.Objects;

import static ast.AstJsonBuilder.*;

public class NodoArray extends NodoVar{
    NodoExp indice;

    public NodoArray(String lexema, int linea, int columna) {
        super(lexema, linea, columna);
    }

    public NodoExp getIndice() {
        return indice;
    }

    @Override
    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st, int profundidad) throws ErrorTiny {
        String salida = "";

        EntradaVariables variable = entradaMetodo.buscarVariableLocal(lexema);
        if (variable == null) {
            variable = entradaMetodo.buscarParametro(lexema);
        }

        if (variable == null) {
            variable = st.getClassActual().buscarAtributo(lexema);
        }

        if (variable == null) {
            throw new VariableNoDeclaradaError(posicion, lexema);
        }

        String subtipo = variable.getSubtipo();

        this.tipo = subtipo;

        salida += tabs(profundidad + 1) + claveJson("tipoNodo") + valorJson("NodoArray") + ",\n";
        salida += tabs(profundidad + 1) + claveJson("lexema") + valorJson(lexema) + ",\n";
        salida += tabs(profundidad + 1) + claveJson("indice") + "{\n";
        salida += indice.chequeoDeSentencias(entradaMetodo, st, profundidad + 1);
        salida += tabs(profundidad + 1) + "},\n";

        if (!Objects.equals(indice.getTipo(), "Int")) {
            throw new TipoIndiceInvalidoError(posicion, indice.getTipo());
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
    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st, String tipoEncadenadoPrev, int profundidad) throws ErrorTiny {
        String salida = "";

        // Fibonacci.a[2]

        EntradaClase entradaClase = st.buscarClase(tipoEncadenadoPrev);
        if (entradaClase == null) {
            throw new ClaseNoDeclaradaError(posicion.getLinea(), posicion.getColumna(), tipoEncadenadoPrev);
        }

        EntradaAtributos atributo = entradaClase.buscarAtributo(lexema);
        if (atributo == null) {
            throw new VariableNoDeclaradaError(posicion, lexema);
        }

        if (!Objects.equals(atributo.getTipo(), "Array")) {
            throw new TipoInvalidoError(posicion, lexema, atributo.getTipo());
        }

        String subtipo = atributo.getSubtipo();

        this.tipo = subtipo;

        salida += tabs(profundidad + 1) + claveJson("tipoNodo") + valorJson("NodoArray") + ",\n";
        salida += tabs(profundidad + 1) + claveJson("lexema") + valorJson(lexema) + ",\n";
        salida += tabs(profundidad + 1) + claveJson("indice") + "{\n";
        salida += indice.chequeoDeSentencias(entradaMetodo, st, profundidad + 1);
        salida += tabs(profundidad + 1) + "},\n";

        if (!Objects.equals(indice.getTipo(), "Int")) {
            throw new TipoIndiceInvalidoError(posicion, indice.getTipo());
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
