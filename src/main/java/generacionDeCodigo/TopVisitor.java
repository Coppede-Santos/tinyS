package generacionDeCodigo;

import analizadorSemantico.*;
import ast.AST;
import ast.NodoBloque;
import ast.NodoClass;
import ast.NodoSentencia;

import java.util.Objects;

/**
 * Clase encargada de generar el codigo para el main, las vtables, las definiciones de metodos y las variables locales.
 */
public class TopVisitor extends NodeVisitor {

    /**
     * Constructor de la clase
     * @param st tabla de simbolos
     * @param ast Arbol semantico abstracto
     */
    public TopVisitor(SymbolTable st, AST ast) {
        this.st = st;
        this.ast = ast;
    }

    /**
     * Genera el codigo para el main, las vtables, las definiciones de metodos y las variables locales.
     */
    public void generarCodigo(){
        codigo.agregarLinea("main:");
        codigo.agregarLinea("sw $fp 0($sp)");
        codigo.agregarLinea("addiu $sp $sp -4");
        codigo.agregarLinea("jal " + getLabel(st.getStartMethod()));
        codigo.agregarLinea("lw $fp 0($sp)");
        codigo.agregarLinea("addiu $sp $sp 4");
        codigo.agregarLinea("b exit");

        EntradaMetodo entradaStart = st.getStartMethod();
        st.setMetodoActual(entradaStart);
        entradaStart.accept(this, ast.getStart());

        // Generamos las vtables de las clases
        for(EntradaClase clase : st.getClases().values()){
            if (!clase.esClasePrimitiva()){
                st.setClassActual(clase);
                clase.accept(this);
            }
        }

        codigo.agregarLinea("exit:");
        codigo.agregarLinea("li $v0, 10  # syscall para exit");
        codigo.agregarLinea("syscall  # salir del programa");

    }

    /**
     * Genera el codigo para las vtables de las clases.
     * @param clase clase para la cual se generaran las vtables
     */
    public void generarCodigo(EntradaClase clase){

        EntradaMetodo constructor = clase.getConstructor();

        codigo.agregarData("VTABLE_"+clase.getLexema()+": #Vtable de la clase "+clase.getLexema());
        codigo.agregarData(".word " + getLabel(constructor));

        for(EntradaMetodo metodo : clase.getMetodos().values()){
            codigo.agregarData(".word " + getLabel(metodo));
        }

        NodoClass nodoClase = ast.getClass(clase.getLexema());
        NodoBloque bloqueConstructor = nodoClase.getMetodo(constructor.getLexema());

        st.setMetodoActual(constructor);
        constructor.accept(this, bloqueConstructor);

        NodoBloque bloqueMetodo;

        if (!clase.esClasePrimitiva()) {
            for (EntradaMetodo metodo : clase.getMetodos().values()) {
                st.setMetodoActual(metodo);
                bloqueMetodo = nodoClase.getMetodo(metodo.getLexema());
                if (bloqueMetodo != null) metodo.accept(this, bloqueMetodo);
            }
        }

    }

