package generacionDeCodigo;

import analizadorSemantico.*;
import ast.*;

import java.util.LinkedList;
import java.util.Objects;

public class MethodBodyVisitor extends NodeVisitor {

    public MethodBodyVisitor(SymbolTable st, AST ast) {
        this.st = st;
        this.ast = ast;
    }

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
                break;
            case DIV:
                codigo.agregarLinea("beq $a0, $zero, ArrayIndexOutOfBoundsException"); // Manejo de división por cero
                codigo.agregarLinea("div $t1, $a0");
                codigo.agregarLinea("mflo $a0");
                break;
            case PERCENTAGE: // mod
                codigo.agregarLinea("div $t1, $a0");
                codigo.agregarLinea("mfhi $a0");
                break;
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
        // Si hay un encadenado previo -> $a0
        // Self = encadenado previo

        EntradaClase entradaClase = st.buscarClase(nodoLlamadaMetodo.getClaseEncadenadoPrev());

        codigo.agregarLinea("sw $fp 0($sp) # Guardar el frame pointer anterior en la pila");
        codigo.agregarLinea("addiu $sp $sp -4 # movemos el puntero de la pila");
        // Generar código para los argumentos y guardarlos en la pila

        LinkedList<NodoExp> parametros = nodoLlamadaMetodo.getParametros();

        int offSetTotalParametros = parametros.size()* (4); //Se calcula cuanto espacio van a ocupar los argumentos

        codigo.agregarLinea("addi $sp, $sp, " + (-offSetTotalParametros) + " # guardamos en la pila el espacio para todos los argumentos");

        String encadenadoPrevio = nodoLlamadaMetodo.getClaseEncadenadoPrev();
        String lexemaMetodo = nodoLlamadaMetodo.getLexema();

        if (!lexemaMetodo.equals(encadenadoPrevio)) {
            if (nodoLlamadaMetodo.getEsEncadenado()) {
                //Si tiene un objeto como encadenado previo se va a encontrar en -> $a0, hay que guardarlo en la pila
                codigo.agregarLinea("sw $a0, 0($sp) # Guardar el encadenado previo en la pila");
                codigo.agregarLinea("addiu $sp $sp -4 # movemos el puntero de la pila");
            } else {
                //Si no tiene un encadenado previo, o no es un constructor, se asume que es un metodo llamado desde self
                codigo.agregarLinea("lw $a0 4($fp) # Cargar el objeto (this) desde el frame pointer antrior");
                codigo.agregarLinea("sw $a0, 0($sp) # Guardar el objeto de la llamada en la pila");
                codigo.agregarLinea("addiu $sp $sp -4 # movemos el puntero de la pila");
            }
        } else {
            // Si es un constructor, se crea un nuevo CIR para el objeto
            codigo.agregarLinea("li $v0 9");
            codigo.agregarLinea("li $a0 " + entradaClase.getTamanioObjeto() + " # Tamaño del objeto");
            codigo.agregarLinea("syscall");

            codigo.agregarLinea("sw $v0, 0($sp) # Guardar la dirección del nuevo objeto en la pila");
            codigo.agregarLinea("addiu $sp $sp -4 # movemos el puntero de la pila");

            codigo.agregarLinea("lw $t0, VTABLE_" + entradaClase.getLexema() + " # Cargar la dirección de la vtable de la clase " + entradaClase.getLexema());
            codigo.agregarLinea("sw $t0, 0($v0) # Guardar la vtable en la CIR del nuevo objeto");

            for (EntradaAtributo atributo : entradaClase.getAtributos().values()) {

                atributo.accept(new TopVisitor(this.st, this.ast));

                // Recuperar la direccion de la clase
                codigo.agregarLinea("lw $t0, 4($sp) # Recuperar la dirección del nuevo objeto desde la pila");

                int offsetAtributo = (atributo.getPosicionAtributo() * 4) + 4; // Offset del atributo en la CIR
                codigo.agregarLinea("sw $a0, " + offsetAtributo + "($t0) # Inicializar el atributo " + atributo.getLexema());
            }

        }

