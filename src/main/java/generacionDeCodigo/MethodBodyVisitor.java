package generacionDeCodigo;

import analizadorSemantico.SymbolTable;
import ast.*;

public class MethodBodyVisitor extends NodeVisitor {
    public void generarCodigo(NodoWhile nw) {
        String doneLabel = "doneW" + genLabel(nw);
        String loopLabel = "loop" + genLabel(nw);

        codigo.agregarLinea(loopLabel + ":");
        nw.getCondicion().accept(); //Esto genera el codigo de la expresion que sirve como condicion del while
        codigo.agregarLinea("bne $a0, 1, " + doneLabel); //Si la condicion es falsa, salta al doneLabel (La condición se guarda en $a0)
        nw.getSentencia().accept(); //Genera el codigo de la sentencia dentro del while
        codigo.agregarLinea("j" + loopLabel);
        codigo.agregarLinea(doneLabel + ":");
    }

    public void generarCodigo(NodoIf nf) {

        String falseLabel = "falseI" + genLabel(nf);
        String doneLabel = "doneI" + genLabel(nf);

        nf.getCondicion().accept(); //Genera el codigo para la condición

        codigo.agregarLinea("bne $ao, 1, " + falseLabel); //Si no se cumple la condición salta a la labelFalse
        nf.getSentenciaIf().accept(); //Genera el codigo para la sentencia dentro del if


        codigo.agregarLinea("j" + doneLabel); //Salta al doneLabel

        codigo.agregarLinea(falseLabel + ":"); //Escribe la labelFalse
        if (nf.getSentenciaElse() != null) { //Si no tiene else lo deja vacio
            nf.getSentenciaElse().accept();  //Genera el codigo para la sentencia dentro del else
        }

        codigo.agregarLinea(doneLabel + ":"); //Escribe la labelDone
    }

    public void generarCodigo(NodoExpBin nb){
        nb.getLadoIzquierdo().accept();
        codigo.agregarLinea("sw $a0, 0($sp)"); //Guarda el valor del lado izquierdo en la pila
        codigo.agregarLinea("addi $sp, $sp, -4");
        nb.getLadoDerecho().accept();
        codigo.agregarLinea("lw $t1, 4($sp)");

        switch (nb.getOperador()){
            case PLUS:
                codigo.agregarLinea("add $a0, $t1, $a0");
                break;
            case MINUS:
                codigo.agregarLinea("sub $a0, $t1, $a0");
                break;
            case MULT:
                codigo.agregarLinea("mult $t1, $a0");
                codigo.agregarLinea("mflo $a0");
        }
    }

    public void generarCodigoPlus(NodoExpBin nb){
    }

    public void generarCodigo(NodoDouble nd){

        String doubleL =  "d_"+genLabel(nd);
        codigo.agregarData(doubleL + ": .double " + nd.getValor());
        codigo.agregarLinea("la $a0, " + doubleL);
    }






//    public void generarCodigo(NodoArray na){
//        na.
//    }



    public void generarCodigo(NodoSentencia ns){

    }

    public String genLabel(NodoSentencia ns){

        String salida = "L" + ns.posicion.getLinea() + "_" + ns.posicion.getColumna();
        return salida;
    }
}