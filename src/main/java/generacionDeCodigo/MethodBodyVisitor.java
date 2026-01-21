package generacionDeCodigo;

import analizadorSemantico.EntradaClase;
import ast.*;

import java.util.LinkedList;
import java.util.Objects;

public class MethodBodyVisitor extends NodeVisitor {


    public void generarCodigo(NodoWhile nw) {
        String doneLabel = "doneW" + genLabel(nw);
        String loopLabel = "loop" + genLabel(nw);

        codigo.agregarLinea(loopLabel + ":");
        nw.getCondicion().accept(this); //Esto genera el codigo de la expresion que sirve como condicion del while
        codigo.agregarLinea("bne $a0, 1, " + doneLabel); //Si la condicion es falsa, salta al doneLabel (La condición se guarda en $a0)
        nw.getSentencia().accept(this); //Genera el codigo de la sentencia dentro del while
        codigo.agregarLinea("j" + loopLabel);
        codigo.agregarLinea(doneLabel + ":");
    }

    public void generarCodigo(NodoIf nf) {

        String falseLabel = "falseI" + genLabel(nf);
        String doneLabel = "doneI" + genLabel(nf);

        nf.getCondicion().accept(this); //Genera el codigo para la condición

        codigo.agregarLinea("bne $ao, 1, " + falseLabel + "# Si no se cumple la condición salta a la labelFalse");
        nf.getSentenciaIf().accept(this); //Genera el codigo para la sentencia dentro del if


        codigo.agregarLinea("j" + doneLabel + " #Salta al doneLabel");

        codigo.agregarLinea(falseLabel + ": Escribe la labelFalse" );
        if (nf.getSentenciaElse() != null) { //Si no tiene else lo deja vacio
            nf.getSentenciaElse().accept(this);  //Genera el codigo para la sentencia dentro del else
        }

        codigo.agregarLinea(doneLabel + ": #Escribe la labelDone");
    }

