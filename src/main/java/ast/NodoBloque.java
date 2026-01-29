package ast;

import ErrorManage.ErrorTiny;
import analizadorSemantico.EntradaMetodo;
import analizadorSemantico.SymbolTable;

import java.util.LinkedList;

import static ast.AstJsonBuilder.*;

/** Clase que representa un nodo de bloque en el AST */
public class NodoBloque extends NodoSentencia{
    LinkedList<NodoSentencia> sentencias = new LinkedList<>();

    /** Constructor de la clase NodoBloque */
    public NodoBloque(int linea, int columna) {
        super(linea, columna);
    }

    /** Método para insertar una sentencia en el bloque */
    public void insertarSentencia(NodoSentencia nodoSentencia){
        sentencias.add(nodoSentencia);
    }

    /** Método para realizar el chequeo de sentencias en el bloque
     *
     * @param entradaMetodo Entrada del método actual en la tabla de símbolos
     * @param st Tabla de símbolos
     * @param profundidad Profundidad actual en el árbol
     * @return String con el resultado del chequeo en formato JSON
     * @throws ErrorTiny Si ocurre un error durante el chequeo
     */
    public String chequeoDeSentencias(EntradaMetodo entradaMetodo,
                                      SymbolTable st,
                                      int profundidad)
            throws ErrorTiny {

        String salida = "";
        salida += tabs(profundidad) + "[\n";

        for (NodoSentencia nodoSentencia : sentencias){
            salida += tabs(profundidad + 1) + "{\n";

            // Si el tipo es NodoBloque, agregamos info
            if (nodoSentencia instanceof NodoBloque){
                salida += tabs(profundidad + 2) +
                        claveJson("tipoNodo") +
                        valorJson("NodoBloque") + ",\n";
                salida += tabs(profundidad + 2) +
                        claveJson("sentencias") + "\n";
                salida += nodoSentencia.chequeoDeSentencias(
                        entradaMetodo, st, profundidad + 3
                );
            } else {
                salida += nodoSentencia.chequeoDeSentencias(
                        entradaMetodo, st, profundidad + 1
                );
            }

            if (nodoSentencia != sentencias.getLast()){
                salida += tabs(profundidad + 1) + "},\n";
            } else {
                salida += tabs(profundidad + 1) + "}\n";
            }
        }
        salida += tabs(profundidad) + "],\n";

        return salida;
    }
}
