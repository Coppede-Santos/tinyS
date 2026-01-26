package generacionDeCodigo;

import analizadorSemantico.*;
import ast.AST;
import ast.NodoBloque;
import ast.NodoClass;
import ast.NodoSentencia;

import java.util.Objects;

public class TopVisitor extends NodeVisitor {

    public TopVisitor(SymbolTable st, AST ast) {
        this.st = st;
        this.ast = ast;
    }

    public void generarCodigo(){
        codigo.agregarLinea("main:");
        codigo.agregarLinea("sw $fp 0($sp)");
        codigo.agregarLinea("addiu $sp $sp -4");
        codigo.agregarLinea("jal " + getLabel(st.getStartMethod()));
        codigo.agregarLinea("b exit");

        EntradaMetodo entradaStart = st.getStartMethod();
        st.setMetodoActual(entradaStart);
        entradaStart.accept(this, ast.getStart());

        // Generamos las vtables de las clases
        for(EntradaClase clase : st.getClases().values()){
            if (!esClasePrimitiva(clase)){
                st.setClassActual(clase);
                clase.accept(this);
            }
        }

        codigo.agregarLinea("exit:");
        codigo.agregarLinea("li $v0, 10  # syscall para exit");
        codigo.agregarLinea("syscall  # salir del programa");

    }

    public void generarCodigo(EntradaClase clase){

        codigo.agregarData("VTABLE_"+clase.getLexema()+": #Vtable de la clase "+clase.getLexema());

        for(EntradaMetodo metodo : clase.getMetodos().values()){
            codigo.agregarData(".word " + getLabel(metodo));
        }

        NodoClass nodoClase = ast.getClass(clase.getLexema());

        EntradaMetodo constructor = clase.getConstructor();
        NodoBloque bloqueConstructor = nodoClase.getMetodo(constructor.getLexema());

        constructor.accept(this, bloqueConstructor);

        NodoBloque bloqueMetodo;

        if (!esClasePrimitiva(st.getClassActual())) {
            for (EntradaMetodo metodo : clase.getMetodos().values()) {
                st.setMetodoActual(metodo);
                bloqueMetodo = nodoClase.getMetodo(metodo.getLexema());
                metodo.accept(this, bloqueMetodo);
            }
        }

    }

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
        }
        if (Objects.equals(variable.getTipo(), "Double")){
            codigo.agregarLinea("li $v0, 9  # Solicitar espacio en memoria");
            codigo.agregarLinea("li $a0, 12  # 8 bytes y su vtable");
            codigo.agregarLinea("syscall ");

            codigo.agregarLinea("la $t0, VTABLE_Double # Cargar la dirección de la vtable de Int en un temporal");
            codigo.agregarLinea("sw $t0, 0($v0) #guardamos la dirección de la vtableDouble en la CIR");
            codigo.agregarLinea("li $t0, 0 # Guardamos el valor en la CIR en un temporal");
            codigo.agregarLinea("sw $t0, 4($v0) #Guardamos el valor en la CIR");


            codigo.agregarLinea("move $a0, $v0 # La dirección del objeto Int queda en $a0");
        }
        if (Objects.equals(variable.getTipo(), "Bool")){
            codigo.agregarLinea("li $v0, 9  # Solicitar espacio en memoria");
            codigo.agregarLinea("li $a0, 8  # 4 bytes y su vtable");
            codigo.agregarLinea("syscall ");

            codigo.agregarLinea("la $t0, VTABLE_Bool # Cargar la dirección de la vtable de Int en un temporal");
            codigo.agregarLinea("sw $t0, 0($v0) #guardamos la dirección de la vtableBool en la CIR");
            codigo.agregarLinea("li $t0, 0 # Guardamos el valor en la CIR en un temporal");
            codigo.agregarLinea("sw $t0, 4($v0) #Guardamos el valor en la CIR");


            codigo.agregarLinea("move $a0, $v0 # La dirección del objeto Int queda en $a0");
        }
        if (Objects.equals(variable.getTipo(), "String")){
            codigo.agregarLinea("li $v0, 9  # Solicitar espacio en memoria");
            codigo.agregarLinea("li $a0, 8  # su vtable");
            codigo.agregarLinea("syscall ");

            codigo.agregarLinea("la $t0, VTABLE_String # Cargar la dirección de la vtable de String en un temporal");
            codigo.agregarLinea("sw $t0, 0($v0) #guardamos la dirección de la vtableString en la CIR");
            codigo.agregarLinea("li $t0,  # Guardamos el valor en la CIR en un temporal");
            codigo.agregarLinea("sw $t0, 4($v0) #Guardamos el valor en la CIR");


            codigo.agregarLinea("move $a0, $v0 # La dirección del objeto Int queda en $a0");
        }

        if (!variable.esPrimitivo()){
            codigo.agregarLinea("li $v0, 9  # Solicitar espacio en memoria");

            EntradaClase clase =  st.buscarClase(variable.getTipo());

            int z = clase.getTamanioObjeto();

            codigo.agregarLinea("li $a0,"+ z +"# su vtable");
            codigo.agregarLinea("syscall ");

            codigo.agregarLinea("la $t0, VTABLE_"+ clase.getLexema() +" # Cargar la dirección de la vtable en un temporal");
            codigo.agregarLinea("sw $t0, 0($v0) #guardamos la dirección de la vtable en la CIR");

            int i;

            for (EntradaAtributo atributo : clase.getAtributos().values()){
                atributo.accept(this);
                i = atributo.getPosicionAtributo();
                codigo.agregarLinea("sw $a0 " + (4*i) + "($v0) #Inicializamos el atributo "+ atributo.getLexema());
            }

            codigo.agregarLinea("move $a0, $v0 # La dirección del objeto queda en $a0");

        }

    }

    public void generarCodigo(EntradaMetodo entradaMetodo, NodoBloque nodoBloque){

        int z = entradaMetodo.getCantidadVariablesLocales() * 4;

        codigo.agregarLinea(getLabel(entradaMetodo) +": # Label del metodo" );

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

       codigo.agregarLinea("lw $ra 0($fp) #cargamos el return address");
        codigo.agregarLinea("addiu $sp $sp "+ z + " #limpiamos la pila de las variables locales");
        codigo.agregarLinea("addiu $sp $sp 4 #limpiamos la pila del return address");
        codigo.agregarLinea("jr $ra #salimos del metodo");


    }


    public String getLabel(EntradaMetodo metodo) {
        String label = "m_" + metodo.getLexema() + "_" +metodo.getLinea() + "_" + metodo.getColumna();
        return label;
    }

    public boolean esClasePrimitiva(EntradaClase clase) {
        return clase.getLexema().equals("Int") ||
               clase.getLexema().equals("Double") ||
               clase.getLexema().equals("Bool") ||
               clase.getLexema().equals("Str") ||
               clase.getLexema().equals("IO") ||
                clase.getLexema().equals("Object") ||
                clase.getLexema().equals("Array")
                ;
    }

    public CodeGen getCodigo() {
        return codigo;
    }

}
