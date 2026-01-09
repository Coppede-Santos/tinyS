package ast;

import analizadorSemantico.EntradaMetodo;
import analizadorSemantico.Errores.ErrorSemantico;
import analizadorSemantico.SymbolTable;

public class NodoIf extends NodoSentencia{
    NodoExp condicion;
    NodoSentencia sentenciaIf;
    NodoSentencia sentenciaElse;

    public NodoIf(NodoExp condicion, NodoSentencia sentenciaIf, NodoSentencia sentenciaElse, int linea, int columna) {
        super(linea, columna);
        this.condicion = condicion;
        this.sentenciaIf = sentenciaIf;
        this.sentenciaElse = sentenciaElse;
    }

    @Override
    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st, int profundidad) throws ErrorSemantico {
        String salida = "";
        if (condicion == null || sentenciaIf == null) throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(), ",","");

        salida += condicion.chequeoDeSentencias(entradaMetodo,st, profundidad + 1);

        if (!condicion.tipo.equals("Bool")){
            throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(), "La condicion de un if debe ser de tipo Bool","");
        }

        salida += sentenciaIf.chequeoDeSentencias(entradaMetodo,st, profundidad + 1);

        if (sentenciaElse != null) salida += sentenciaElse.chequeoDeSentencias(entradaMetodo,st, profundidad + 1);

        return salida;
    }
}
