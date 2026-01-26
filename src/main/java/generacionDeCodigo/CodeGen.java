package generacionDeCodigo;

import java.util.LinkedList;

public class CodeGen {
    LinkedList<String> data = new LinkedList<>();
    LinkedList<String> codigo = new LinkedList<>();

    public void agregarLinea(String linea) {
        codigo.add(linea);
    }

    public void agregarData(String data) {
        this.data.add(data);
    }


    public String consolidar(){
        StringBuilder salida = new StringBuilder();

        salida.append(".data\n");
        for (String dato : data){
            salida.append(dato).append("\n");
        }

        salida.append("\n.text\n");

        for (String linea : codigo){
            salida.append(linea).append("\n");
        }

        return salida.toString();
    }
}
