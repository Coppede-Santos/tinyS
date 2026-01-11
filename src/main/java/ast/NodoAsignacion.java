package ast;

import analizadorSemantico.EntradaMetodo;
import analizadorSemantico.EntradaVariables;
import analizadorSemantico.Errores.ErrorSemantico;
import analizadorSemantico.SymbolTable;

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

    @Override
    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st, int profundidad) throws ErrorSemantico {
        String salida = "";
        if (izquierda == null || derecha == null) throw new ErrorSemantico(posicion.getLinea(),posicion.getColumna(),"aaaaaa","");

        salida += tabs(profundidad + 1) + claveJson("tipoNodo") + valorJson("NodoAsignacion") + ",\n";

        salida += tabs(profundidad + 1) + claveJson("izquierda") + "{\n";
        salida += izquierda.chequeoDeSentencias(entradaMetodo,st, profundidad + 1);
        salida += tabs(profundidad + 1) + "},\n";

        salida += tabs(profundidad + 1) + claveJson("derecha") + "{\n";
        salida += derecha.chequeoDeSentencias(entradaMetodo,st, profundidad + 1);
        salida += tabs(profundidad + 1) + "}\n";

        if (!Objects.equals(izquierda.getTipo(), derecha.getTipo())) throw new ErrorSemantico(posicion.getLinea(),posicion.getColumna(),"ooooooo","");

        // Array Int a;
        // a = new Int[5];

        if (derecha.getClass() == NodoConstructorArray.class) {
            EntradaVariables variableIzquierda = entradaMetodo.buscarVariableLocal(izquierda.lexema);
            if (variableIzquierda == null) {
                variableIzquierda = entradaMetodo.buscarParametro(izquierda.lexema);
            }

            if (variableIzquierda == null) {
                variableIzquierda = st.getClassActual().buscarAtributo(izquierda.lexema);
            }

            if (variableIzquierda == null) {
                throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(), "la variable " + izquierda.lexema + " no existe en el metodo " + entradaMetodo.getLexema(), "");
            }

            String subtipoIzq = variableIzquierda.getSubtipo().getLexema();

            if (subtipoIzq != ((NodoConstructorArray) derecha).subtipo) {
                throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(), "el subtipo del array en la asignacion no coincide", "");
            }
        }

        return salida;
    }
}
