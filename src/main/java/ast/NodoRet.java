package ast;

public class NodoRet extends NodoSentencia{
    NodoExp exp;

    public NodoRet(NodoExp exp){
        this.exp = exp;
    }

    public NodoExp getExp(){
        return exp;
    }
}
