package analizadorLexico;

import analizadorLexico.Errores.ErrorLex;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * La clase {@code Etapa1} es la clase principal que realiza el análisis léxico de un archivo de entrada
 * y guarda los tokens resultantes en un archivo de salida.
 */

public class Etapa1 {

    /**
     * Metodo principal que ejecuta el análisis léxico.
     *
     * @param args Un array de argumentos de línea de comandos. Se espera que el primer argumento sea la ruta
     *             del archivo de entrada. Opcionalmente, el segundo argumento puede ser la ruta del archivo de salida.
     * @throws IOException Si ocurre un error de entrada/salida durante la lectura o escritura de archivos.
     * @throws ErrorLex Si ocurre un error léxico durante el análisis.
     */


    public static void main(String[] args) throws IOException, ErrorLex {
        String source;
        Escaner escaner = new Escaner();
        LectorCF lector = new LectorCF();
        escaner.setEscaner(lector);
        // Verificar si se proporcionan los argumentos de línea de comandos necesarios
        if (args.length < 1) {
            System.out.println("Por favor, proporciona la ruta del archivo de entrada como argumento.");
            //System.out.println("Ejemplo: java analizadorLexico.Etapa1 archivo_entrada.s archivo_salida.txt");
            return; // Salir si no se proporcionan los argumentos
        }
        String rutaArchivoEntrada = args[0];
        if (!rutaArchivoEntrada.endsWith(".s")) {
            System.out.println("Por favor, proporciona la ruta del archivo de entrada debe terminar en '.s'.");
            return;
        }
        String nombreArchivoSalida = "";

        if (args.length == 2) {
            nombreArchivoSalida = args[1];
        }else{
            if(args.length > 2){
                System.out.println("Por favor, solo proporcionar la ruta del archivo de entrada y salida como argumento.");
            }
        }

        String salida = "";
        boolean analisisCorrecto = true;
        lector.lectorArchivo(rutaArchivoEntrada);

        try {
            salida += ("CORRECTO: ANALISIS LEXICO\n");
            source = lector.rechargeBuffer();
            escaner.setBuffer(source);
            List<Token> tokens = new ArrayList<>();
            Token tokenActual;
            do {
                try {
                    tokenActual = escaner.nextToken();
                    if (tokenActual != null) {
                        tokens.add(tokenActual);
                        salida += (tokenActual.toString() + "\n"); // Escribir el token en el archivo
                    } else {
                        // Manejar el caso en que tokenActual es nulo
                        //System.err.println("Error: tokenActual es nulo");
                        break; // Salir del bucle si no se puede obtener un token válido
                    }
                } catch (ErrorLex e) {
                    System.out.println("ERROR: LEXICO\n" + e.getMessage());
                    tokenActual = new Token(TokenType.EOF, "", 0, 0);
                    analisisCorrecto = false;
                }
            } while (tokenActual.getType() != TokenType.EOF);

        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo de salida: " + e.getMessage());
        } finally {
            if (analisisCorrecto) {
                if (args.length == 2) {
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivoSalida))) {
                        writer.write(salida);
                        System.out.println("CORRECTO: ANALISIS LEXICO");
                    } catch (IOException e) {
                        System.err.println("Error al escribir en el archivo de salida: " + e.getMessage());
                    }
                } else {
                    System.out.println(salida);
                }
            }
        }

    }
}