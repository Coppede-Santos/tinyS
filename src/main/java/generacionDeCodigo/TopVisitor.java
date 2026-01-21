package generacionDeCodigo;

import analizadorSemantico.EntradaClase;
import analizadorSemantico.EntradaMetodo;
import ast.NodoBloque;
import ast.NodoSentencia;

public class TopVisitor extends NodeVisitor {

    public void generarCodigo(EntradaClase clase){

        codigo.agregarData("VTABLE_"+clase.getLexema()+": #Vtable de la clase "+clase.getLexema());
        for(EntradaMetodo metodo : clase.getMetodos().values()){
            metodo.accept();
        }

    }

    public void generarCodigo(EntradaMetodo entradaMetodo, NodoBloque nodoBloque){

        int z = 8;
        z += entradaMetodo.getCantidadParametros() * 4;
        z += entradaMetodo.getCantidadVariablesLocales() * 4;

        codigo.agregarText(getLabel(EntradaMetodo) +": # Label del metodo" );
        codigo.agregarText("move $fp $sp #movemos el puntero de frame al puntero de stack");
        codigo.agregarText("sw $ra 0($sp) #guardamos en la pila el return address");
        codigo.agregarText("addiu $sp $sp -4 #restamos 4 bytes para guardar el return address");


        for (NodoSentencia sentencia : nodoBloque.getSentencias()) {
            sentencia.accept();
        }

       codigo.agregarText("lw $ra 4($sp) #cargamos el return address");
        codigo.agregarText("addiu $sp $sp "+ z + " #limpiamos la pila de las variables locales");
        codigo.agregarText("lw $fp 0($sp) #restauramos el frame pointer");
        codigo.agregarText("jr $ra #salimos del metodo");


    }


    public String getLabel(EntradaMetodo metodo) {
        String label = "Metodo_" + metodo.getLexema() + "_" +metodo.getLinea() + "_" + metodo.getColumna();
        return label;
    }

}
