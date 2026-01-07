package ast;

import analizadorSemantico.EntradaMetodo;
import analizadorSemantico.Errores.ErrorSemantico;
import analizadorSemantico.SymbolTable;

public class NodoWhile extends NodoSentencia {
    NodoExp condicion;
    NodoSentencia sentencia;

    public NodoWhile(NodoExp condicion, NodoSentencia sentencia, int linea, int columna) {
        super(linea, columna);
        this.condicion = condicion;
        this.sentencia = sentencia;
    }

    @Override
    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st) throws ErrorSemantico {
        String salida = "";

        if(condicion == null || sentencia == null) throw new ErrorSemantico(posicion.getLinea(),posicion.getColumna(),"","");

        condicion.chequeoDeSentencias(entradaMetodo,st);

        if (!condicion.tipo.equals("Bool")){
            throw new ErrorSemantico(condicion.posicion.getLinea(), condicion.posicion.getColumna(),
                    "La condicion de un while debe ser de tipo Bool","");
        }

        sentencia.chequeoDeSentencias(entradaMetodo,st);

        return salida;
    }
}
