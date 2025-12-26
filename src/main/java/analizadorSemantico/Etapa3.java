package analizadorSemantico;

import analizadorLexico.Errores.ErrorLex;
import analizadorLexico.Escaner;
import analizadorLexico.LectorCF;
import analizadorLexico.Token;
import analizadorSemantico.Errores.ErrorSemantico;
import analizadorSintactico.Errores.ErrorSintactico;
import analizadorSintactico.Parser;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


/**
 * La clase {@code Etapa2} realiza el análisis sintáctico y léxico de un archivo de entrada en lenguaje TinyS.
 * <p>
 * Esta clase coordina el uso del analizador léxico ({@link Escaner}) y el analizador sintáctico ({@link Parser})
 * para verificar la validez del código fuente TinyS proporcionado en un archivo de entrada.
 * </p>
 */

public class Etapa3 {


    /**
     * Metodo principal que ejecuta el análisis sintáctico y léxico, y chequeo de declaraciones del archivo de entrada.
     *
     * @param args Un array de argumentos de línea de comandos.
     *             Se espera que el primer argumento sea la ruta al archivo de entrada TinyS (con extensión .s).
     *             Opcionalmente, el segundo argumento puede ser la ruta al archivo de salida donde se escribirán los resultados.
     *             Si no se proporciona un archivo de salida, se utilizará un nombre basado en el archivo de entrada,
     *             cambiando la extensión a .txt.
     */
    public static void main(String[] args) {
        // ────────────── Validación de argumentos ──────────────
        if (args.length < 1 || args.length > 2) {
            System.out.println("Uso: java analizadorSemantico.Etapa3 <archivo_entrada.s>");
            return;
        }

        String rutaArchivoEntrada = args[0];
        if (!rutaArchivoEntrada.endsWith(".s")) {
            System.out.println("El archivo de entrada debe tener extensión '.s'.");
            return;
        }

        String nombreArchivoSalida = args.length == 2
                ? args[1]
                : rutaArchivoEntrada.replace(".s", ".txt");

        String resultadoAnalisis;

        try {
            // ────────────── Preparación de componentes ──────────────
            LectorCF lector = new LectorCF();
            Escaner escaner = new Escaner();
            Parser parser = new Parser();


            lector.lectorArchivo(rutaArchivoEntrada);
            String source = lector.rechargeBuffer();
            escaner.setBuffer(source);
            escaner.setEscaner(lector);
            parser.setEscaner(escaner);

            // ────────────── Análisis ──────────────
            Token firstToken = escaner.nextToken();
            parser.setCurrentToken(firstToken);

            // Se ejecuta el parser.
            if (parser.s()) {
                resultadoAnalisis = "CORRECTO: SEMANTICO - DECLARACIONES\n";
            } else {
                // Este caso maneja una falla sin excepción, que podría indicar un parseo incompleto.
                resultadoAnalisis = "ERROR: SEMANTICO - DECLARACIONES\n";
            }

        } catch (ErrorSintactico e) {
            resultadoAnalisis = "ERROR: SINTACTICO\n" + e.getMessage();
        } catch (ErrorLex e) {
            resultadoAnalisis = "ERROR: LEXICO\n" + e.getMessage();
        } catch (ErrorSemantico e) {
            resultadoAnalisis = "ERROR: SEMANTICO - DECLARACIONES\n" + e.getMessage();
        } catch (IOException e) {
            // Este error ocurre si hay problemas leyendo el archivo de entrada.
            System.err.println("Error de E/S al leer el archivo de entrada: " + e.getMessage());
            return;
        } catch (Exception e) {
            // Captura cualquier otro error inesperado durante el análisis.
            resultadoAnalisis = "ERROR: INESPERADO\nSe ha producido un error no controlado durante el análisis.\n" + e.getMessage();
            e.printStackTrace();
        }

        // ────────────── Escritura de resultados ──────────────
        // Este bloque ahora solo se encarga de escribir el resultado final en el archivo.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivoSalida))) {
            writer.write(resultadoAnalisis);
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo de salida '" + nombreArchivoSalida + "': " + e.getMessage());
        }

        if (resultadoAnalisis.startsWith("ERROR")) {
            System.err.println(resultadoAnalisis.trim());
        } else {
            System.out.println(resultadoAnalisis.trim());

        }
    }

    /**
     * Función auxiliar para escribir un mensaje de error en el archivo de salida, reemplazando cualquier contenido previo.
     *
     * @param writer  El {@link BufferedWriter} utilizado para escribir en el archivo.
     * @param archivo La ruta del archivo en el que se escribirá el mensaje de error.
     * @param mensaje El mensaje de error que se escribirá en el archivo.
     * @throws IOException Si ocurre un error de entrada/salida durante la escritura en el archivo.
     */

    private static void reemplazarConError(BufferedWriter writer, String archivo, String mensaje) throws IOException {
        if (writer != null) writer.close();
        try (BufferedWriter newWriter = new BufferedWriter(new FileWriter(archivo, false))) {
            newWriter.write(mensaje);
        }
        System.err.println(mensaje.trim());
    }

}
