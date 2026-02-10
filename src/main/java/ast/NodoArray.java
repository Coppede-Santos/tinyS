package ast;

import ErrorManage.ErrorTiny;
import analizadorSemantico.*;
import analizadorSemantico.Errores.ClaseNoDeclaradaError;
import ast.Errores.TipoIndiceInvalidoError;
import ast.Errores.TipoInvalidoError;
import ast.Errores.VariableNoDeclaradaError;
import java.util.Objects;
import static ast.AstJsonBuilder.*;

/** Clase que representa un nodo de acceso a un arreglo */
public class NodoArray extends NodoVar{
    NodoExp indice;

    /** Constructor de la clase NodoArray
     * @param lexema Lexema del nodo
     * @param linea Línea donde se encuentra el nodo
     * @param columna Columna donde se encuentra el nodo
     */
    public NodoArray(String lexema, int linea, int columna) {
        super(lexema, linea, columna);
    }

    /** Método para el chequeo de sentencias del nodo de acceso a un arreglo
     *
     * @param entradaMetodo Entrada del método actual
     * @param st Tabla de símbolos
     * @param profundidad Profundidad en el árbol de análisis
     * @return String con la representación JSON del nodo
     * @throws ErrorTiny Si ocurre un error semántico durante el chequeo
     */
    @Override
    public String chequeoDeSentencias(EntradaMetodo entradaMetodo,
                                      SymbolTable st,
                                      int profundidad) throws ErrorTiny {
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

        this.tipo = variable.getSubtipo();

        salida += tabs(profundidad + 1) + claveJson("tipoNodo")
                + valorJson("NodoArray") + ",\n";
        salida += tabs(profundidad + 1) + claveJson("lexema")
                + valorJson(lexema) + ",\n";
        salida += tabs(profundidad + 1) + claveJson("indice")
                + "{\n";
        salida += indice.chequeoDeSentencias(
                entradaMetodo,
                st,
                profundidad + 1
        );
        salida += tabs(profundidad + 1) + "},\n";

        if (!Objects.equals(indice.getTipo(), "Int")) {
            throw new TipoIndiceInvalidoError(posicion, indice.getTipo());
        }

        if (encadenado != null){
            salida += tabs(profundidad + 1) +
                    claveJson("encadenado") + "{\n";
            salida += this.encadenado.chequeoDeSentencias(
                    entradaMetodo,
                    st,
                    this.tipo,
                    profundidad + 1
            );
            salida += tabs(profundidad + 1) + "},\n";
            this.tipo = encadenado.getTipo();
        }

        salida += tabs(profundidad + 1) + claveJson("tipo") +
                valorJson(this.tipo) + "\n";

        return salida;
    }

    /** Método para el chequeo de sentencias del nodo de acceso a un arreglo con encadenado previo
     *
     * @param entradaMetodo Entrada del método actual
     * @param st Tabla de símbolos
     * @param tipoEncadenadoPrev Tipo del encadenado previo
     * @param profundidad Profundidad en el árbol de análisis
     * @return String con la representación JSON del nodo
     * @throws ErrorTiny Si ocurre un error semántico durante el chequeo
     */
    @Override
    public String chequeoDeSentencias(EntradaMetodo entradaMetodo,
                                      SymbolTable st,
                                      String tipoEncadenadoPrev,
                                      int profundidad)
            throws ErrorTiny {
        String salida = "";

        EntradaClase entradaClase = st.buscarClase(tipoEncadenadoPrev);
        if (entradaClase == null) {
            throw new ClaseNoDeclaradaError(
                    posicion.getLinea(), posicion.getColumna(),
                    tipoEncadenadoPrev
            );
        }

        EntradaAtributos atributo = entradaClase.buscarAtributo(lexema);
        if (atributo == null) {
            throw new VariableNoDeclaradaError(posicion, lexema);
        }

        if (!Objects.equals(atributo.getTipo(), "Array")) {
            throw new TipoInvalidoError(posicion, lexema, atributo.getTipo());
        }

        this.tipo = atributo.getSubtipo();

        salida += tabs(profundidad + 1) + claveJson("tipoNodo")
                + valorJson("NodoArray") + ",\n";
        salida += tabs(profundidad + 1) + claveJson("lexema")
                + valorJson(lexema) + ",\n";
        salida += tabs(profundidad + 1) + claveJson("indice")
                + "{\n";
        salida += indice.chequeoDeSentencias(
                entradaMetodo, st, profundidad + 1
        );
        salida += tabs(profundidad + 1) + "},\n";

        if (!Objects.equals(indice.getTipo(), "Int")) {
            throw new TipoIndiceInvalidoError(posicion, indice.getTipo());
        }

        if (encadenado != null){
            salida += tabs(profundidad + 1) +
                    claveJson("encadenado") + "{\n";
            salida += this.encadenado.chequeoDeSentencias(
                    entradaMetodo,
                    st,
                    this.tipo,
                    profundidad + 1
            );
            salida += tabs(profundidad + 1) + "},\n";
            this.tipo = encadenado.getTipo();
        }

        salida += tabs(profundidad + 1) + claveJson("tipo")
                + valorJson(this.tipo) + "\n";

        return salida;
    }

    /** Setter del índice del arreglo.
     */
    public void setIndice(NodoExp indice) {
        this.indice = indice;
    }
}
