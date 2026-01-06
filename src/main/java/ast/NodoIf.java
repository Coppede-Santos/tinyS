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
    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st) throws ErrorSemantico {
        String salida = "";
        if (condicion == null || sentenciaIf == null) throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(), ",","");

        condicion.chequeoDeSentencias(entradaMetodo,st);
        sentenciaIf.chequeoDeSentencias(entradaMetodo,st);

        if (sentenciaElse != null) sentenciaElse.chequeoDeSentencias(entradaMetodo,st);





        return salida;
    }
}
