package ast;

import analizadorSemantico.EntradaMetodo;
import analizadorSemantico.Errores.ErrorSemantico;
import analizadorSemantico.SymbolTable;

import java.util.LinkedList;

import static ast.AstJsonBuilder.*;

public class NodoBloque extends NodoSentencia{
    LinkedList<NodoSentencia> sentencias = new LinkedList<>();

    public NodoBloque(int linea, int columna) {
        super(linea, columna);
    }

    public void insertarSentencia(NodoSentencia nodoSentencia){
        sentencias.add(nodoSentencia);
    }



    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st, int profundidad) throws ErrorSemantico{
        String salida = tabs(4) + "{\n";
        salida += tabs(5) + claveJson("nombre") + valorJson(entradaMetodo.getLexema()) + ",\n";
        salida += tabs(5) + claveJson("posicion") + "{\n";
        salida += tabs(6) + claveJson("linea") + valorJson(String.valueOf(posicion.getLinea())) + ",\n";
        salida += tabs(6) + claveJson("columna") + valorJson(String.valueOf(posicion.getColumna())) + "\n";
        salida += tabs(5) + "},\n";
        salida += tabs(5) + claveJson("sentencias") + "[\n";

        for (NodoSentencia nodoSentencia : sentencias){
            salida += nodoSentencia.chequeoDeSentencias(entradaMetodo, st, 6);
            salida += ",\n";
        }
        salida += tabs(4) + "]\n";

        return salida;
    }
}
