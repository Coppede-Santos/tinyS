package ast;

import ErrorManage.ErrorTiny;
import analizadorSemantico.*;
import analizadorSemantico.Errores.ClaseNoDeclaradaError;
import ast.Errores.AtributoNoDeclaradoError;
import ast.Errores.VariableNoDeclaradaError;
import ast.Errores.VisibilidadError;
import generacionDeCodigo.MethodBodyVisitor;

import static ast.AstJsonBuilder.*;

public class NodoVar extends NodoOperando {
    String lexema;
    Boolean esEstatico = false;
    Boolean esEncadenado = false;
    String claseEncadenadoPrev = ""; //Clase que contiene la variable como atributo, clase que llama al metodo o "" en caso de no tener encadenado previo

    public NodoVar(String lexema, int linea, int columna) {
        super(linea, columna);
        this.lexema = lexema;
    }

    public void setEsEstatico(Boolean esEstatico) {
        this.esEstatico = esEstatico;

    }

    public Boolean getEsEstatico() {
        return esEstatico;
    }

    public String getClaseEncadenadoPrev() {
        return claseEncadenadoPrev;
    }


    public boolean getEsEncadenado() {
        return esEncadenado;
    }

    public String getLexema() {
        return lexema;
    }

    @Override
    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st, int profundidad) throws ErrorTiny {
        //No hay encadenamiento previo, se deja el valor false por defecto de esEncadenado
        String salida = "";

        salida += tabs(profundidad + 1) + claveJson("tipoNodo") + valorJson("NodoVariable") + ",\n";
        salida += tabs(profundidad + 1) + claveJson("lexema") + valorJson(lexema) + ",\n";

        EntradaClase claseReferenciada;

        EntradaVariable variable = entradaMetodo.buscarVariableLocal(lexema);
        if (variable == null) {
            variable = entradaMetodo.buscarParametro(lexema);
        }

        if (entradaMetodo != st.getStartMethod() && variable == null) {
            claseReferenciada = st.getClassActual();
            if (claseReferenciada != null) {
                variable = claseReferenciada.buscarAtributo(lexema);
                if (variable != null && entradaMetodo.esEstatico()){
                    throw new VisibilidadError(posicion,lexema);
                }

                EntradaAtributo atributo = (EntradaAtributo) variable;
                if (atributo != null && atributo.esPrivado()
                        && !st.getClassActual().getLexema().equals(atributo.getClasePropietaria())) {
                    throw new VisibilidadError(posicion,lexema);
                }
            }
        }

        if (variable == null) {
            if (!lexema.equals("self")) {
                if (esEstatico) {
                    EntradaClase claseActual = st.buscarClase(lexema);
                    if (claseActual == null) {
                        throw new ClaseNoDeclaradaError(posicion.getLinea(), posicion.getColumna(), lexema);
                    }
                    this.tipo = claseActual.getLexema();

                    salida += tabs(profundidad + 1) + claveJson("esEstatico") + valorJson("true") + ",\n";

                } else {
                    // ------------------------------------------------------------------------------------------------------------------------------------
                    throw new VariableNoDeclaradaError(posicion,lexema);
                }
            } else {
                this.tipo = st.getClassActual().getLexema();
            }
        } else {
            this.tipo = variable.getTipo();
        }

        salida += tabs(profundidad + 1) + claveJson("tipo") + valorJson(this.tipo) + ",\n";
        salida += tabs(profundidad + 1) + claveJson("posicion") + "{\n";
        salida += tabs(profundidad + 2) + claveJson("linea") + valorJson(String.valueOf(posicion.getLinea())) + ",\n";
        salida += tabs(profundidad + 2) + claveJson("columna") + valorJson(String.valueOf(posicion.getColumna())) + "\n";
        salida += tabs(profundidad + 1) + "}";

        if (encadenado != null) {
            salida += ",\n" + tabs(profundidad + 1) + claveJson("encadenado") + "\n";
            salida += tabs(profundidad + 2) + "{\n";
            salida += this.encadenado.chequeoDeSentencias(
                    entradaMetodo,
                    st,
                    this.tipo,
                    profundidad + 2
            );
            salida += tabs(profundidad + 2) + "}\n";
            this.tipo = encadenado.getTipo();
        } else {
            salida += "\n";
        }

        return salida;
    }

    @Override
    public void accept(MethodBodyVisitor methodBodyVisitor) {
        methodBodyVisitor.generarCodigo(this);
    }

    public void acceptLadoIzquerdo(MethodBodyVisitor methodBodyVisitor) {
        methodBodyVisitor.generarCodigoAccesoVariable(this);
    }

    @Override
    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st, String tipoEncadenadoPrev, int profundidad) throws ErrorTiny {
        //Hay un encadenado previo
        esEncadenado = true;
        claseEncadenadoPrev = tipoEncadenadoPrev;

        String salida = "";

        salida += tabs(profundidad + 1) + claveJson("tipoNodo") + valorJson("NodoVariable") + ",\n";
        salida += tabs(profundidad + 1) + claveJson("lexema") + valorJson(lexema) + ",\n";

        EntradaClase entradaClase = st.buscarClase(tipoEncadenadoPrev);
        if (entradaClase == null) {
            throw new ClaseNoDeclaradaError(posicion.getLinea(), posicion.getColumna(), tipoEncadenadoPrev);
        }

        EntradaAtributo atributo = entradaClase.buscarAtributo(lexema);
        if (atributo == null) {
            throw new AtributoNoDeclaradoError(posicion,lexema);
        }

        if (atributo.esPrivado()
                && (!st.getClassActual().getLexema().equals(tipoEncadenadoPrev)
                || !atributo.getClasePropietaria().equals(tipoEncadenadoPrev))) {
            throw new VisibilidadError(posicion,lexema);
        }

        this.tipo = atributo.getTipo();

        salida += tabs(profundidad + 1) + claveJson("tipo") + valorJson(this.tipo) + ",\n";
        salida += tabs(profundidad + 1) + claveJson("posicion") + "{\n";
        salida += tabs(profundidad + 2) + claveJson("linea") + valorJson(String.valueOf(posicion.getLinea())) + ",\n";
        salida += tabs(profundidad + 2) + claveJson("columna") + valorJson(String.valueOf(posicion.getColumna())) + "\n";
        salida += tabs(profundidad + 1) + "}";


        if (encadenado != null) {
            salida += ",\n";
            salida += tabs(profundidad + 1) + claveJson("encadenado") + "\n";
            salida += tabs(profundidad + 2) + "{\n";
            salida += this.encadenado.chequeoDeSentencias(
                    entradaMetodo,
                    st,
                    this.tipo,
                    profundidad + 2
            );
            salida += tabs(profundidad + 2) + "}\n";
            this.tipo = encadenado.getTipo();
        } else {
            salida += "\n";
        }

        return salida;
    }
}