    public void generarCodigo(NodoExpBin nodoExpBin){
        nodoExpBin.getLadoIzquierdo().accept(this);
        codigo.agregarLinea("sw $a0, 0($sp)"); //Guarda el valor del lado izquierdo en la pila
        codigo.agregarLinea("addi $sp, $sp, -4");
        nodoExpBin.getLadoDerecho().accept(this);
        codigo.agregarLinea("lw $t1, 4($sp)");

        switch (nodoExpBin.getOperador()){
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

    public void generarCodigoPlus(NodoExpBin nodoExpBin){

        if (Objects.equals(nodoExpBin.getLadoIzquierdo().getTipo(), "String") || Objects.equals(nodoExpBin.getLadoDerecho().getTipo(), "String")) {

            //Hacer la llamada a metodo concat
            codigo.agregarLinea("jal concat");

        } else {

            if(Objects.equals(nodoExpBin.getLadoIzquierdo().getTipo(), "Double") || Objects.equals(nodoExpBin.getLadoDerecho().getTipo(), "Double")){

                codigo.agregarLinea("lwc1 $f1, 0($sp)");
                codigo.agregarLinea("add.d $f0, $f0, $f1");
            }


        }

        nodoExpBin.getLadoIzquierdo().accept(this);
        codigo.agregarLinea("addi $sp, $sp, -4");
        nodoExpBin.getLadoDerecho().accept(this);
        codigo.agregarLinea("lw $t1, 4($sp)");
        codigo.agregarLinea("add $a0, $t1, $a0");
    }


//    public void generarCodigo(NodoLlamadaMetodo nodoLlamadaMetodo){
//
//        //buscar la entrada clase en la tabla de símbolos
//        EntradaClase entradaClase =  st.buscarClase(nodoLlamadaMetodo.getClase());
//
//        //buscar la entranda metodo en la clase
//        EntradaMetodo entradaMetodo = entradaClase.buscarMetodo(nodoLlamadaMetodo.getLexema());
//
//        String retorno = entradaMetodo.getTipoRetorno();
//
//
//
//
//        // Generar código para los argumentos y guardarlos en la pila
//        for (NodoSentencia argumento : ) {
//            argumento.accept();
//            codigo.agregarLinea("addi $sp, $sp, -4");
//            codigo.agregarLinea("sw $a0, 0($sp)"); // Guardar el argumento en la pila
//        }
//
//        // Cargar la dirección del método desde la vtable
//        codigo.agregarLinea("lw $t0, 0($a0)  # Cargar la vtable del objeto");
//        codigo.agregarLinea("lw $t1, " + (nodoLlamadaMetodo.getOffset() * 4) + "($t0)  # Cargar la dirección del método");
//
//        // Llamar al método
//        codigo.agregarLinea("jalr $t1");
//
//        // Limpiar la pila después de la llamada
//        int numArgumentos = nodoLlamadaMetodo.getArgumentos().size();
//        if (numArgumentos > 0) {
//            codigo.agregarLinea("addi $sp, $sp, " + (numArgumentos * 4));
//        }
//
//    }



    public void generarCodigo(NodoDouble nodoDouble){


        codigo.agregarLinea("li $v0, 9  # Solicitar espacio en memoria");
        codigo.agregarLinea("li $a0, 12  # 8 bytes y su vtable");
        codigo.agregarLinea("syscall ");

        codigo.agregarLinea("la $t0, VTABLE_Double # Cargar la dirección de la vtable de Int en un temporal");
        codigo.agregarLinea("sw $t0, 0($v0) #guardamos la dirección de la vtableDouble en la CIR");
        codigo.agregarLinea("li $t0, " + nodoDouble.getValor() + " # Guardamos el valor en la CIR en un temporal");
        codigo.agregarLinea("sw $t0, 4($v0) #Guardamos el valor en la CIR");


        codigo.agregarLinea("move $a0, $v0 # La dirección del objeto Int queda en $a0");

    }


    // Double a;

    // a = new Circulo();


    // $ao = dirección de la CIR
    // 4($sp) = dirección de la variable a


    public void generarCodigo(NodoAsignacion nodoAsignacion) {

        // LADO IZQUIERDO: dirección de la variable → $a0
        nodoAsignacion.getIzquierda().accept(this);

        // push dirección
        codigo.agregarLinea("sw $a0, 0($sp)");
        codigo.agregarLinea("addi $sp, $sp, -4");

        // LADO DERECHO: dirección (CIR del objeto) → $a0
        nodoAsignacion.getDerecha().accept(this);

        // pop dirección
        codigo.agregarLinea("lw $t0, 4($sp)");
        codigo.agregarLinea("addi $sp, $sp, 4");

        // *asignación real*
        codigo.agregarLinea("sw $a0, 0($t0)");
    }



    public void generarCodigo(NodoInt nodoInt){


        codigo.agregarLinea("li $v0, 9  # Solicitar espacio en memoria");
        codigo.agregarLinea("li $a0, 8  # 4 bytes y su vtable");
        codigo.agregarLinea("syscall ");

        codigo.agregarLinea("la $t0, VTABLE_Int # Cargar la dirección de la vtable de Int en un temporal");
        codigo.agregarLinea("sw $t0, 0($v0) #guardamos la dirección de la vtableInt en la CIR");
        codigo.agregarLinea("li $t0, " + nodoInt.getValor() + " # Guardamos el valor en la CIR en un temporal");
        codigo.agregarLinea("sw $t0, 4($v0) #Guardamos el valor en la CIR");


        codigo.agregarLinea("move $a0, $v0 # La dirección del objeto Int queda en $a0");

    }

    public void generarCodigo(NodoBool nodoBool){


        codigo.agregarLinea("li $v0, 9  # Solicitar espacio en memoria");
        codigo.agregarLinea("li $a0, 8  # 4 bytes y su vtable");
        codigo.agregarLinea("syscall ");

        codigo.agregarLinea("la $t0, VTABLE_Bool # Cargar la dirección de la vtable de Int en un temporal");
        codigo.agregarLinea("sw $t0, 0($v0) #guardamos la dirección de la vtableBool en la CIR");
        codigo.agregarLinea("li $t0, " + (nodoBool.getValor() ? 1 : 0) + " # Guardamos el valor en la CIR en un temporal");
        codigo.agregarLinea("sw $t0, 4($v0) #Guardamos el valor en la CIR");


        codigo.agregarLinea("move $a0, $v0 # La dirección del objeto Int queda en $a0");

    }

    public void generarCodigo(NodoString nodoString) {

        int longitudString = nodoString.getValor().length() + 1;

        int longitudCIR = longitudString + (4 - (longitudString % 4)) + 4;


        codigo.agregarLinea("li $v0, 9  # Solicitar espacio en memoria");
        codigo.agregarLinea("li $a0, " + longitudCIR + "  #  len() bytes + padding + su vtable");
        codigo.agregarLinea("syscall ");

        codigo.agregarLinea("la $t0, VTABLE_String # Cargar la dirección de la vtable de String en un temporal");
        codigo.agregarLinea("sw $t0, 0($v0) #guardamos la dirección de la vtableString en la CIR");
        codigo.agregarLinea("li $t0, " + nodoString.getValor() + " # Guardamos el valor en la CIR en un temporal");
        codigo.agregarLinea("sw $t0, 4($v0) #Guardamos el valor en la CIR");


        codigo.agregarLinea("move $a0, $v0 # La dirección del objeto Int queda en $a0");

    }


    public void generarCodigo(NodoLlamadaMetodo nodoLlamadaMetodo){
        // Nodo llamada metodo para cuando no hay encadenado previo


        EntradaClase entradaClase = st.buscarClase(nodoLlamadaMetodo.getClase());


        codigo.agregarLinea("sw $fp 0($sp) # Guardar el frame pointer actual en la pila");
        codigo.agregarLinea("addiu $sp $sp -4 # movemos el puntero de la pila");
        // Generar código para los argumentos y guardarlos en la pila

        LinkedList<NodoExp> parametros = nodoLlamadaMetodo.getParametros();

        for (int i = parametros.size()-1 ; i==0; i-=1) {
            parametros.get(i).accept(this); //Nos da la dirección de la CIR del argumento
            codigo.agregarLinea("sw $a0 0($sp) # Guardar el argumento en la pila");
            codigo.agregarLinea("addiu $sp $sp -4 # movemos el puntero de la pila");
        }

        if (nodoLlamadaMetodo.getEsEstatico()){
            //Es estatico
            //No tiene encadenado previo
            codigo.agregarLinea("li, $t0, 0 #Guardamos un valor nulo en el temporal");
            codigo.agregarLinea("sw $t0, 0($sp) #Como es estatico se guarda un self nulo en el registro de activación para no romper la estructura");
            codigo.agregarLinea("addiu $sp $sp -4 #movemos el puntero de la pila");


            codigo.agregarLinea("ld $t0, VTABLE_" + nodoLlamadaMetodo.getClase() + " # Cargar la dirección de la vtable de la clase " + nodoLlamadaMetodo.getClase());

        }else{
            //Si no es estatico y no tiene un encadenado previo es un metodo que se llama desde self
            codigo.agregarLinea("lw $t1, 4($fp) # Cargar la dirección del objeto (this) desde el frame pointer antrior");
            codigo.agregarLinea("sw $t1, 0($sp) # Guardar el objeto (this) en la pila");
            codigo.agregarLinea("addiu $sp $sp -4 # movemos el puntero de la pila");

            codigo.agregarLinea("lw $t0, 0($t1) # Cargar la vtable del objeto");

        }

        int offSet = entradaClase.getMetodo(nodoLlamadaMetodo.getLexema()).getPosicionMetodo(); //Obtemenos el offset del metodo
        codigo.agregarLinea("addi $t0, $t0, " + (offSet * 4) + " # Calcular la dirección del método en la vtable");
        codigo.agregarLinea("jalr $t0 # Llamar al método " + nodoLlamadaMetodo.getLexema());

        codigo.agregarLinea("addi $sp $sp 4 # movemos el puntero de la pila para sacar el self");

        for (int i = parametros.size()-1 ; i==0; i-=1) {
            codigo.agregarLinea("addiu $sp $sp 4 # movemos el puntero de la pila");
        }

        codigo.agregarLinea("lw $fp 0($sp) # Restauramos el frame pointer");
        codigo.agregarLinea("addiu $sp $sp 4 # sacamos el frame pointer de la pila");

    }



    public void generarCodigo(NodoLlamadaMetodo nodoLlamadaMetodo, NodoExp nodoExp){
        // Nodo llamada metodo para cuando hay encadenado previo


        EntradaClase entradaClase = st.buscarClase(nodoLlamadaMetodo.getClase());


        codigo.agregarLinea("sw $fp 0($sp) # Guardar el frame pointer actual en la pila");
        codigo.agregarLinea("addiu $sp $sp -4 # movemos el puntero de la pila");
        // Generar código para los argumentos y guardarlos en la pila

        LinkedList<NodoExp> parametros = nodoLlamadaMetodo.getParametros();

        for (int i = parametros.size()-1 ; i==0; i-=1) {
            parametros.get(i).accept(this); //Nos da la dirección de la CIR del argumento
            codigo.agregarLinea("sw $a0 0($sp) # Guardar el argumento en la pila");
            codigo.agregarLinea("addiu $sp $sp -4 # movemos el puntero de la pila");
        }

        //hacemos el codigo para el encadenado previo
        nodoExp.accept(this);

        // Como tiene encadenado previo tenemos que buscar la dirección del objeto
        codigo.agregarLinea("sw $a0, 0($sp) # Guardar el argumento en la pila #movemos el objeto a la pila");
        codigo.agregarLinea("addiu $sp $sp -4 # movemos el puntero de la pila");

        codigo.agregarLinea("lw $t0, 0($a0) # Cargar la vtable del objeto");


        int offSet = entradaClase.getMetodo(nodoLlamadaMetodo.getLexema()).getPosicionMetodo(); //Obtemenos el offset del metodo
        codigo.agregarLinea("addi $t0, $t0, " + (offSet * 4) + " # Calcular la dirección del método en la vtable");
        codigo.agregarLinea("jalr $t0 # Llamar al método " + nodoLlamadaMetodo.getLexema());

        codigo.agregarLinea("addi $sp $sp 4 # movemos el puntero de la pila para sacar el self");

        for (int i = parametros.size()-1 ; i==0; i-=1) {
            codigo.agregarLinea("addiu $sp $sp 4 # movemos el puntero de la pila");
        }

        codigo.agregarLinea("lw $fp 0($sp) # Restauramos el frame pointer");
        codigo.agregarLinea("addiu $sp $sp 4 # sacamos el frame pointer de la pila");

    }






//    public void generarCodigo(NodoArray na){
//        na.
//    }



    public void generarCodigo(NodoSentencia ns){

    }

    public String genLabel(NodoSentencia ns){

        return "L" + ns.posicion.getLinea() + "_" + ns.posicion.getColumna();
    }
}