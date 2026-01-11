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
        String salida = "";
        salida +=  "[\n";
        for (NodoSentencia nodoSentencia : sentencias){
            salida += tabs(profundidad + 1) + "{\n";
            salida += nodoSentencia.chequeoDeSentencias(entradaMetodo, st, profundidad + 1);
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
