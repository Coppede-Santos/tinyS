package ast;

public class NodoIf extends NodoSentencia{
    NodoExp condicion;
    NodoSentencia sentenciaIf;
    NodoSentencia sentenciaElse;

    public NodoIf(NodoExp condicion, NodoSentencia sentenciaIf, NodoSentencia sentenciaElse) {
        this.condicion = condicion;
        this.sentenciaIf = sentenciaIf;
        this.sentenciaElse = sentenciaElse;
    }

}
