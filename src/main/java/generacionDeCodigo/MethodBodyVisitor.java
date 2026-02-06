package generacionDeCodigo;

import analizadorSemantico.*;
import ast.*;

import java.util.LinkedList;
import java.util.Objects;

/**
 * Clase encargada de generar codigo para las sentencias del codigo.
 */
public class MethodBodyVisitor extends NodeVisitor {


    /**
     * Constructor de la clase
     *
     * @param st     Tabla de simbolos
     * @param ast    Arbol semantico abstracto
     * @param codigo Generador de codigo
     *
     *
     **/
    public MethodBodyVisitor(SymbolTable st, AST ast, CodeGen codigo) {
        this.st = st;
        this.ast = ast;
        this.codigo = codigo;
    }


    /**
     * Genera codigo para nodoBloque, va a recorrer cada una de las sentencias de nodo bloque para generar el codigo.
     *
     * @param nodoBloque el nodo bloque, con una LinkedList de nodoSentencia
     */
    public void generarCodigo(NodoBloque nodoBloque) {
        for (NodoSentencia ns : nodoBloque.getSentencias()) {
            ns.accept(this);
        }
    }


    /**
     * Genera codigo para nodoWhile, genera el codigo para el loop
     * @param nodoWhile el nodo while
     */
    public void generarCodigo(NodoWhile nodoWhile) {
        codigo.agregarLinea("#Empieza codigo para While");
        String doneLabel = "doneW" + genLabel(nodoWhile);
        String loopLabel = "loop" + genLabel(nodoWhile);

        codigo.agregarLinea(loopLabel + ": #label del loop");
        nodoWhile.getCondicion().accept(this); //Esto genera el codigo de la expresion que sirve como condicion del while
        codigo.agregarLinea("lw $a0, 4($a0) # Carga el valor de la condicion while"); //Cargamos el valor de la condicion en $a0 (Asumimos que es un Int)
        codigo.agregarLinea("bne $a0, 1, " + doneLabel + " #En caso de que la condición no se cumpla, saltamos al done label."); //Si la condicion es falsa, salta al doneLabel (La condición se guarda en $a0)
        nodoWhile.getSentencia().accept(this); //Genera el codigo de la sentencia dentro del while
        codigo.agregarLinea("j " + loopLabel + " #Volvemos al loop");
        codigo.agregarLinea(doneLabel + ": #termina el loop");
    }

    /**
     * Genera codigo para el nodoIf
     * Generamos el codigo para la Expresion de condicion y $a0 apunta a la CIR del resultado
     * cargamos su valor
     * Si se cumple seguimos a la siguiente linea, en caso contrario saltamos a la labelFalse
     *
     */
    public void generarCodigo(NodoIf nf) {
        codigo.agregarLinea("#Empieza codigo para IF");

        String falseLabel = "falseI" + genLabel(nf);
        String doneLabel = "doneI" + genLabel(nf);

        nf.getCondicion().accept(this); //Genera el codigo para la condición
        codigo.agregarLinea("lw $a0, 4($a0) # Carga el valor de la condicion if"); //Cargamos el valor de la condicion en $a0 (Asumimos que es un Int)
        codigo.agregarLinea("bne $a0, 1, " + falseLabel + " # Si no se cumple la condición salta a la labelFalse");
        nf.getSentenciaIf().accept(this); //Genera el codigo para la sentencia dentro del if


        codigo.agregarLinea("j " + doneLabel + " #Salta al doneLabel, se termina el if");

        codigo.agregarLinea(falseLabel + ": #labelFalse de if");
        if (nf.getSentenciaElse() != null) { //Si no tiene else lo deja vacio
            nf.getSentenciaElse().accept(this);  //Genera el codigo para la sentencia dentro del else
        }

        codigo.agregarLinea(doneLabel + ": #labelDone del if");
    }



    /**
     * Genera codigo para una asignación
     * @param nodoAsignacion nodo que contiene la asignación
     */
    public void generarCodigo(NodoAsignacion nodoAsignacion) {

        // LADO IZQUIERDO: dirección de la variable → $a0
        nodoAsignacion.getIzquierda().acceptLadoIzquerdo(this);

        // push dirección
        codigo.agregarLinea("sw $a0, 0($sp)");
        codigo.agregarLinea("addi $sp, $sp, -4");

        // LADO DERECHO: dirección (CIR del objeto) → $a0
        nodoAsignacion.getDerecha().accept(this);

        // pop dirección
        codigo.agregarLinea("lw $t0, 4($sp)");
        codigo.agregarLinea("addi $sp, $sp, 4");

        if (nodoAsignacion.getIzquierda().getTipo().equals("Double")
                && nodoAsignacion.getDerecha().getTipo().equals("Int")) {
            // Convertir Int a Double antes de la asignación
            codigo.agregarLinea("lw $t1, 4($a0) # Cargar el valor del Int");
            codigo.agregarLinea("mtc1.d $t1, $f12 # Mover el valor entero a un registro de punto flotante");
            codigo.agregarLinea("cvt.d.w $f12, $f12 # Convertir el valor entero a double");

            // Crear un nuevo objeto Double para la asignación
            codigo.agregarLinea("li $v0, 9  # Solicitar espacio en memoria");
            codigo.agregarLinea("li $a0, 12  # 8 bytes y su vtable");
            codigo.agregarLinea("syscall ");

            codigo.agregarLinea("la $t2, VTABLE_Double # Cargar la dirección de la vtable de Int en un temporal");
            codigo.agregarLinea("sw $t2, 0($v0) #guardamos la dirección de la vtableDouble en la CIR");
            codigo.agregarLinea("swc1 $f12, 4($v0) #guardar el valor del double");
            codigo.agregarLinea("swc1 $f13, 8($v0) #cargar la segunda mitad del valor del double");

            codigo.agregarLinea("move $a0, $v0 # La dirección del objeto Double queda en $a0");
        }

        // *asignación real*
        codigo.agregarLinea("sw $a0, 0($t0)");
    }

    /**
     * Genera codigo para un retorno de un metodo.
     * @param nodoRet nodo que contiene el tipo de retorno del metodo
     */
    public void generarCodigo(NodoRet nodoRet) {

        NodoExp expRet = nodoRet.getExp();

        if (expRet != null) {
            expRet.accept(this); // La dirección de la CIR del valor de retorno queda en $a0
        } else {
            codigo.agregarLinea("li $a0, 0 # Valor de retorno nulo");
        }

        codigo.agregarLinea("j " + getLabel(st.getMetodoActual()) + "_end # Salta al epilogo del método");

    }


    /**
     * Genera codigo para una expresion unaria.
     * @param nodoExpUn Nodo que contiene la expresion unaria
     */
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

                    codigo.agregarLinea("li $v0 , 9  # Solicitar espacio en memoria");
                    codigo.agregarLinea("li $a0, 12  # 4 bytes y su vtable");
                    codigo.agregarLinea("syscall ");

                    codigo.agregarLinea("la $t2, VTABLE_Double # Cargar la dirección de la vtable de Double en un temporal");
                    codigo.agregarLinea("sw $t2, 0($v0) #guardamos la dirección de la vtableDouble en la CIR");
                    codigo.agregarLinea("move $a0, $v0 # La dirección del objeto Double queda en $a0");

