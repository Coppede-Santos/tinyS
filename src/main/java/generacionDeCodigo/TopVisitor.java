package generacionDeCodigo;

import analizadorSemantico.*;
import ast.NodoBloque;
import ast.NodoSentencia;

import java.util.Objects;

public class TopVisitor extends NodeVisitor {

    public void generarCodigo(EntradaClase clase){

        codigo.agregarData("VTABLE_"+clase.getLexema()+": #Vtable de la clase "+clase.getLexema());
        for(EntradaMetodo metodo : clase.getMetodos().values()){
            metodo.accept(this);
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

            codigo.agregarLinea("la $t0, VTABLE_"+ clase.getLexema() +" # Cargar la dirección de la vtable de String en un temporal");
            codigo.agregarLinea("sw $t0, 0($v0) #guardamos la dirección de la vtable en la CIR");

            int i;

            for (EntradaAtributo atributo : clase.getAtributos().values()){
                atributo.accept(this);
                i = atributo.getPosicionAtributo();
                codigo.agregarText("sw $a0 " + (4*i) + "($v0) #Inicializamos el atributo "+ atributo.getLexema());
            }


        }

    }

    public void generarCodigo(EntradaMetodo entradaMetodo, NodoBloque nodoBloque){



        int z = entradaMetodo.getCantidadVariablesLocales() * 4;

        codigo.agregarText(entradaMetodo.getLexema() +": # Label del metodo" );

        codigo.agregarText("sw $ra 0($sp) #guardamos en la pila el return address");
        codigo.agregarText("addiu $sp $sp -4 #restamos 4 bytes para guardar el return address");


        codigo.agregarText("addi $sp $sp " + z + "#restamos 4 bytes para cada variable local");

        int i;
        for(EntradaVariable variable : entradaMetodo.getVariablesLocales().values()){
            // Inicializamos las variables locales
            variable.accept(this);
            i = variable.getPosicionVariable();
            codigo.agregarText("sw $a0 " + (-4*i) + "($fp)");
        }

        MethodBodyVisitor methodBodyVisitor = new MethodBodyVisitor();
        for (NodoSentencia sentencia : nodoBloque.getSentencias()) {
            sentencia.accept(methodBodyVisitor);
        }

       codigo.agregarText("lw $ra 0($fp) #cargamos el return address");
        codigo.agregarText("addiu $sp $sp "+ z + " #limpiamos la pila de las variables locales");
        codigo.agregarText("addiu $sp $sp 4 #limpiamos la pila del return address");
        codigo.agregarText("jr $ra #salimos del metodo");


    }


//    public String getLabel(EntradaMetodo metodo) {
//        String label = "Metodo_" + metodo.getLexema() + "_" +metodo.getLinea() + "_" + metodo.getColumna();
//        return label;
//    }

}