    /**
     * Genera el codigo para inicializar las variables locales.
     * @param variable variable para la cual se generara el codigo
     */
    public void generarCodigo(EntradaVariable variable){
        // Cargamos el valor por defecto de la variable en $a0

        if (Objects.equals(variable.getTipo(), "Int")){
            codigo.agregarLinea("li $v0, 9  # Solicitar espacio en memoria");
            codigo.agregarLinea("li $a0, 8  # 4 bytes y su vtable");
            codigo.agregarLinea("syscall ");

            codigo.agregarLinea("la $t0, VTABLE_Int # Cargar la dirección de la vtable de Int en un temporal");
            codigo.agregarLinea("sw $t0, 0($v0) #guardamos la dirección de la vtableInt en la CIR");
            codigo.agregarLinea("li $t0, 0 # Guardamos el valor en la CIR en un temporal");
            codigo.agregarLinea("sw $t0, 4($v0) #Guardamos el valor en la CIR");

            codigo.agregarLinea("move $a0, $v0 # La dirección del objeto Int queda en $a0");
        }else {


            if (Objects.equals(variable.getTipo(), "Double")) {
                codigo.agregarLinea("li $v0, 9  # Solicitar espacio en memoria");
                codigo.agregarLinea("li $a0, 12  # 8 bytes y su vtable");
                codigo.agregarLinea("syscall ");

                codigo.agregarLinea("la $t0, VTABLE_Double # Cargar la dirección de la vtable de Int en un temporal");
                codigo.agregarLinea("sw $t0, 0($v0) #guardamos la dirección de la vtableDouble en la CIR");
                codigo.agregarLinea("li $t0, 0 # Guardamos el valor en la CIR en un temporal");
                codigo.agregarLinea("sw $t0, 4($v0) #Guardamos el valor en la CIR");


                codigo.agregarLinea("move $a0, $v0 # La dirección del objeto Double queda en $a0");
            } else {


                if (Objects.equals(variable.getTipo(), "Bool")) {
                    codigo.agregarLinea("li $v0, 9  # Solicitar espacio en memoria");
                    codigo.agregarLinea("li $a0, 8  # 4 bytes y su vtable");
                    codigo.agregarLinea("syscall ");

                    codigo.agregarLinea("la $t0, VTABLE_Bool # Cargar la dirección de la vtable de Int en un temporal");
                    codigo.agregarLinea("sw $t0, 0($v0) #guardamos la dirección de la vtableBool en la CIR");
                    codigo.agregarLinea("li $t0, 0 # Guardamos el valor en la CIR en un temporal");
                    codigo.agregarLinea("sw $t0, 4($v0) #Guardamos el valor en la CIR");


                    codigo.agregarLinea("move $a0, $v0 # La dirección del objeto Bool queda en $a0");
                } else {
                    if (Objects.equals(variable.getTipo(), "Str")) {
                        codigo.agregarLinea("li $v0, 9  # Solicitar espacio en memoria");
                        codigo.agregarLinea("li $a0, 8  # su vtable");
                        codigo.agregarLinea("syscall ");

                        codigo.agregarLinea("la $t0, VTABLE_Str # Cargar la dirección de la vtable de String en un temporal");
                        codigo.agregarLinea("sw $t0, 0($v0) #guardamos la dirección de la vtableString en la CIR");
                        codigo.agregarLinea("li $t0, 0 # Guardamos el valor en la CIR en un temporal");
                        codigo.agregarLinea("sw $t0, 4($v0) #Guardamos el valor en la CIR");


                        codigo.agregarLinea("move $a0, $v0 # La dirección del objeto String queda en $a0");
                    } else {
                        codigo.agregarLinea("li $v0, 9  # Solicitar espacio en memoria");

                        EntradaClase clase = st.buscarClase(variable.getTipo());

                        int z = clase.getTamanioObjeto();

                        codigo.agregarLinea("li $a0," + z + "# su vtable");
                        codigo.agregarLinea("syscall ");

                        codigo.agregarLinea("la $t0, VTABLE_" + clase.getLexema() + " # Cargar la dirección de la vtable en un temporal");
                        codigo.agregarLinea("sw $t0, 0($v0) #guardamos la dirección de la vtable en la CIR");

                        int i;


                        codigo.agregarLinea("sw $v0 0($sp) #Guardamos la direccion de la cir del objeto en la pila");
                        codigo.agregarLinea("addiu $sp $sp -4 #restamos 4 bytes para guardar la direccion de la cir del objeto");

                        for (EntradaAtributo atributo : clase.getAtributos().values()) {

                            atributo.accept(this);
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
     * Genera el codigo para la definicion de un metodo.
     * @param entradaMetodo entrada del metodo
     * @param nodoBloque sentencias del metodo
     */
    public void generarCodigo(EntradaMetodo entradaMetodo, NodoBloque nodoBloque){

        int z = entradaMetodo.getCantidadVariablesLocales() * 4;

        codigo.agregarLinea(getLabel(entradaMetodo) +": # Label del metodo" );

        codigo.agregarLinea("move $fp $sp #El frame apunta al enlace dinamico");
        codigo.agregarLinea("sw $ra 0($sp) #guardamos en la pila el return address");
        codigo.agregarLinea("addiu $sp $sp -4 #restamos 4 bytes para guardar el return address");


        codigo.agregarLinea("addi $sp $sp " + (-1)*z + " #restamos 4 bytes para cada variable local");

        int i;
        for(EntradaVariable variable : entradaMetodo.getVariablesLocales().values()){
            // Inicializamos las variables locales
            variable.accept(this);
            i = variable.getPosicionVariable();
            codigo.agregarLinea("sw $a0 " + (-4*i) + "($fp)");
        }

        MethodBodyVisitor methodBodyVisitor = new MethodBodyVisitor(st, ast, codigo);
        for (NodoSentencia sentencia : nodoBloque.getSentencias()) {
            sentencia.accept(methodBodyVisitor);
        }

        String lexemaMetodo = entradaMetodo.getLexema();

        if (Character.isUpperCase(lexemaMetodo.charAt(0))) {
            codigo.agregarLinea("# Devolvemos el self del constructor en $a0");
            codigo.agregarLinea("lw $a0 4($fp) # cargamos el self en $a0");
        }

        codigo.agregarLinea("lw $ra 0($fp) #cargamos el return address");
        codigo.agregarLinea("addiu $sp $sp "+ z + " #limpiamos la pila de las variables locales");
        codigo.agregarLinea("addiu $sp $sp 4 #limpiamos la pila del return address");


        codigo.agregarLinea("jr $ra #salimos del metodo");


    }


    /**
     * Genera el label para un metodo.
     * @param metodo metodo para el cual se generara el label
     * @return label generado
     */
    public String getLabel(EntradaMetodo metodo) {
        String label = "m_" + metodo.getLexema() + "_" +metodo.getLinea() + "_" + metodo.getColumna();
        return label;
    }



    /**
     * Devuelve el codigo generado.
     */
    public CodeGen getCodigo() {
        return codigo;
    }

}
