package ast;

import ErrorManage.ErrorTiny;
import analizadorSemantico.EntradaClase;
import analizadorSemantico.EntradaMetodo;
import analizadorSemantico.EntradaVariable;
import analizadorSemantico.SymbolTable;
import ast.Errores.AsignacionInvalidaError;
import ast.Errores.TipoInvalidoError;
import ast.Errores.VariableNoDeclaradaError;
import generacionDeCodigo.MethodBodyVisitor;

import java.util.Objects;

import static ast.AstJsonBuilder.*;

public class NodoAsignacion extends NodoSentencia{
    NodoVar izquierda;
    NodoExp derecha;

    public NodoAsignacion(NodoVar izquierda, NodoExp derecha, int linea, int columna) {
        super(linea, columna);
        this.izquierda = izquierda;
        this.derecha = derecha;
    }

    public NodoVar getIzquierda() {
        return izquierda;
    }

    public NodoExp getDerecha() {
        return derecha;
    }

    @Override
    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st, int profundidad) throws ErrorTiny {
        String salida = "";
        if (izquierda == null || derecha == null) throw new AsignacionInvalidaError(posicion, "=");

        salida += tabs(profundidad + 1) + claveJson("tipoNodo") + valorJson("NodoAsignacion") + ",\n";

        salida += tabs(profundidad + 1) + claveJson("izquierda") + "{\n";
        salida += izquierda.chequeoDeSentencias(entradaMetodo, st, profundidad + 1);
        salida += tabs(profundidad + 1) + "},\n";

        salida += tabs(profundidad + 1) + claveJson("derecha") + "{\n";
        salida += derecha.chequeoDeSentencias(entradaMetodo, st, profundidad + 1);
        salida += tabs(profundidad + 1) + "}\n";


        // Verificamos que el tipo de la derecha sea compatible con el tipo de la izquierda
        // Si derecha es nil es compatible con todos
        if (!derecha.getTipo().equals("nil")) {

            //Buscamos el tipo de la derecha
            EntradaClase derechaClase = st.buscarClase(derecha.tipo);


            // Buscamos que el tipo de la izquierda sea ancestro de la clase derecha, en otro caso es error
            if ((!derechaClase.buscarAncestro(st,izquierda.tipo)) || derecha.getTipo().isEmpty()) {
                if (!(izquierda.getTipo().equals("Double")
                        && derecha.getTipo().equals("Int"))) {
                    throw new TipoInvalidoError(
                            posicion, izquierda.lexema, derecha.getTipo()
                    );
                }
            }


            // Array Int a;
            // a = new Int[5];

            // Si la derecha es un array, verificamos que el subtipo coincida con el de la variable izquierda
            if (derecha.getClass() == NodoConstructorArray.class) {

                //Buscamos si izquierda es una variable local o parametro
                EntradaVariable variableIzquierda = entradaMetodo.buscarVariableLocal(izquierda.lexema);
                if (variableIzquierda == null) {
                    variableIzquierda = entradaMetodo.buscarParametro(izquierda.lexema);
                }

                //Si no es una variable tiene que ser un atributo
                if (variableIzquierda == null) {
                    variableIzquierda = st.getClassActual().buscarAtributo(izquierda.lexema);
                }

                //Si no es una variable ni atributo es error
                if (variableIzquierda == null) {
                    throw new VariableNoDeclaradaError(posicion, izquierda.lexema);
                }


                //Vemos el subtipo de la variable izquierda
                String subtipoIzq = variableIzquierda.getSubtipo();

                //Si no coincide es error
                if (!Objects.equals(subtipoIzq, ((NodoConstructorArray) derecha).subtipo)) {
                    throw new TipoInvalidoError(posicion, izquierda.lexema, subtipoIzq);
                }
            }
        }

        return salida;
    }

    @Override
    public void accept(MethodBodyVisitor methodBodyVisitor) {
        methodBodyVisitor.generarCodigo(this);
    }
}