        int offSetParametro = 4;
        for (int i = parametros.size()-1 ; i==0; i-=1) {
            parametros.get(i).accept(this); //Nos da la dirección de la CIR del argumento
            offSetParametro = offSetParametro + 4; //Calculamos el offset parametro
            codigo.agregarLinea("sw $a0 "+ offSetParametro+"($sp) # Guardar el argumento en la pila");
        }

// Ya se guardo el self en la pila, no seria necesario este codigo
//        if (nodoLlamadaMetodo.getEsEstatico()){
//            //Es estatico
//            //No tiene encadenado previo
//            codigo.agregarLinea("li, $t0, 0 #Guardamos un valor nulo en el temporal");
//            codigo.agregarLinea("sw $t0, 0($sp) #Como es estatico se guarda un self nulo en el registro de activación para no romper la estructura");
//            codigo.agregarLinea("addiu $sp $sp -4 #movemos el puntero de la pila");
//
//
//            codigo.agregarLinea("ld $t0, VTABLE_" + nodoLlamadaMetodo.getClase() + " # Cargar la dirección de la vtable de la clase " + nodoLlamadaMetodo.getClase());
//
//        }else{
//            //Si no es estatico y no tiene un encadenado previo es un metodo que se llama desde self
//            codigo.agregarLinea("lw $t1, 4($fp) # Cargar la dirección del objeto (this) desde el frame pointer antrior");
//            codigo.agregarLinea("sw $t1, 0($sp) # Guardar el objeto (this) en la pila");
//            codigo.agregarLinea("addiu $sp $sp -4 # movemos el puntero de la pila");
//
//            codigo.agregarLinea("lw $t0, 0($t1) # Cargar la vtable del objeto");
//
//        }

        int offSetMetodo = entradaClase.getMetodo(nodoLlamadaMetodo.getLexema()).getPosicionMetodo(); //Obtemenos el offset del metodo
        codigo.agregarLinea("addi $t0, $t0, " + (offSetMetodo * 4) + " # Calcular la dirección del método en la vtable");
        codigo.agregarLinea("jalr $t0 # Llamar al método " + nodoLlamadaMetodo.getLexema());

        codigo.agregarLinea("addi $sp $sp 4 # movemos el puntero de la pila para sacar el self");

        codigo.agregarLinea("addiu $sp $sp " + (offSetTotalParametros) + " #Sacamos el espacio para todos los parametros");

        codigo.agregarLinea("lw $fp 0($sp) # Restauramos el frame pointer");
        codigo.agregarLinea("addiu $sp $sp 4 # sacamos el frame pointer de la pila");

    }

    // IO.a
    // a.getAtributo().setRadioAtributo()
    // a.b.getRadio()
