package ast;

import ErrorManage.ErrorTiny;
import analizadorSemantico.EntradaMetodo;
import analizadorSemantico.EntradaVariables;
import analizadorSemantico.Errores.ErrorSemantico;
import analizadorSemantico.SymbolTable;
import ast.Errores.AsignacionInvalidaError;
import ast.Errores.TipoInvalidoError;
import ast.Errores.VariableNoDeclaradaError;

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
    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st, int profundidad) throws ErrorTiny {
        String salida = "";
        if (izquierda == null || derecha == null) throw new AsignacionInvalidaError(posicion,"=");

        salida += tabs(profundidad + 1) + claveJson("tipoNodo") + valorJson("NodoAsignacion") + ",\n";

        salida += tabs(profundidad + 1) + claveJson("izquierda") + "{\n";
        salida += izquierda.chequeoDeSentencias(entradaMetodo,st, profundidad + 1);
        salida += tabs(profundidad + 1) + "},\n";

        salida += tabs(profundidad + 1) + claveJson("derecha") + "{\n";
        salida += derecha.chequeoDeSentencias(entradaMetodo,st, profundidad + 1);
        salida += tabs(profundidad + 1) + "}\n";

        if (!Objects.equals(izquierda.getTipo(), derecha.getTipo()) && derecha.getTipo().isEmpty()) throw new TipoInvalidoError(posicion, izquierda.lexema, derecha.getTipo());

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
                throw new VariableNoDeclaradaError(posicion, izquierda.lexema);
            }

            String subtipoIzq = variableIzquierda.getSubtipo().getLexema();

            if (!Objects.equals(subtipoIzq, ((NodoConstructorArray) derecha).subtipo)) {
                throw new TipoInvalidoError(posicion, izquierda.lexema, subtipoIzq);
            }
        }

        return salida;
    }
}
