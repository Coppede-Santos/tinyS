package analizadorSintactico;

import analizadorLexico.*;

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


public class Etapa2 {
    static Parser parser = new Parser();
    static Escaner escaner = new Escaner();
    static LectorCF lector = new LectorCF();


    /**
     * Método principal que ejecuta el análisis sintáctico y léxico del archivo de entrada.
     *
     * @param args Un array de argumentos de línea de comandos.
     *             Se espera que el primer argumento sea la ruta al archivo de entrada TinyS (con extensión .s).
     *             Opcionalmente, el segundo argumento puede ser la ruta al archivo de salida donde se escribirán los resultados.
     *             Si no se proporciona un archivo de salida, se utilizará un nombre basado en el archivo de entrada,
     *             cambiando la extensión a .txt.
     * @throws IOException Si ocurre un error de entrada/salida durante la lectura o escritura de archivos.
     */

    public static void main(String[] args) throws IOException {
        escaner.setEscaner(lector);

        // ────────────── Validación de argumentos ──────────────
        if (args.length < 1 || args.length > 2) {
            System.out.println("Uso: java analizadorLexico.Etapa2 <archivo_entrada.s> [archivo_salida.txt]");
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

        // ────────────── Preparar analizador ──────────────
        lector.lectorArchivo(rutaArchivoEntrada);
        String source = lector.rechargeBuffer();
        escaner.setBuffer(source);
        parser.setEscaner(escaner);

        // ────────────── Procesamiento ──────────────
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivoSalida))) {
            try {

                Token firstToken = escaner.nextToken();
                parser.setCurrentToken(firstToken);

                try {
                    if (parser.s()) {
                        writer.write("CORRECTO: ANALISIS LEXICO\n");
                    }
                } catch (ErrorSintactico e) {
                    reemplazarConError(writer, nombreArchivoSalida, "ERROR: SINTACTICO\n" + e.getMessage());
                } catch (ErrorLex e) {
                    reemplazarConError(writer, nombreArchivoSalida, "ERROR: LEXICO\n" + e.getMessage());
                }

            } catch (IOException e) {
                System.err.println("Error al abrir o escribir en el archivo: " + e.getMessage());
            } catch (ErrorLex e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            System.err.println("Error al cerrar el archivo de salida: " + e.getMessage());
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

    // Función auxiliar para escribir un mensaje de error, reemplazando el archivo
    private static void reemplazarConError(BufferedWriter writer, String archivo, String mensaje) throws IOException {
        if (writer != null) writer.close();
        try (BufferedWriter newWriter = new BufferedWriter(new FileWriter(archivo, false))) {
            newWriter.write(mensaje);
        }
        System.err.println(mensaje.trim());
    }

}