//No vamos a manejar asi las llamadas a metodo con encadenado previo
//
//    public void generarCodigo(NodoLlamadaMetodo nodoLlamadaMetodo, NodoExp nodoExp){
//        // Nodo llamada metodo para cuando hay encadenado previo
//
//
//        EntradaClase entradaClase = st.buscarClase(nodoLlamadaMetodo.getClase());
//
//
//        codigo.agregarLinea("sw $fp 0($sp) # Guardar el frame pointer actual en la pila");
//        codigo.agregarLinea("addiu $sp $sp -4 # movemos el puntero de la pila");
//        // Generar código para los argumentos y guardarlos en la pila
//
//        LinkedList<NodoExp> parametros = nodoLlamadaMetodo.getParametros();
//
//        for (int i = parametros.size()-1 ; i==0; i-=1) {
//            parametros.get(i).accept(this); //Nos da la dirección de la CIR del argumento
//            codigo.agregarLinea("sw $a0 0($sp) # Guardar el argumento en la pila");
//            codigo.agregarLinea("addiu $sp $sp -4 # movemos el puntero de la pila");
//        }
//
//        //hacemos el codigo para el encadenado previo
//        nodoExp.accept(this);
//
//        // Como tiene encadenado previo tenemos que buscar la dirección del objeto
//        codigo.agregarLinea("sw $a0, 0($sp) # Guardar el argumento en la pila #movemos el objeto a la pila");
//        codigo.agregarLinea("addiu $sp $sp -4 # movemos el puntero de la pila");
//
//        codigo.agregarLinea("lw $t0, 0($a0) # Cargar la vtable del objeto");
//
//
//        int offSet = entradaClase.getMetodo(nodoLlamadaMetodo.getLexema()).getPosicionMetodo(); //Obtemenos el offset del metodo
//        codigo.agregarLinea("addi $t0, $t0, " + (offSet * 4) + " # Calcular la dirección del método en la vtable");
//        codigo.agregarLinea("jalr $t0 # Llamar al método " + nodoLlamadaMetodo.getLexema());
//
//        codigo.agregarLinea("addi $sp $sp 4 # movemos el puntero de la pila para sacar el self");
//
//        for (int i = parametros.size()-1 ; i==0; i-=1) {
//            codigo.agregarLinea("addiu $sp $sp 4 # movemos el puntero de la pila");
//        }
//
//        codigo.agregarLinea("lw $fp 0($sp) # Restauramos el frame pointer");
//        codigo.agregarLinea("addiu $sp $sp 4 # sacamos el frame pointer de la pila");
//
//    }



    public void generarCodigo(NodoVar nodoVar){


        EntradaMetodo entradaMetodo = st.getMetodoActual(); //Obtenemos el metodo actual
        EntradaClase claseReferenciada; //inicializamos una clase
        int offset;


        if (nodoVar.getEsEncadenado()){
            //El caso de que el objeto sea el resultado de un encadenado previo
            //El encadenado previo ya dejo la dirección de la CIR en $a0

            claseReferenciada = st.buscarClase(nodoVar.getClaseEncadenadoPrev()); //Buscamos la clase a la cual pertenece el objeto como atributo
            EntradaAtributo atributo = claseReferenciada.buscarAtributo(nodoVar.getLexema()); //Buscamos el atributo en la clase referenciadas
            offset = (atributo.getPosicionAtributo() * (-4)) - 4; //Calculamos ell offset dentro de la CIR del objeto del encadenado previo

            codigo.agregarLinea("lw $a0 ," + offset + "($a0) #Buscamos el atributo en la CIR del encadenado previo");


        }else {
            //El caso de que el objeto sea accedido directamente (sin encadenado previo)
            EntradaVariable variable = entradaMetodo.buscarVariableLocal(nodoVar.getLexema());
            if (variable != null) {
                //El caso de que el objeto sea una variable
                offset = (variable.getPosicionVariable() * (-4)) - 4; //Buscamos la posición de la variable pero el offset apunta primero al enlace dinamico
                codigo.agregarLinea("lw $a0 ," + offset + "($fp) #Buscamos la variable en la pila");
            } else {
                //El caso de que el objeto sea un parametro
                EntradaParametro parametro = entradaMetodo.buscarParametro(nodoVar.getLexema());
                if (parametro != null) {
                    offset = (parametro.getPosicionParametro() * (4)) + 4; //Buscamos la posición del parametro pero el offset apunta primero al enlace dinamico y arriba esta el self
                    codigo.agregarLinea("lw $a0 ," + offset + "($fp) #Buscamos el parametro en la pila");
                } else {
                    //El caso de que el objeto sea una variable de instancia
                    EntradaClase clase = st.getClassActual();
                    EntradaAtributo atributo = clase.buscarAtributo(nodoVar.getLexema());

                    codigo.agregarLinea("lw $t0  4($fp) #Buscamos el objeto self en la pila");

                    offset = (atributo.getPosicionAtributo() * (-4)) - 4; //Buscamos el atributo del objeto pero el primer elemento de la cir es la vtable
                    codigo.agregarLinea("lw $a0 ," + offset + "($t0) #Buscamos el atributo en la CIR");

                }
            }
        }

    }

    public void generarCodigoAccesoVariable(NodoVar nodoVar){

        // No devolvemos la direccion de la cir del objeto, devolvemos la direccion del espacio a memoria que apunta a la cir del objeto

        EntradaMetodo entradaMetodo = st.getMetodoActual(); //Obtenemos el metodo actual
        EntradaClase claseReferenciada; //inicializamos una clase
        int offset;


        if (nodoVar.getEsEncadenado()){
            //El caso de que el objeto sea el resultado de un encadenado previo
            //El encadenado previo ya dejo la dirección de la CIR en $a0

            claseReferenciada = st.buscarClase(nodoVar.getClaseEncadenadoPrev()); //Buscamos la clase a la cual pertenece el objeto como atributo
            EntradaAtributo atributo = claseReferenciada.buscarAtributo(nodoVar.getLexema()); //Buscamos el atributo en la clase referenciadas
            offset = (atributo.getPosicionAtributo() * (-4)) - 4; //Calculamos ell offset dentro de la CIR del objeto del encadenado previo

            //codigo.agregarLinea("lw $a0 ," + offset + "($a0) #Buscamos el atributo en la CIR del encadenado previo");
            codigo.agregarLinea("addiu $a0 $a0 , " + offset + " #Devolvemos la direccion del atributo en la CIR del encadenado previo");



        }else {
            //El caso de que el objeto sea accedido directamente (sin encadenado previo)
            EntradaVariable variable = entradaMetodo.buscarVariableLocal(nodoVar.getLexema());
            if (variable != null) {
                //El caso de que el objeto sea una variable
                offset = (variable.getPosicionVariable() * (-4)) - 4; //Buscamos la posición de la variable pero el offset apunta primero al enlace dinamico

                //codigo.agregarLinea("lw $a0 ," + offset + "($fp) #Buscamos la variable en la pila");

                codigo.agregarLinea("addiu $a0 $fp , " + offset + " #Devolvemos la direccion de la variable en la pila");
            } else {
                //El caso de que el objeto sea un parametro
                EntradaParametro parametro = entradaMetodo.buscarParametro(nodoVar.getLexema());
                if (parametro != null) {
                    offset = (parametro.getPosicionParametro() * (4)) + 4; //Buscamos la posición del parametro pero el offset apunta primero al enlace dinamico y arriba esta el self

                    //codigo.agregarLinea("lw $a0 ," + offset + "($fp) #Buscamos el parametro en la pila");
                    codigo.agregarLinea("addiu $a0 $fp , " + offset + " #Devolvemos la direccion del parametro en la pila");
                } else {
                    //El caso de que el objeto sea una variable de instancia
                    EntradaClase clase = st.getClassActual();
                    EntradaAtributo atributo = clase.buscarAtributo(nodoVar.getLexema());

                    codigo.agregarLinea("lw $t0  4($fp) #Buscamos el objeto self en la pila");

                    offset = (atributo.getPosicionAtributo() * (-4)) - 4; //Buscamos el atributo del objeto pero el primer elemento de la cir es la vtable
                    //codigo.agregarLinea("lw $a0 ," + offset + "($t0) #Buscamos el atributo en la CIR");
                    codigo.agregarLinea("addiu $a0 $t0 , " + offset + " #Devolvemos la direccion del atributo en la CIR");
                }
            }
        }

    }

    public void generarCodigo(NodoExp nodoExp){
        nodoExp.accept(this);
    }

    public void generarCodigo(NodoExpUn nodoExpUn) {

        nodoExpUn.getLadoDerecho().accept(this);

        boolean doubleOperacion = nodoExpUn.getLadoDerecho().getTipo().equals("Double");

        switch (nodoExpUn.getOperador()) {
            case MINUS:
                if (doubleOperacion) {

                    codigo.agregarLinea("lwc1 $f0, 4($a0) #cargar la primera mitad del valor del double");
                    codigo.agregarLinea("lwc1 $f1, 8($a0) #cargar la segunda mitad del valor del double");
                    codigo.agregarLinea("ldc1 $f2, db_one #cargar el valor de 1.0");
                    codigo.agregarLinea("ldc1 $f4, db_one #cargar el valor de 1.0");
                    codigo.agregarLinea("sub.d $f2, $f2, $f4 #1.0 - 1.0 = 0.0 ");
                    codigo.agregarLinea("sub.d $f2, $f2, $f4 #0.0 - 1.0 = -1.0 ");
                    codigo.agregarLinea("mul.d $f0, $f0, $f2 #multiplicar por -1");
                    codigo.agregarLinea("swc1 $f0, 4($a0) #guardar el valor del double");
                    codigo.agregarLinea("swc1 $f1, 8($a0) #cargar la segunda mitad del valor del double");
                    break;

                }else{

                    codigo.agregarLinea("li $t0, -1");
                    codigo.agregarLinea("lw $t1, 4($a0) #cargar el valor del int");
                    codigo.agregarLinea("mul $t1, $t1, $t0 #multiplicar por -1");
                    codigo.agregarLinea("sw $t1, 4($a0) #guardar el valor del int");

                 }

            case NOT:
                codigo.agregarLinea("lw $t0, 4($a0) #cargar el valor del bool");

                codigo.agregarLinea("li $t1, 1");
                codigo.agregarLinea("xor $t0, $t1 #invertir el valor del bool");
                codigo.agregarLinea("sw $t0, 4($a0) #guardar el valor del bool");

            case PLUS_PLUS:
                if (doubleOperacion) {

                    codigo.agregarLinea("lwc1 $f0, 4($a0) #cargar el valor del double");
                    codigo.agregarLinea("lwc1 $f1, 8($a0) #cargar el valor del double en la pila");
                    codigo.agregarLinea("ldc1 $f2, db_one");
                    codigo.agregarLinea("add.d $f0, $f0, $f1 #decrementar el valor del double");
                    codigo.agregarLinea("swc1 $f0, 4($a0) #guardar el valor del double");
                    codigo.agregarLinea("swc1 $f1, 8($a0) #cargar la segunda mitad del valor del double");

                }else {

                    codigo.agregarLinea("lw $t0, 4($a0) #cargar el valor del int");
                    codigo.agregarLinea("addi $t0, $t0, 1 #incrementar el valor del int");
                    codigo.agregarLinea("sw $t0, 4($a0) #guardar el valor del int");

                }
            case MINUS_MINUS:

                if(doubleOperacion){

                    codigo.agregarLinea("lwc1 $f0, 4($a0) #cargar el valor del double");
                    codigo.agregarLinea("lwc1 $f1, 8($a0) #cargar el valor del double en la pila");
                    codigo.agregarLinea("ldc1 $f2, db_one");
                    codigo.agregarLinea("sub.d $f0, $f0, $f1 #decrementar el valor del double");
                    codigo.agregarLinea("swc1 $f0, 4($a0) #guardar el valor del double");
                    codigo.agregarLinea("swc1 $f1, 8($a0) #cargar la segunda mitad del valor del double");

                }else {

                    codigo.agregarLinea("lw $t0, 4($a0) #cargar el valor del int");
                    codigo.agregarLinea("addi $t0, $t0, -1 #decrementar el valor del int");
                    codigo.agregarLinea("sw $t0, 4($a0) #guardar el valor del int");

                }
            case LEFT_PAREN:
                codigo.agregarLinea("lwc1 $fs, 4($a0) #cargar el valor del objeto");
                codigo.agregarLinea("cvt.s.w fd, fs #convertir a single precision");
                codigo.agregarLinea("sw $fd, 4($a0) #guardar el valor del objeto en la CIR");

                codigo.agregarLinea("la $t0, VTABLE_Int # Cargar la dirección de la vtable de Int en un temporal");
                codigo.agregarLinea("sw $t0, 0($a0) #modificamos la dirección de la vtableDouble en la CIR");

        }
    }


    public void generarCodigo(NodoArrayAcceso nodoArrayAcceso){

        codigo.agregarLinea("sw $a0, 0($sp) #guardar el objeto en la pila");
        codigo.agregarLinea("addiu $sp $sp -4 #movemos el puntero de la pila");

        nodoArrayAcceso.getIndice().accept(this);

        codigo.agregarLinea("lw $t0, 4($sp) #Cargar el array");
        codigo.agregarLinea("lw $t1, 4($t0) #Cargar el tamaño del array");

        codigo.agregarLinea("lw $t2, 4($a0) #Cargar el indice del array");

        codigo.agregarLinea("slt $t3, $t1, $t2 #Saber si el indice es mayor al tamaño del array");
        codigo.agregarLinea("beq $t3, $zero, ArrayIndexOutOfBoundsException #Si es mayor salimos del metodo");

        codigo.agregarLinea("mul $t2, $t2, 4 #Convertir el indice del array a bytes");
        codigo.agregarLinea("add $t0, $t0, $t2 #Obtenemos el valor del elemento del array");
        codigo.agregarLinea("lw $a0, 0($t0) #Cargar el valor del elemento del array");
        codigo.agregarLinea("addiu $sp $sp 4 #movemos el puntero de la pila");
    }








    public void generarCodigo(NodoSentencia ns){

    }

    public String genLabel(NodoSentencia ns){

        return "L" + ns.posicion.getLinea() + "_" + ns.posicion.getColumna();
    }
}