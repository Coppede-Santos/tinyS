package ast;

import ErrorManage.ErrorTiny;
import analizadorSemantico.EntradaMetodo;
import analizadorSemantico.SymbolTable;
import ast.Errores.EncadenadoInvalido;

import java.util.Objects;

import static ast.AstJsonBuilder.claveJson;
import static ast.AstJsonBuilder.tabs;
import static ast.AstJsonBuilder.valorJson;

/** Clase que representa un nodo booleano en el AST */
public class NodoBool extends NodoOperando{

    boolean valor;

    /** Constructor de la clase NodoBool */
    public NodoBool(Boolean valor, int linea, int columna) {
        super("Bool", linea, columna);
        this.valor = valor;
    }

    /** Método para realizar el chequeo de sentencias con encadenado
     *
     * @param entradaMetodo Entrada del método actual en la tabla de símbolos
     * @param st Tabla de símbolos
     * @param tipoEncadenadoPrev Tipo del encadenado previo
     * @param profundidad Profundidad actual en el árbol
     * @return String con el resultado del chequeo en formato JSON
     * @throws ErrorTiny Si ocurre un error durante el chequeo
     */
    @Override
    public String chequeoDeSentencias(EntradaMetodo entradaMetodo,
                                      SymbolTable st,
                                      String tipoEncadenadoPrev,
                                      int profundidad)
            throws ErrorTiny {

        throw new EncadenadoInvalido(posicion, valor ? "true" : "false");
    }

    /** Método para realizar el chequeo de sentencias
     *
     * @param entradaMetodo Entrada del método actual en la tabla de símbolos
     * @param st Tabla de símbolos
     * @param profundidad Profundidad actual en el árbol
     * @return String con el resultado del chequeo en formato JSON
     * @throws ErrorTiny Si ocurre un error durante el chequeo
     */
    @Override
    public String chequeoDeSentencias(EntradaMetodo entradaMetodo,
                                      SymbolTable st,
                                      int profundidad)
            throws ErrorTiny {
        String salida = "";

        salida += tabs(profundidad + 1) + claveJson("tipoNodo")
                + valorJson("NodoBool") + ",\n";
        salida += tabs(profundidad + 1) + claveJson("valor")
                + Objects.toString(valor) + ",\n";
        salida += tabs(profundidad + 1) + claveJson("tipo")
                + valorJson("Bool") + ",\n";
        salida += tabs(profundidad + 1) + claveJson("posicion")
                + "{\n";
        salida += tabs(profundidad + 2) + claveJson("linea")
                + valorJson(String.valueOf(posicion.getLinea())) + ",\n";
        salida += tabs(profundidad + 2) + claveJson("columna")
                + valorJson(String.valueOf(posicion.getColumna())) + "\n";
        salida += tabs(profundidad + 1) + "}\n";

        if (encadenado != null){
            throw new EncadenadoInvalido(posicion, valor ? "true" : "false");
        }

        return salida;
    }
}
