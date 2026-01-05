package ast;

public class NodoRet extends NodoSentencia{
    NodoExp exp;

    public NodoRet(NodoExp exp, int linea, int columna){
        super(linea, columna);
        this.exp = exp;
    }

    public NodoExp getExp(){
        return exp;
    }
}
