package generacionDeCodigo;

import java.io.*;
import java.util.LinkedList;


/**
 * Clase encargada de generar el codigo final en MIPS
 */
public class CodeGen {
    /**
     * Listas para almacenar el codigo por debajo de .data
     */
    LinkedList<String> data = new LinkedList<>();


    /**
     * Lista para almacenar el codigo por debajo de .text
     */
    LinkedList<String> codigo = new LinkedList<>();


    /**
     * Agrega una linea al codigo final
     * @param linea linea a agregar
     */
    public void agregarLinea(String linea) {
        codigo.add(linea);
    }

    /**
     * Agrega una linea al data final
     * @param data linea a agregar
     */
    public void agregarData(String data) {
        this.data.add(data);
    }


    /**
     * Una vez finalizado la etapa de generación de codigo se escribe el archivo final.
     */
    public String consolidar(){
        StringBuilder salida = new StringBuilder();

        salida.append(".data\n");
        for (String dato : data){
            salida.append(dato).append("\n");
        }

        salida.append("\n.text\n");

        for (String linea : codigo){
            salida.append(linea).append("\n\n\t");
        }

        salida.append("\n");

        String vtable_IO_path = "src/main/java/generacionDeCodigo/metodos/VTABLE_IO.txt";
        String vtable_Array_path = "src/main/java/generacionDeCodigo/metodos/VTABLE_Array.txt";
        String vtable_Str_path = "src/main/java/generacionDeCodigo/metodos/VTABLE_Str.txt";
        String exception_handling_path = "src/main/java/generacionDeCodigo/metodos/Exceptions.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(vtable_IO_path))) {
            for (String line; (line = reader.readLine()) != null; ) {
                salida.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Error al escribir la VTABLE_IO: '" + e.getMessage());
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(vtable_Array_path))) {
            for (String line; (line = reader.readLine()) != null; ) {
                salida.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Error al escribir la VTABLE_IO: '" + e.getMessage());
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(vtable_Str_path))) {
            for (String line; (line = reader.readLine()) != null; ) {
                salida.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Error al escribir la VTABLE_IO: '" + e.getMessage());
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(exception_handling_path))) {
            for (String line; (line = reader.readLine()) != null; ) {
                salida.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Error al escribir la VTABLE_IO: '" + e.getMessage());
        }

        return salida.toString();
    }
}
