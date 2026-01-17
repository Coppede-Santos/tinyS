package generacionDeCodigo;

import ast.NodoSentencia;

public class LHSVisitor extends NodeVisitor{
    MethodBodyVisitor valueVisitor;

    public LHSVisitor(MethodBodyVisitor valueVisitor){
        this.valueVisitor = valueVisitor;
    }
}