                    codigo.agregarLinea("swc1 $f0, 4($a0) #guardar el valor del double");
                    codigo.agregarLinea("swc1 $f1, 8($a0) #cargar la segunda mitad del valor del double");

                }else{
                    codigo.agregarLinea("li $t0, -1");
                    codigo.agregarLinea("lw $t1, 4($a0) #cargar el valor del int");
                    codigo.agregarLinea("mul $t1, $t1, $t0 #multiplicar por -1");

                    codigo.agregarLinea("li $v0 , 9  # Solicitar espacio en memoria");
                    codigo.agregarLinea("li $a0, 8  # 4 bytes y su vtable");
                    codigo.agregarLinea("syscall ");

                    codigo.agregarLinea("la $t2, VTABLE_Int # Cargar la dirección de la vtable de Int en un temporal");
                    codigo.agregarLinea("sw $t2, 0($v0) #guardamos la dirección de la vtableInt en la CIR");

                    codigo.agregarLinea("sw $t1, 4($v0) #guardar el valor del int");

                    codigo.agregarLinea("move $a0, $v0 # La dirección del objeto Int queda en $a0");
                }
                break;

            case NOT:
                codigo.agregarLinea("lw $t0, 4($a0) #cargar el valor del bool");

                codigo.agregarLinea("li $t1, 1");
                codigo.agregarLinea("xor $t0, $t0, $t1 #invertir el valor del bool");

                codigo.agregarLinea("li $v0 , 9  # Solicitar espacio en memoria");
                codigo.agregarLinea("li $a0, 8  # 4 bytes y su vtable");
                codigo.agregarLinea("syscall ");

                codigo.agregarLinea("la $t2, VTABLE_Bool # Cargar la dirección de la vtable de Bool en un temporal");
                codigo.agregarLinea("sw $t2, 0($v0) #guardamos la dirección de la vtableInt en la CIR");

                codigo.agregarLinea("sw $t0, 4($v0) #guardar el valor del int");

                codigo.agregarLinea("move $a0, $v0 # La dirección del objeto Int queda en $a0");
                break;

            case PLUS_PLUS:
                if (doubleOperacion) {

                    codigo.agregarLinea("lwc1 $f0, 4($a0) #cargar el valor del double");
                    codigo.agregarLinea("lwc1 $f1, 8($a0) #cargar el valor del double en la pila");
                    codigo.agregarLinea("ldc1 $f2, db_one");
                    codigo.agregarLinea("add.d $f0, $f0, $f2 #decrementar el valor del double");
                    codigo.agregarLinea("swc1 $f0, 4($a0) #guardar el valor del double");
                    codigo.agregarLinea("swc1 $f1, 8($a0) #cargar la segunda mitad del valor del double");

                }else {

                    codigo.agregarLinea("lw $t0, 4($a0) #cargar el valor del int");
                    codigo.agregarLinea("addi $t0, $t0, 1 #incrementar el valor del int");
                    codigo.agregarLinea("sw $t0, 4($a0) #guardar el valor del int");

                }
                break;
            case MINUS_MINUS:

                if(doubleOperacion){

                    codigo.agregarLinea("lwc1 $f0, 4($a0) #cargar el valor del double");
                    codigo.agregarLinea("lwc1 $f1, 8($a0) #cargar el valor del double en la pila");
                    codigo.agregarLinea("ldc1 $f2, db_one");
                    codigo.agregarLinea("sub.d $f0, $f0, $f2 #decrementar el valor del double");
                    codigo.agregarLinea("swc1 $f0, 4($a0) #guardar el valor del double");
                    codigo.agregarLinea("swc1 $f1, 8($a0) #cargar la segunda mitad del valor del double");

                }else {

                    codigo.agregarLinea("lw $t0, 4($a0) #cargar el valor del int");
                    codigo.agregarLinea("addi $t0, $t0, -1 #decrementar el valor del int");
                    codigo.agregarLinea("sw $t0, 4($a0) #guardar el valor del int");

                }
                break;
            case LEFT_PAREN:
                codigo.agregarLinea("lwc1 $f0, 4($a0) #cargar el valor del objeto");
                codigo.agregarLinea("lwc1 $f1, 8($a0) #cargar el valor del objeto");

                codigo.agregarLinea("cvt.s.w $f2, $f0 #convertir a single precision");

                codigo.agregarLinea("swc1 $f2, 4($a0) #guardar el valor del objeto en la CIR");

                codigo.agregarLinea("la $t0, VTABLE_Int # Cargar la dirección de la vtable de Int en un temporal");
                codigo.agregarLinea("sw $t0, 0($a0) #modificamos la dirección de la vtableDouble en la CIR");
                break;

        }

        if(nodoExpUn.getEncadenado()!= null){
            nodoExpUn.getEncadenado().accept(this);
        }
    }


    /**
     * Genera el codigo para una expresion binaria.
     * @param nodoExpBin Nodo que contiene la expresion binaria
     */
    public void generarCodigo(NodoExpBin nodoExpBin) {

        codigo.agregarLinea("#Empieza la expresion binaria");

        String tipoIzq = nodoExpBin.getLadoIzquierdo().getTipo();
        String tipoDer = nodoExpBin.getLadoDerecho().getTipo();

        boolean esDouble = tipoIzq.equals("Double") || tipoDer.equals("Double");
        boolean esString = tipoIzq.equals("Str") || tipoDer.equals("Str");

        codigo.agregarLinea("#Empieza codigo para expBin");

        nodoExpBin.getLadoIzquierdo().accept(this);
        codigo.agregarLinea("sw $a0, 0($sp) #Guarda la CIR de la expresión izq en la pila"); //Guarda el valor del lado izquierdo en la pila
        codigo.agregarLinea("addi $sp, $sp, -4 #movemos el puntero de la pila");
        nodoExpBin.getLadoDerecho().accept(this);
        codigo.agregarLinea("lw $t0, 4($sp) # cargamos la CIR de la exp izquierda en t0");
        codigo.agregarLinea("addi $sp, $sp, 4 # sacamos de la pila la exp izquierda");
        codigo.agregarLinea("move $t1, $a0 #guardamos la direccon de la CIR de exp derecha en t1");

        //Tenemos en $t0 el lado izquierdo
        //Tenemos en $t1 el lado derecho

        switch (nodoExpBin.getOperador()) {
            case PLUS:
                if (esString) {
                    codigo.agregarLinea("#Se concatenan dos String");
                    codigo.agregarLinea("sw $fp 0($sp) # Guardar el valor de $fp en la pila antes de la llamada a concat)");
                    codigo.agregarLinea("addi $sp, $sp, -4 #movemos el puntero de la pila");
                    codigo.agregarLinea("sw $t1 0($sp) #Guardamos el puntero de CIR de la expresión derecha en el la pila como un parametro de concat");
                    codigo.agregarLinea("addi $sp, $sp, -4 #movemos el puntero de la pila");
                    codigo.agregarLinea("sw $t0 0($sp) #Guardamos el puntero de CIR de la expresión izquierda en el la pila como self");
                    codigo.agregarLinea("addi $sp, $sp, -4 #movemos el puntero de la pila");

                    codigo.agregarLinea("jal concat");

                    codigo.agregarLinea("addi $sp, $sp, 8 #movemos el puntero de la pila para sacar el self y el parametro de concat");
                    codigo.agregarLinea("lw $fp, 4($sp) #Restauramos el valor de $fp en la pila");
                    codigo.agregarLinea("addi $sp, $sp, 4 #movemos el puntero de la pila para sacar el framepointer anterior");
                    // El resultado de la llamada a concat se guarda en $a0

                } else {
                    if (esDouble) {

                        //El caso de que alguno de los dos sea double:
                        expBinDouble(tipoIzq, tipoDer);

                        // Una vez que tenemos ambos valores en $f0 y $f1, sumamos los dos
                        codigo.agregarLinea("add.d $f0, $f0, $f2 #sumar los dos doubles");

                        expBinResultadoDouble();
                    } else {
                        //El caso de que ambos sean int:
                        codigo.agregarLinea("lw $t0 4($t0) #Cargar el valor del int izquierdo");
                        codigo.agregarLinea("lw $t1 4($t1) #Cargar el valor del int derecho");

                        codigo.agregarLinea("add $t0, $t0, $t1 #sumar los dos int");

                        codigo.agregarLinea("li $a0, 8  # 4 bytes y su vtable");
                        codigo.agregarLinea("li $v0, 9  # Solicitar espacio en memoria");
                        codigo.agregarLinea("syscall ");

                        codigo.agregarLinea("move $a0 $v0 #La dirección del objeto Int queda en $a0");

                        codigo.agregarLinea("la $t1, VTABLE_Int # Cargar la dirección de la vtable de Int en un temporal");
                        codigo.agregarLinea("sw $t1, 0($a0) #guardamos la dirección de la vtableInt en la CIR");

                        codigo.agregarLinea("sw $t0, 4($a0) #guardar el valor del int");
                    }
                }
                break;
            case MINUS:
                if (esDouble) {
                    //El caso de que alguno de los dos sea double:
                    expBinDouble(tipoIzq, tipoDer);
                    // Una vez que tenemos ambos valores en $f0 y $f1, restamos los dos
                    codigo.agregarLinea("sub.d $f0, $f0, $f2 #restar los dos doubles");

                    expBinResultadoDouble();
                } else {
                    //El caso de que ambos sean int:
                    codigo.agregarLinea("lw $t0 4($t0) #Cargar el valor del int izquierdo");
                    codigo.agregarLinea("lw $t1 4($t1) #Cargar el valor del int derecho");

                    codigo.agregarLinea("sub $t0, $t0, $t1 #restar los dos int");

                    expBinResultadoInt();
                }
                break;

            case MULT:
                if (esDouble) {
                    //El caso de que alguno de los dos sea double:
                    expBinDouble(tipoIzq, tipoDer);
                    // Una vez que tenemos ambos valores en $f0 y $f1, multiplicamos los dos
                    codigo.agregarLinea("mul.d $f0, $f0, $f2 #multiplicamos los dos doubles");

                    expBinResultadoDouble();
                } else {
                    //El caso de que ambos sean int:
                    codigo.agregarLinea("lw $t0 4($t0) #Cargar el valor del int izquierdo");
                    codigo.agregarLinea("lw $t1 4($t1) #Cargar el valor del int derecho");

                    codigo.agregarLinea("mul $t0, $t0, $t1 #multiplicar los dos int");

                    expBinResultadoInt();
                }
                break;

            case DIV:
                // DIV es division entre enteros puramente
                //El caso de que ambos sean int:
                codigo.agregarLinea("lw $t0 4($t0) #Cargar el valor del int izquierdo");
                codigo.agregarLinea("lw $t1 4($t1) #Cargar el valor del int derecho");

                codigo.agregarLinea("beqz $t1, DivisionByZeroException #Si el lado derecho es igual a cero saltamos un error");


                codigo.agregarLinea("div $t0, $t0, $t1 #dividir los dos int");

                expBinResultadoInt();
                break;

            case PERCENTAGE: // mod
                if (esDouble) {
                    //El caso de que alguno de los dos sea double:
                    expBinDouble(tipoIzq, tipoDer);
                    codigo.agregarLinea("ldc1 $f4, db_cero #traemos el double cero para poder compararlo con el lado derecho");
                    codigo.agregarLinea("c.eq.d $f4, $f2 #comparamos si el double es igual a cero");
                    codigo.agregarLinea("bc1t DivisionByZeroException");


                    // Una vez que tenemos ambos valores en $f0 y $f1, multiplicamos los dos
                    codigo.agregarLinea("div.d $f0, $f0, $f2 #dividimos los dos doubles");
                    codigo.agregarLinea("cvt.w.d $f6, $f0 #Truncar el cociente");
                    codigo.agregarLinea("cvt.d.w $f6, $f6 #convertimos el resultado a double");
                    codigo.agregarLinea("mul.d $f6, $f6, $f2 #multiplicamos el resultado por el lado derecho");
                    codigo.agregarLinea("sub.d $f0, $f0, $f6 #restamos el resultado del modulo del lado izquierdo");

                    expBinResultadoDouble();

                } else {
                    //El caso de que ambos sean int:
                    codigo.agregarLinea("lw $t0 4($t0) #Cargar el valor del int izquierdo");
                    codigo.agregarLinea("lw $t1 4($t1) #Cargar el valor del int derecho");

                    codigo.agregarLinea("beqz $t1, DivisionByZeroException #Si el lado derecho es igual a cero saltamos un error");


                    codigo.agregarLinea("div $t0, $t1 #dividimos los dos int");
                    codigo.agregarLinea("mfhi $t0 #guardamos el resultado en $t0 del resto");

                    expBinResultadoInt();
                }
                break;

            case SLASH:
                if (esDouble) {
                    //El caso de que alguno de los dos sea double:
                    expBinDouble(tipoIzq, tipoDer);
                    codigo.agregarLinea("ldc1 $f4, db_cero #traemos el double cero para poder compararlo con el lado derecho");
                    codigo.agregarLinea("c.eq.d $f4, $f2 #comparamos si el double es igual a cero");
                    codigo.agregarLinea("bc1t DivisionByZeroException");


                    // Una vez que tenemos ambos valores en $f0 y $f1, dividimos los dos
                    codigo.agregarLinea("div.d $f0, $f0, $f2 #dividimos los dos doubles");

                    expBinResultadoDouble();
                } else {
                    //El caso de que ambos sean int:
                    codigo.agregarLinea("lw $t0 4($t0) #Cargar el valor del int izquierdo");
                    codigo.agregarLinea("lw $t1 4($t1) #Cargar el valor del int derecho");

                    codigo.agregarLinea("beqz $t1, DivisionByZeroException #Si el lado derecho es igual a cero saltamos un error");


                    codigo.agregarLinea("div $t0, $t0, $t1 #dividimos los dos int");

                    expBinResultadoInt();
                }
                break;

            case EQUAL_EQUAL:
                if (esString) {
                    // Si es string
                    expBinString();
                } else {

                    if (esDouble) {
                        //El caso de que alguno de los dos sea double:
                        expBinDouble(tipoIzq, tipoDer);

                        // Una vez que tenemos ambos valores en $f0 y $f1, multiplicamos los dos
                        codigo.agregarLinea("c.eq.d $f0, $f2 #comparamos si el double es igual al lado derecho");
                        codigo.agregarLinea("li $t1 0 #si son iguales saltamos a la label true");
                        String label = "true_" + nodoExpBin.posicion.getLinea() + "_" + nodoExpBin.posicion.getColumna();
                        codigo.agregarLinea("bc1t " + label + "#si son iguales saltamos a la label true");
                        codigo.agregarLinea("li $t1 1 #si no son iguales cargamos este valor para restar");
                        codigo.agregarLinea(label + ":");
                        codigo.agregarLinea("li $t0, 1 #Si son iguales no se cargo el valor anterior");
                        codigo.agregarLinea("sub $t0, $t0, $t1 #Si son iguales seteamos el valor a 1, sino a 0");


                    } else {
                        if(tipoIzq.equals("Int") || tipoIzq.equals("Bool")) {
                            //El caso de que ambos sean int:
                            codigo.agregarLinea("lw $t0 4($t0) #Cargar el valor del int o bool izquierdo");
                            codigo.agregarLinea("lw $t1 4($t1) #Cargar el valor del int o bool derecho");


                            codigo.agregarLinea("slt $t2, $t0, $t1 #Comparo si izquierda es más grande que derecha");
                            codigo.agregarLinea("slt $t3 , $t1, $t0 #Comparo si derecha es más grande que izquierda");
                            codigo.agregarLinea("or $t0, $t2, $t3 #Si alguna de las dos es verdadera, entonces no son iguales");
                            codigo.agregarLinea("xor $t0, $t0, 1 #Si ambas son iguales, seteamos el valor a 1, sino a 0");
                        }
                        else{
                            // El caso de nil
                            codigo.agregarLinea("li $t2, 0 #cargamos el valor de true");
                            codigo.agregarLinea("beq $t0, $t1, true_" + nodoExpBin.posicion.getLinea() + "_" + nodoExpBin.posicion.getColumna() +" #Si ambos son true, seteamos el valor a 1, sino a 0");
                            codigo.agregarLinea("li $t2, 1 #cargamos el valor de false");
                            String label = "true_" + nodoExpBin.posicion.getLinea() + "_" + nodoExpBin.posicion.getColumna();
                            codigo.agregarLinea(label + ":");
                            codigo.agregarLinea("li $t1, 1");
                            codigo.agregarLinea("sub $t1, $t1, $t2 #Si ambos son false, seteamos el valor a 0, sino a 1");
                        }
                    }
                    expBinResultadoBool();
                }
                break;

            case NOT_EQUAL:
                if (esString) {
                    // Si es string
                    expBinString();

                    codigo.agregarLinea("lw $t0, 4($a0) #Cargar el valor resultado");
                    codigo.agregarLinea("xor $t0, $t0, 1 #negamos el valor");
                    codigo.agregarLinea("sw $t0 4($a0) #Guardar el nuevo valor");

                } else {


                    if (esDouble) {
                        //El caso de que alguno de los dos sea double:
                        expBinDouble(tipoIzq, tipoDer);


                        // Una vez que tenemos ambos valores en $f0 y $f1, multiplicamos los dos

                        codigo.agregarLinea("c.eq.d $f0, $f2 #comparamos si el double es igual al lado derecho");
                        codigo.agregarLinea("li $t1 0 #si son iguales saltamos a la label true");
                        String label = "true_" + nodoExpBin.posicion.getLinea() + "_" + nodoExpBin.posicion.getColumna();
                        codigo.agregarLinea("bc1t " + label + "#si son iguales saltamos a la label true");
                        codigo.agregarLinea("li $t1 1 #si no son iguales cargamos este valor para restar");
                        codigo.agregarLinea(label + ":");
                        codigo.agregarLinea("li $t0, 1 #Si son iguales no se cargo el valor anterior");
                        codigo.agregarLinea("sub $t0, $t0, $t1 #Si son iguales seteamos el valor a 1, sino a 0");

                        codigo.agregarLinea("xor $t0, $t0, 1 #Invertimos el valor anterior para obtener el valor final");


                    } else {
                        if(tipoIzq.equals("Int") || tipoIzq.equals("Bool")) {
                            //El caso de que ambos sean int:
                            codigo.agregarLinea("lw $t0 4($t0) #Cargar el valor del int o bool izquierdo");
                            codigo.agregarLinea("lw $t1 4($t1) #Cargar el valor del int o bool derecho");


                            codigo.agregarLinea("slt $t2, $t0, $t1 #Comparo si izquierda es más grande que derecha");
                            codigo.agregarLinea("slt $t3 , $t1, $t0 #Comparo si derecha es más grande que izquierda");
                            codigo.agregarLinea("or $t0, $t2, $t3 #Si alguna de las dos es verdadera, entonces no son iguales");

                        }else{
                            // El caso de nil
                            codigo.agregarLinea("li $t2, 1 #cargamos el valor de true");
                            codigo.agregarLinea("beq $t0, $t1, true_"+ nodoExpBin.posicion.getLinea() + "_" + nodoExpBin.posicion.getColumna() +" #Si ambos son true, seteamos el valor a 1, sino a 0");
                            codigo.agregarLinea("li $t2, 0 #cargamos el valor de false");
                            String label = "true_" + nodoExpBin.posicion.getLinea() + "_" + nodoExpBin.posicion.getColumna();
                            codigo.agregarLinea(label + ":");
                            codigo.agregarLinea("li $t1, 1");
                            codigo.agregarLinea("sub $t1, $t1, $t2 #Si ambos son false, seteamos el valor a 0, sino a 1");
                        }

                    }
                    expBinResultadoBool();
                }
                break;

            case LESS:

                if (esDouble) {
                    //El caso de que alguno de los dos sea double:
                    expBinDouble(tipoIzq, tipoDer);

                    // Una vez que tenemos ambos valores en $f0 y $f1, multiplicamos los dos

                    codigo.agregarLinea("c.lt.d $f0, $f2 #comparamos si el double de la izquierda es menor al lado derecho");
                    codigo.agregarLinea("li $t1 0 #si es menor saltamos a la label true");
                    String label = "true_" + nodoExpBin.posicion.getLinea() + "_" + nodoExpBin.posicion.getColumna();
                    codigo.agregarLinea("bc1t " + label + "#si son iguales saltamos a la label true");
                    codigo.agregarLinea("li $t1 1 #si no es menor cargamos este valor para restar");
                    codigo.agregarLinea(label + ":");
                    codigo.agregarLinea("li $t0, 1 #Si es menor no se cargo el valor anterior");
                    codigo.agregarLinea("sub $t0, $t0, $t1 #Si es menor seteamos el valor a 1, sino a 0");


                } else {
                    //El caso de que ambos sean int:
                    codigo.agregarLinea("lw $t0 4($t0) #Cargar el valor del int o bool izquierdo");
                    codigo.agregarLinea("lw $t1 4($t1) #Cargar el valor del int o bool derecho");


                    codigo.agregarLinea("slt $t0, $t0, $t1 #Comparo si izquierda es más chico que derecha");

                }
                expBinResultadoBool();
                break;

            case LESS_EQUAL:
                if (esDouble) {
                    //El caso de que alguno de los dos sea double:
                    expBinDouble(tipoIzq, tipoDer);


                    // Una vez que tenemos ambos valores en $f0 y $f1, multiplicamos los dos

                    codigo.agregarLinea("c.le.d $f0, $f2 #comparamos si el double de la izquierda es menor o igual al lado derecho");
                    codigo.agregarLinea("li $t1 0 #si son iguales saltamos a la label true");
                    String label = "true_" + nodoExpBin.posicion.getLinea() + "_" + nodoExpBin.posicion.getColumna();
                    codigo.agregarLinea("bc1t " + label + "#si no es menor o igual saltamos a la label true");
                    codigo.agregarLinea("li $t1 1 #si no son iguales cargamos este valor para restar");
                    codigo.agregarLinea(label + ":");
                    codigo.agregarLinea("li $t0, 1 #Si son iguales no se cargo el valor anterior");
                    codigo.agregarLinea("sub $t0, $t0, $t1 #Si es menor o igual seteamos el valor a 1, sino a 0");

                } else {
                    //El caso de que ambos sean int:
                    codigo.agregarLinea("lw $t0 4($t0) #Cargar el valor del int o bool izquierdo");
                    codigo.agregarLinea("lw $t1 4($t1) #Cargar el valor del int o bool derecho");


                    codigo.agregarLinea("slt $t0, $t1, $t0 #Comparo si izquierda es más grande que derecha");
                    codigo.agregarLinea("xori $t0, $t0, 1 # niego lo anterior para obtener menor o igual");

                }
                expBinResultadoBool();
                break;

            case GREATER:
                if (esDouble) {
                    //El caso de que alguno de los dos sea double:
                    expBinDouble(tipoIzq, tipoDer);

                    // Una vez que tenemos ambos valores en $f0 y $f1, multiplicamos los dos

                    codigo.agregarLinea("c.le.d $f0, $f2 #comparamos si el double de la izquierda es menor o igual al lado derecho");
                    codigo.agregarLinea("li $t1 0 #si son iguales saltamos a la label true");
                    String label = "true_" + nodoExpBin.posicion.getLinea() + "_" + nodoExpBin.posicion.getColumna();
                    codigo.agregarLinea("bc1t " + label + "#si son iguales saltamos a la label true");
                    codigo.agregarLinea("li $t1 1 #si no son iguales cargamos este valor para restar");
                    codigo.agregarLinea(label + ":");
                    codigo.agregarLinea("li $t0, 1 #Si son iguales no se cargo el valor anterior");
                    codigo.agregarLinea("sub $t0, $t0, $t1 #Si son iguales seteamos el valor a 1, sino a 0");

                    codigo.agregarLinea("xor $t0, $t0, 1 #negamos lo anterior y obtenemos mayor");


                } else {
                    //El caso de que ambos sean int:
                    codigo.agregarLinea("lw $t0 4($t0) #Cargar el valor del int o bool izquierdo");
                    codigo.agregarLinea("lw $t1 4($t1) #Cargar el valor del int o bool derecho");


                    codigo.agregarLinea("slt $t0, $t1, $t0 #Comparo si izquierda es más grande que derecha");

                }
                expBinResultadoBool();
                break;

            case GREATER_EQUAL:

                if (esDouble) {
                    //El caso de que alguno de los dos sea double:
                    expBinDouble(tipoIzq, tipoDer);


                    // Una vez que tenemos ambos valores en $f0 y $f1, multiplicamos los dos

                    codigo.agregarLinea("c.lt.d $f0, $f2 #comparamos si el double de la izquierda es menor al lado derecho");
                    codigo.agregarLinea("li $t1 0 #si son iguales saltamos a la label true");
                    String label = "true_" + nodoExpBin.posicion.getLinea() + "_" + nodoExpBin.posicion.getColumna();
                    codigo.agregarLinea("bc1t " + label + "#si son iguales saltamos a la label true");
                    codigo.agregarLinea("li $t1 1 #si no son iguales cargamos este valor para restar");
                    codigo.agregarLinea(label + ":");
                    codigo.agregarLinea("li $t0, 1 #Si son iguales no se cargo el valor anterior");
                    codigo.agregarLinea("sub $t0, $t0, $t1 #Si son iguales seteamos el valor a 1, sino a 0");

                    codigo.agregarLinea("xor $t0, $t0, 1 #negamos lo anterior y obtenemos mayor o igual");


                } else {
                    //El caso de que ambos sean int:
                    codigo.agregarLinea("lw $t0 4($t0) #Cargar el valor del int o bool izquierdo");
                    codigo.agregarLinea("lw $t1 4($t1) #Cargar el valor del int o bool derecho");


                    codigo.agregarLinea("slt $t0, $t0, $t1 #Comparo si izquierda es más chico que derecha");
                    codigo.agregarLinea("xori $t0, $t0, 1 #niego lo anterior para obtener mayor o igual");

                }
                expBinResultadoBool();
                break;
            case AND:

                //El caso de que ambos sean Bool:
                codigo.agregarLinea("lw $t0 4($t0) #Cargar el valor del int o bool izquierdo");
                codigo.agregarLinea("lw $t1 4($t1) #Cargar el valor del int o bool derecho");

                codigo.agregarLinea("and $t0 $t0 $t1 #Hacemos la operacion and entre exp izq y exp der");


                expBinResultadoBool();
                break;

            case OR:

                //El caso de que ambos sean Bool:
                codigo.agregarLinea("lw $t0 4($t0) #Cargar el valor del int o bool izquierdo");
                codigo.agregarLinea("lw $t1 4($t1) #Cargar el valor del int o bool derecho");

                codigo.agregarLinea("or $t0 $t0 $t1 #Hacemos la operacion or entre exp izq y exp der");


                expBinResultadoBool();
                break;
        }

        codigo.agregarLinea("#Termina codigo para expBin");
        if (nodoExpBin.getEncadenado() != null){
            nodoExpBin.getEncadenado().accept(this);
        }

    }

    /**
     * Genera una CIR para Bool y guarda el valor que se encuentra en t0
     */
    private void expBinResultadoBool() {
        codigo.agregarLinea("li $a0, 8  # 4 bytes y su vtable");
        codigo.agregarLinea("li $v0, 9  # Solicitar espacio en memoria");
        codigo.agregarLinea("syscall ");
        codigo.agregarLinea("move $a0 $v0 #La dirección del objeto Bool queda en $a0");
        codigo.agregarLinea("la $t1, VTABLE_Bool # Cargar la dirección de la vtable de Bool en un temporal");
        codigo.agregarLinea("sw $t1, 0($a0) #guardamos la dirección de la vtableBool en la CIR");
        codigo.agregarLinea("sw $t0, 4($a0) #guardar el valor del Bool en la CIR");
    }


    /**
     * Genera una CIR para Int y guardamos el valor que se encuentra en t0
     */
    private void expBinResultadoInt(){
        codigo.agregarLinea("li $a0, 8  # 4 bytes y su vtable");
        codigo.agregarLinea("li $v0, 9  # Solicitar espacio en memoria");
        codigo.agregarLinea("syscall ");

        codigo.agregarLinea("move $a0 $v0 #La dirección del objeto Int queda en $a0");

        codigo.agregarLinea("la $t1, VTABLE_Int # Cargar la dirección de la vtable de Int en un temporal");
        codigo.agregarLinea("sw $t1, 0($a0) #guardamos la dirección de la vtableInt en la CIR");

        codigo.agregarLinea("sw $t0, 4($a0) #guardar el valor del int");
    }


    /**
     * Genera codigo para comprobar si dos string son iguales
     */
    private void expBinString() {

        codigo.agregarLinea("sw $fp 0($sp) # Guardar el valor de $fp en la pila antes de la llamada a eq_string)");
        codigo.agregarLinea("addi $sp, $sp, -4 #movemos el puntero de la pila");
        codigo.agregarLinea("sw $t1 0($sp) #Guardamos el valor de la expresión derecha en el la pila como un parametro de eq_string");
        codigo.agregarLinea("addi $sp, $sp, -4 #movemos el puntero de la pila");
        codigo.agregarLinea("sw $t0 0($sp) #Guardamos el valor de la expresión izquierda en el la pila como self");
        codigo.agregarLinea("addi $sp, $sp, -4 #movemos el puntero de la pila");

        codigo.agregarLinea("jal eq_str #nos devuelve un bool, 1 si los string son iguales");

        codigo.agregarLinea("addi $sp, $sp, 8 #movemos el puntero de la pila para sacar el self y el parametro");
        codigo.agregarLinea("lw $fp, 0($sp) #Restauramos el valor de $fp en la pila");
        codigo.agregarLinea("addi $sp, $sp, 4 #movemos el puntero de la pila para sacar el framepointer anterior");
    }

    /**
     * Genera una CIR para Double y guardamos el valor que se encuentra en f0
     */
    private void expBinResultadoDouble() {

        codigo.agregarLinea("li $a0, 12  # 8 bytes y su vtable");
        codigo.agregarLinea("li $v0, 9  # Solicitar espacio en memoria");
        codigo.agregarLinea("syscall ");

        codigo.agregarLinea("move $a0 $v0 #La dirección del objeto Double queda en $a0");

        codigo.agregarLinea("la $t0, VTABLE_Double # Cargar la dirección de la vtable de Int en un temporal");
        codigo.agregarLinea("sw $t0, 0($a0) #guardamos la dirección de la vtableDouble en la CIR");

        codigo.agregarLinea("swc1 $f0, 4($a0) #guardar el valor del double");
        codigo.agregarLinea("swc1 $f1, 8($a0) #cargar la segunda mitad del valor del double");
    }

    /**
     * Genera codigo para cargar en f0 un double para lado izquierdo y en f2 un doble para lado derecho
     *
     * @param tipoIzq tipo de la expresion de lado izquierdo
     * @param tipoDer tipo de la expresion de lado derecho
     */

    private void expBinDouble(String tipoIzq, String tipoDer) {

        if (tipoIzq.equals("Int")) {
            codigo.agregarLinea("lwc1 $f0, 4($t0) #cargar el valor del int izquierdo");
            codigo.agregarLinea("cvt.d.w $f0, $f0 #convertir el int a double");

            codigo.agregarLinea("lwc1 $f2 4($t1) #guardamos el valor de derecha en $f2");
            codigo.agregarLinea("lwc1 $f3 8($t1) #guardamos el valor de derecha en $f3 para completar el double");

        } else {
            if (tipoDer.equals("Int")) {
                codigo.agregarLinea("lwc1 $f2, 4($t1) #cargar el valor del int derecho");
                codigo.agregarLinea("cvt.d.w $f2, $f2 #convertir el int a double");
            } else {
                codigo.agregarLinea("lwc1 $f2, 4($t1) #cargar el valor del double izquierdo");
                codigo.agregarLinea("lwc1 $f3, 8($t1) #cargar el valor del double derecho");
            }
            codigo.agregarLinea("lwc1 $f0 4($t0) #guardamos el valor de izquierda en $f0");
            codigo.agregarLinea("lwc1 $f1 8($t0) #guardamos el valor de izquierda en $f1 para completar el double");
        }
    }


    /**
     * Genera codigo para un literal double
     * @param nodoDouble nodo que contiene el valor del double
     */
    public void generarCodigo(NodoDouble nodoDouble){


        codigo.agregarLinea("li $v0, 9  # Solicitar espacio en memoria");
        codigo.agregarLinea("li $a0, 12  # 8 bytes y su vtable");
        codigo.agregarLinea("syscall ");

        codigo.agregarLinea("la $t0, VTABLE_Double # Cargar la dirección de la vtable de Int en un temporal");
        codigo.agregarLinea("sw $t0, 0($v0) #guardamos la dirección de la vtableDouble en la CIR");


        String label = nodoDouble.posicion.getLinea() + "_" + nodoDouble.posicion.getColumna();

        codigo.agregarData("double_const_" + label + ": .double " + nodoDouble.getValor());

        codigo.agregarLinea("ldc1 $f0, double_const_" + label + " # Guardamos el valor en la CIR en un temporal");


        codigo.agregarLinea("swc1 $f0, 4($v0) #guardar el valor del double");
        codigo.agregarLinea("swc1 $f1, 8($v0) #cargar la segunda mitad del valor del double");


        codigo.agregarLinea("move $a0, $v0 # La dirección del objeto Int queda en $a0");

        if (nodoDouble.getEncadenado() != null){
            nodoDouble.getEncadenado().accept(this);
        }

    }





    /**
     * Genera codigo para un literal int
     * @param nodoInt nodo que contiene el valor del int
     */
    public void generarCodigo(NodoInt nodoInt){

        codigo.agregarLinea("li $v0, 9  # Solicitar espacio en memoria");
        codigo.agregarLinea("li $a0, 8  # 4 bytes y su vtable");
        codigo.agregarLinea("syscall ");

        codigo.agregarLinea("la $t0, VTABLE_Int # Cargar la dirección de la vtable de Int en un temporal");
        codigo.agregarLinea("sw $t0, 0($v0) #guardamos la dirección de la vtableInt en la CIR");
        codigo.agregarLinea("li $t0, " + nodoInt.getValor() + " # Guardamos el valor en la CIR en un temporal");
        codigo.agregarLinea("sw $t0, 4($v0) #Guardamos el valor en la CIR");


        codigo.agregarLinea("move $a0, $v0 # La dirección del objeto Int queda en $a0");

        if (nodoInt.getEncadenado() != null){
            nodoInt.getEncadenado().accept(this);
        }

    }

    /**
     * Genera codigo para un literal bool
     * @param nodoBool nodo que contiene el valor del bool
     */
    public void generarCodigo(NodoBool nodoBool){


        codigo.agregarLinea("li $v0, 9  # Solicitar espacio en memoria");
        codigo.agregarLinea("li $a0, 8  # 4 bytes y su vtable");
        codigo.agregarLinea("syscall ");

        codigo.agregarLinea("la $t0, VTABLE_Bool # Cargar la dirección de la vtable de Int en un temporal");
        codigo.agregarLinea("sw $t0, 0($v0) #guardamos la dirección de la vtableBool en la CIR");
        codigo.agregarLinea("li $t0, " + (nodoBool.getValor() ? 1 : 0) + " # Guardamos el valor en la CIR en un temporal");
        codigo.agregarLinea("sw $t0, 4($v0) #Guardamos el valor en la CIR");


        codigo.agregarLinea("move $a0, $v0 # La dirección del objeto Int queda en $a0");

        if (nodoBool.getEncadenado() != null){
            nodoBool.getEncadenado().accept(this);
        }

    }

    /**
     * Genera codigo para un literal string
     * @param nodoString nodo que contiene el valor del string
     */
    public void generarCodigo(NodoString nodoString) {

        int longitudString = nodoString.getValor().length() + 1;

        int longitudCIR = longitudString + (4 - (longitudString % 4)) + 4;


        codigo.agregarLinea("li $v0, 9  # Solicitar espacio en memoria");
        codigo.agregarLinea("li $a0, " + longitudCIR + "  #  len() bytes + padding + su vtable");
        codigo.agregarLinea("syscall ");

        codigo.agregarLinea("la $t0, VTABLE_Str # Cargar la dirección de la vtable de String en un temporal");
        codigo.agregarLinea("sw $t0, 0($v0) #guardamos la dirección de la vtableString en la CIR");

        String label = nodoString.posicion.getLinea() + "_" + nodoString.posicion.getColumna();

        codigo.agregarData("str_const_" + label + ": .asciiz \"" + nodoString.getValor() + "\"");

        codigo.agregarLinea("la $a0, str_const_" + label + " # Guardamos el valor en la CIR en un temporal");
        codigo.agregarLinea("jal save_str #Guardamos el valor en la CIR");


        codigo.agregarLinea("move $a0, $v0 # La dirección del objeto Int queda en $a0");

        if (nodoString.getEncadenado() != null){
            nodoString.getEncadenado().accept(this);
        }

    }

    /**
     * Genera codigo para una llamada de metodo
     * @param nodoLlamadaMetodo nodo que contiene la llamada al metodo
     */
    public void generarCodigo(NodoLlamadaMetodo nodoLlamadaMetodo){
        // Si hay un encadenado previo -> $a0
        // Self = encadenado previo
        codigo.agregarLinea("#Comienza codigo para llamada de metodo " + nodoLlamadaMetodo.getLexema());

        EntradaClase entradaClase = st.buscarClase(nodoLlamadaMetodo.getClaseEncadenadoPrev());

        codigo.agregarLinea("sw $fp 0($sp) # Guardar el frame pointer anterior en la pila");
        codigo.agregarLinea("addiu $sp $sp -4 # movemos el puntero de la pila");
        // Generar código para los argumentos y guardarlos en la pila

        LinkedList<NodoExp> parametros = nodoLlamadaMetodo.getParametros();

        int offSetTotalParametros = parametros.size() * (4); //Se calcula cuanto espacio van a ocupar los argumentos

        codigo.agregarLinea("addi $sp, $sp, " + (-offSetTotalParametros) + " # guardamos en la pila el espacio para todos los argumentos");

        boolean esConstructor = Character.isUpperCase(nodoLlamadaMetodo.getLexema().charAt(0));

        if (!esConstructor) {
            if (nodoLlamadaMetodo.getEsEncadenado()) {
                //Si tiene un objeto como encadenado previo se va a encontrar en -> $a0, hay que guardarlo en la pila
                codigo.agregarLinea("sw $a0, 0($sp) #Guardar el encadenado previo en la pila como self");
                codigo.agregarLinea("addiu $sp $sp -4 #movemos el puntero de la pila");
            }
            else {
                //Si no tiene un encadenado previo, o no es un constructor, se asume que es un metodo llamado desde self
                codigo.agregarLinea("lw $a0 4($fp) #Cargar el objeto (this) desde el frame pointer antrior");
                codigo.agregarLinea("sw $a0, 0($sp) #Guardar el objeto de la llamada en la pila como self");
                codigo.agregarLinea("addiu $sp $sp -4 #movemos el puntero de la pila");
            }
        }
        else {
            // Si es un constructor, se crea un nuevo CIR por defecto para el objeto
             generarCodigoClase(entradaClase.getLexema());
             codigo.agregarLinea("sw $a0, 0($sp) # Guardar el objeto de la llamada en la pila como self");
             codigo.agregarLinea("addiu $sp $sp -4 # movemos el puntero de la pila");

        }

        int offSetParametro = 4;
        for (NodoExp parametro : parametros) {
            parametro.accept(this); //Nos da la dirección de la CIR del argumento
            offSetParametro = offSetParametro + 4; //Calculamos el offset parametro
            codigo.agregarLinea("sw $a0 " + offSetParametro + "($sp) # Guardar el argumento en la pila");
        }

        codigo.agregarLinea("lw $t0 4($sp) # Cargar el objeto self desde la pila");
        codigo.agregarLinea("beqz $t0, nullPointerException # Verificar si el objeto es null");

        codigo.agregarLinea("lw $t0, 0($t0) # Cargar la vtable del objeto");

        int offSetMetodo = entradaClase.getMetodo(nodoLlamadaMetodo.getLexema()).getPosicionMetodo(); //Obtemenos el offset del metodo
        codigo.agregarLinea("lw $t0, " + (offSetMetodo * 4) + "($t0) # Calcular la dirección del método en la vtable");


        codigo.agregarLinea("jalr $t0 # Llamar al método " + nodoLlamadaMetodo.getLexema());

        codigo.agregarLinea("addi $sp $sp " + (offSetTotalParametros) + " # movemos el puntero de la pila para sacar los parametros");

        codigo.agregarLinea("addi $sp $sp 4 # movemos el puntero de la pila para sacar el self");
        codigo.agregarLinea("lw $fp 4($sp) # Restauramos el frame pointer");
        codigo.agregarLinea("addi $sp $sp 4 # movemos el puntero de la pila para sacar el frame pointer anterior");


        //Continuamos con el codigo para el encadenamiento
        if (nodoLlamadaMetodo.getEncadenado() != null){
            nodoLlamadaMetodo.getEncadenado().accept(this);
        }

        codigo.agregarLinea("#Termina codigo para llamada de metodo");

    }


    /**
     * Genera codigo para un acceso a un objeto
     * @param nodoVar Nodo que contiene el nombre de la variable
     */
    public void generarCodigo(NodoVar nodoVar){



        EntradaMetodo entradaMetodo = st.getMetodoActual(); //Obtenemos el metodo actual
        EntradaClase claseReferenciada; //inicializamos una clase
        int offset;


        if (nodoVar.getEsEncadenado()){
            //El caso de que el objeto sea el resultado de un encadenado previo
            //El encadenado previo ya dejo la dirección de la CIR en $a0

            claseReferenciada = st.buscarClase(nodoVar.getClaseEncadenadoPrev()); //Buscamos la clase a la cual pertenece el objeto como atributo
            EntradaAtributo atributo = claseReferenciada.buscarAtributo(nodoVar.getLexema()); //Buscamos el atributo en la clase referenciadas
            offset = (atributo.getPosicionAtributo() * (4)); //Calculamos ell offset dentro de la CIR del objeto del encadenado previo

            codigo.agregarLinea("lw $a0 ," + offset + "($a0) #Buscamos el atributo en la CIR del encadenado previo");


        }else {
            if(! nodoVar.getEsEstatico()){
                //No es estatico
                //El caso de que el objeto sea accedido directamente (sin encadenado previo)
                EntradaVariable variable = entradaMetodo.buscarVariableLocal(nodoVar.getLexema());
                if (variable != null) {
                    //El caso de que el objeto sea una variable
                    offset = variable.getPosicionVariable() * (-4); //Buscamos la posición de la variable pero el offset apunta primero al enlace dinamico
                    codigo.agregarLinea("lw $a0 ," + offset + "($fp) #Buscamos la variable en la pila");
                } else {
                    //El caso de que el objeto sea un parametro
                    EntradaParametro parametro = entradaMetodo.buscarParametro(nodoVar.getLexema());
                    if (parametro != null) {
                        // Posicion Parametro empieza en 0, y self ocupa 4($fp)
                        offset = (parametro.getPosicionParametro() * (4)) + 8; //Buscamos la posición del parametro pero el offset apunta primero al enlace dinamico y arriba esta el self
                        codigo.agregarLinea("lw $a0 ," + offset + "($fp) #Buscamos el parametro en la pila");
                    } else {
                        //El caso de que el objeto sea una variable de instancia
                        EntradaClase clase = st.getClassActual();
                        EntradaAtributo atributo = clase.buscarAtributo(nodoVar.getLexema());

                        codigo.agregarLinea("lw $t0  4($fp) #Buscamos el objeto self en la pila");

                        offset = atributo.getPosicionAtributo() * 4; //Buscamos el atributo del objeto pero el primer elemento de la cir es la vtable
                        codigo.agregarLinea("lw $a0 ," + offset + "($t0) #Buscamos el atributo en la CIR");

                    }
                }
            }else{
                //Es estatico

                codigo.agregarLinea("li $a0, 4 #Es un objeto estatico, reservamos 4 bytes en memoria para la VTABLE");
                codigo.agregarLinea("li $v0 9  # Solicitar espacio en memoria");
                codigo.agregarLinea("syscall ");

                codigo.agregarLinea("la $a0, VTABLE_" + nodoVar.getLexema() + " # Cargar la dirección de la vtable de la clase " + nodoVar.getLexema());
                codigo.agregarLinea("sw $a0, 0($v0) # Guardar la vtable en la CIR");
                codigo.agregarLinea("move $a0, $v0 # La dirección del objeto queda en $a0");
            }
        }

        //En caso de ser un NodoArratAcceso se procede a generar el codigo para acceder a la posicion indicacada.
        if (nodoVar.getClass() == NodoArrayAcceso.class){
            NodoArrayAcceso nodoArrayAcceso = (NodoArrayAcceso) nodoVar;
            generarCodigoAccesoArray(nodoArrayAcceso);
        }

        //Se genera el codigo el encadenado.
        if (nodoVar.getEncadenado() != null){
            nodoVar.getEncadenado().accept(this);
        }


    }


    /**
     * Genera codigo para un acceso al espacio en memoria de una variable
     * @param nodoVar Nodo que contiene el nombre de la variable
     */

    public void generarCodigoAccesoVariable(NodoVar nodoVar){

        // No devolvemos la direccion de la cir del objeto, devolvemos la direccion del espacio a memoria que apunta a la cir del objeto

        EntradaMetodo entradaMetodo = st.getMetodoActual(); //Obtenemos el metodo actual
        EntradaClase claseReferenciada; //inicializamos una clase
        int offset;


        if (nodoVar.getEsEncadenado()){
            //El caso de que el objeto sea el resultado de un encadenado previo
            //El encadenado previo ya dejo la dirección de la CIR en $a0

            codigo.agregarLinea("lw $a0 0($a0) # Cargamos el CIR del encadenado previo");

            claseReferenciada = st.buscarClase(nodoVar.getClaseEncadenadoPrev()); //Buscamos la clase a la cual pertenece el objeto como atributo
            EntradaAtributo atributo = claseReferenciada.buscarAtributo(nodoVar.getLexema()); //Buscamos el atributo en la clase referenciadas
            offset = (atributo.getPosicionAtributo() * (4)); //Calculamos el offset dentro de la CIR del objeto del encadenado previo

            //codigo.agregarLinea("lw $a0 ," + offset + "($a0) #Buscamos el atributo en la CIR del encadenado previo");
            codigo.agregarLinea("addiu $a0 $a0 , " + offset + " #Devolvemos la direccion del atributo en la CIR del encadenado previo");

        }else {
            //El caso de que el objeto sea accedido directamente (sin encadenado previo)
            EntradaVariable variable = entradaMetodo.buscarVariableLocal(nodoVar.getLexema());
            if (variable != null) {
                //El caso de que el objeto sea una variable
                offset = variable.getPosicionVariable() * (-4); //Buscamos la posición de la variable pero el offset apunta primero al enlace dinamico

                //codigo.agregarLinea("lw $a0 ," + offset + "($fp) #Buscamos la variable en la pila");

                codigo.agregarLinea("addiu $a0 $fp , " + offset + " #Devolvemos la direccion de la variable en la pila " + nodoVar.getLexema()+ "_" + variable.getPosicionVariable());
            } else {
                //El caso de que el objeto sea un parametro
                EntradaParametro parametro = entradaMetodo.buscarParametro(nodoVar.getLexema());
                if (parametro != null) {
                    offset = (parametro.getPosicionParametro() * (4)) + 4; //Buscamos la posición del parametro pero el offset apunta primero al enlace dinamico y arriba esta el self

                    //codigo.agregarLinea("lw $a0 ," + offset + "($fp) #Buscamos el parametro en la pila");
                    codigo.agregarLinea("addiu $a0 $fp , " + offset + " #Devolvemos la direccion del parametro en la pila");
                } else {
                    if (nodoVar.getLexema().equals("self")){
                        //El caso de que el objeto sea self
                        codigo.agregarLinea("addiu $a0 $fp , 4 #Devolvemos la direccion de self en la pila");

                    } else {
                        //El caso de que el objeto sea una variable de instancia
                        EntradaClase clase = st.getClassActual();
                        EntradaAtributo atributo = clase.buscarAtributo(nodoVar.getLexema());

                        codigo.agregarLinea("lw $t0  4($fp) #Buscamos el objeto self en la pila");

                        offset = atributo.getPosicionAtributo() * 4; //Buscamos el atributo del objeto pero el primer elemento de la cir es la vtable
                        //codigo.agregarLinea("lw $a0 ," + offset + "($t0) #Buscamos el atributo en la CIR");
                        codigo.agregarLinea("addiu $a0 $t0 " + offset + " #Devolvemos la direccion del atributo en la CIR");
                    }
                }
            }
        }
        if (nodoVar.getClass() == NodoArrayAcceso.class){
            NodoArrayAcceso nodoArrayAcceso = (NodoArrayAcceso) nodoVar;
            generarCodigoIzquierdaAccesoArray(nodoArrayAcceso);
        }


        NodoVar encadenado = (NodoVar) nodoVar.getEncadenado();

        if (encadenado != null) {
            encadenado.acceptLadoIzquerdo(this);
        }

    }


    /**
     * Genera codigo para un acceso a un atributo de array
     * @param nodoArrayAcceso Nodo que contiene el nombre del atributo
     */
    public void generarCodigoAccesoArray(NodoArrayAcceso nodoArrayAcceso){

        //generarCodigo(nodoArrayAcceso);

        codigo.agregarLinea("sw $a0, 0($sp) #guardar el objeto en la pila");
        codigo.agregarLinea("addiu $sp $sp -4 #movemos el puntero de la pila");

        nodoArrayAcceso.getIndice().accept(this);

        codigo.agregarLinea("lw $t0, 4($sp) #Cargar el array");
        codigo.agregarLinea("lw $t1, 4($t0) #Cargar el tamaño del array");

        codigo.agregarLinea("lw $t2, 4($a0) #Cargar el indice del array");

        codigo.agregarLinea("slt $t3, $t1, $t2 #Saber si el indice es mayor al tamaño del array");
        codigo.agregarLinea("beq $t3, 1, ArrayIndexOutOfBoundsException #Si es mayor salimos del metodo");

        codigo.agregarLinea("mul $t2, $t2, 4 #Convertir el indice del array a bytes");
        codigo.agregarLinea("add $t2 $t2 8 #Agregamos el offset de Vtable y tamaño");
        codigo.agregarLinea("add $t0, $t0, $t2 #Obtenemos el valor del elemento del array");
        codigo.agregarLinea("lw $a0, 0($t0) #Cargar el valor del elemento del array");
        codigo.agregarLinea("addiu $sp $sp 4 #movemos el puntero de la pila");
    }

    /**
     * Genera codigo para un acceso a al espacio en memoria de un atributo de array
     * @param nodoArrayAcceso Nodo que contiene el nombre del atributo
     */
    public void generarCodigoIzquierdaAccesoArray(NodoArrayAcceso nodoArrayAcceso){



        codigo.agregarLinea("lw $a0, 0($a0) #Cargar el array");
        codigo.agregarLinea("sw $a0, 0($sp) #guardar el objeto en la pila");
        codigo.agregarLinea("addiu $sp $sp -4 #movemos el puntero de la pila");

        nodoArrayAcceso.getIndice().accept(this);

        codigo.agregarLinea("lw $t0, 4($sp) #Cargar el array");
        codigo.agregarLinea("lw $t1, 4($t0) #Cargar el tamaño del array");

        codigo.agregarLinea("lw $t2, 4($a0) #Cargar el indice del array");

        codigo.agregarLinea("slt $t3, $t1, $t2 #Saber si el indice es mayor al tamaño del array");
        codigo.agregarLinea("beq $t3, 1, ArrayIndexOutOfBoundsException #Si es mayor salimos del metodo");

        codigo.agregarLinea("mul $t2, $t2, 4 #Convertir el indice del array a bytes");
        codigo.agregarLinea("add $t2 $t2 8  #Agregamos el offset de Vtable y tamaño");
        codigo.agregarLinea("add $a0, $t0, $t2 #Obtenemos la direccion del elemento del array");

        codigo.agregarLinea("addiu $sp $sp 4 #movemos el puntero de la pila");
    }


    /**
     * Genera codigo para el construcotor de un array
     */
    public void generarCodigo(NodoConstructorArray nodoArray) {

        nodoArray.getDimension().accept(this);
        //En a0 tenemos el CIR de int con el tamaño del array

        codigo.agregarLinea("lw $t0, 4($a0) #Cargar el tamaño del array");

        codigo.agregarLinea("slti $t2, $t0, 0 #Saber si el indice es negativo");
        codigo.agregarLinea("bne $t2, $zero, NegativeArraySizeException #Si es menor que cero salimos del metodo");


        codigo.agregarLinea("mul $t1, $t0, 4 #Calcular el tamaño en bytes del array");
        codigo.agregarLinea("addi $t1, $t1, 8 #Se suma al tamaño del array el espacio para guadar el tamaño y la vtable");

        codigo.agregarLinea("li $v0, 9  # Solicitar espacio en memoria");

        codigo.agregarLinea("move $a0, $t1  # Espacio para el array");
        codigo.agregarLinea("syscall ");

        codigo.agregarLinea("la $t1, VTABLE_Array # Cargar la dirección de la vtable de Array en un temporal");
        codigo.agregarLinea("sw $t1, 0($v0) #guardamos la dirección de la vtableArray en la CIR");
        codigo.agregarLinea("sw $t0, 4($v0) #Guardamos el tamaño del array en la CIR");
        codigo.agregarLinea("move $t4, $v0  # Espacio para el array");
        codigo.agregarLinea("addi $t4 $t4 8 #apuntamos al primer elemento del array");



        if(Objects.equals(nodoArray.getTipo(), "int")){
            codigo.agregarLinea("la $t1, VTABLE_Int # Cargar la dirección de la vtable de Int en un temporal");
            codigo.agregarLinea("li $t3, 8 #Tamaño de la CIR de int");
        }else{
            if(Objects.equals(nodoArray.getTipo(), "Double")){
                codigo.agregarLinea("la $t1, VTABLE_Int # Cargar la dirección de la vtable Double en un temporal");
                codigo.agregarLinea("li $t3, 12 #Tamaño de la CIR de double");
            }else{
                if(Objects.equals(nodoArray.getTipo(), "Bool")){
                    codigo.agregarLinea("la $t1, VTABLE_Bool # Cargar la dirección de la vtable de Bool en un temporal");
                    codigo.agregarLinea("li $t3, 8 #Tamaño de la CIR de bool");
                }else{
                    if (Objects.equals(nodoArray.getTipo(), "String")) {
                        codigo.agregarLinea("la $t1, VTABLE_Str # Cargar la dirección de la vtable de String en un temporal");;
                        codigo.agregarLinea("li $t3, 8 #Tamaño de la CIR de String");
                    }
                }
            }
        }


        codigo.agregarLinea("move $t2, $v0 # La dirección del objeto Array queda en $t2");

        String labelLoop = getLabel(st.getMetodoActual()) + genLabel(nodoArray) + "_loop";
        codigo.agregarLinea(labelLoop + ":" );

        codigo.agregarLinea("li $v0, 9  # Solicitar espacio en memoria");
        codigo.agregarLinea("move $a0, $t3 # Espacio para el primer elemento del array");
        codigo.agregarLinea("syscall");
        codigo.agregarLinea("sw $t1, 0($v0) #guardamos la dirección de la vtable del tipo en la CIR");
        codigo.agregarLinea("sw $v0 0($t4) #Guardamos el objeto en el array");
        codigo.agregarLinea("addi $t4 $t4 4 #apuntamos al siguiente elemento del array");

        codigo.agregarLinea("addi $t0, $t0, -1 #Decrementamos el tamaño del contador");
        codigo.agregarLinea("bnez $t0 "+ labelLoop+" #Si el tamaño del array es distinto de cero, saltar la inicialización");


        codigo.agregarLinea("move $a0, $t2 # La dirección del objeto Array queda en $a0");

    }


    /**
     * Genera un label para un nodo sentencia
     * @param ns nodo sentencia
     * @return label
     */
    public String genLabel(NodoSentencia ns){

        return "S_" + ns.posicion.getLinea() + "_" + ns.posicion.getColumna();
    }




    /**
     * Genera codigo para la creacion de un objeto de una clase
     * @param nombreClase nombre de la clase
     */
    public void generarCodigoClase(String nombreClase){

        EntradaClase entradaClase = st.buscarClase(nombreClase);
        // Cargamos el valor por defecto de la variable en $a0

        if (Objects.equals(entradaClase.getLexema(), "Int")){
            codigo.agregarLinea("li $v0, 9  # Solicitar espacio en memoria");
            codigo.agregarLinea("li $a0, 8  # 4 bytes y su vtable");
            codigo.agregarLinea("syscall ");

            codigo.agregarLinea("la $t0, VTABLE_Int # Cargar la dirección de la vtable de Int en un temporal");
            codigo.agregarLinea("sw $t0, 0($v0) #guardamos la dirección de la vtableInt en la CIR");
            codigo.agregarLinea("li $t0, 0 # Guardamos el valor en la CIR en un temporal");
            codigo.agregarLinea("sw $t0, 4($v0) #Guardamos el valor en la CIR");

            codigo.agregarLinea("move $a0, $v0 # La dirección del objeto Int queda en $a0");
        }else {


            if (Objects.equals(entradaClase.getLexema(), "Double")) {
                codigo.agregarLinea("li $v0, 9  # Solicitar espacio en memoria");
                codigo.agregarLinea("li $a0, 12  # 8 bytes y su vtable");
                codigo.agregarLinea("syscall ");

                codigo.agregarLinea("la $t0, VTABLE_Double # Cargar la dirección de la vtable de Int en un temporal");
                codigo.agregarLinea("sw $t0, 0($v0) #guardamos la dirección de la vtableDouble en la CIR");
                codigo.agregarLinea("li $t0, 0 # Guardamos el valor en la CIR en un temporal");
                codigo.agregarLinea("sw $t0, 4($v0) #Guardamos el valor en la CIR");


                codigo.agregarLinea("move $a0, $v0 # La dirección del objeto Int queda en $a0");
            } else {


                if (Objects.equals(entradaClase.getLexema(), "Bool")) {
                    codigo.agregarLinea("li $v0, 9  # Solicitar espacio en memoria");
                    codigo.agregarLinea("li $a0, 8  # 4 bytes y su vtable");
                    codigo.agregarLinea("syscall ");

                    codigo.agregarLinea("la $t0, VTABLE_Bool # Cargar la dirección de la vtable de Int en un temporal");
                    codigo.agregarLinea("sw $t0, 0($v0) #guardamos la dirección de la vtableBool en la CIR");
                    codigo.agregarLinea("li $t0, 0 # Guardamos el valor en la CIR en un temporal");
                    codigo.agregarLinea("sw $t0, 4($v0) #Guardamos el valor en la CIR");


                    codigo.agregarLinea("move $a0, $v0 # La dirección del objeto Int queda en $a0");
                } else {
                    if (Objects.equals(entradaClase.getLexema(), "Str")) {
                        codigo.agregarLinea("li $v0, 9  # Solicitar espacio en memoria");
                        codigo.agregarLinea("li $a0, 8  # su vtable");
                        codigo.agregarLinea("syscall ");

                        codigo.agregarLinea("la $t0, VTABLE_Str # Cargar la dirección de la vtable de String en un temporal");
                        codigo.agregarLinea("sw $t0, 0($v0) #guardamos la dirección de la vtableString en la CIR");
                        codigo.agregarLinea("li $t0, 0 # Guardamos el valor en la CIR en un temporal");
                        codigo.agregarLinea("sw $t0, 4($v0) #Guardamos el valor en la CIR");


                        codigo.agregarLinea("move $a0, $v0 # La dirección del objeto Int queda en $a0");
                    } else {


                        codigo.agregarLinea("li $v0, 9  # Solicitar espacio en memoria");


                        int z = entradaClase.getTamanioObjeto();

                        codigo.agregarLinea("li $a0, " + z + " #su vtable");
                        codigo.agregarLinea("syscall ");

                        codigo.agregarLinea("la $t0, VTABLE_" + entradaClase.getLexema() + " # Cargar la dirección de la vtable en un temporal");
                        codigo.agregarLinea("sw $t0, 0($v0) #guardamos la dirección de la vtable en la CIR");

                        int i;


                        codigo.agregarLinea("sw $v0 0($sp) #Guardamos la direccion de la cir del objeto en la pila");
                        codigo.agregarLinea("addiu $sp $sp -4 #restamos 4 bytes para guardar la direccion de la cir del objeto");

                        for (EntradaAtributo atributo : entradaClase.getAtributos().values()) {

                            generarCodigoClase(atributo.getTipo());
                            i = atributo.getPosicionAtributo();
                            codigo.agregarLinea("lw $v0, 4($sp) #traemos la direccion de la cir del objeto de la pila");
                            codigo.agregarLinea("sw $a0 " + (4 * i) + "($v0) #Inicializamos el atributo " + atributo.getLexema());
                        }


                        codigo.agregarLinea("lw $a0 4($sp) #Recuperamos la direccion de la cir del objeto de la pila y la dejamos en $a0");
                        codigo.agregarLinea("addiu $sp $sp 4 #Sacamos la direccion de la cir del objeto de la pila");

                    }
                }
            }
        }





    }

    /**
     * Genera codigo para un literal nil
     * @param nodoNil nodo que contiene el valor nil
     */
    public void generarCodigo(NodoNil nodoNil){
        codigo.agregarLinea("li $a0 , 0 #Guardamos el valor en la CIR en un temporal");
    }


}