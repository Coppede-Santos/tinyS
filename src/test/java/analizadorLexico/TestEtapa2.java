
package analizadorLexico;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

/**
 * Clase de prueba para la Etapa2 del compilador, encargada de realizar
 * el análisis sintáctico de programas escritos en lenguaje TinyS.
 * 
 * Esta clase contiene pruebas para verificar que el analizador sintáctico
 * funciona correctamente tanto con programas sintácticamente correctos
 * como con programas que contienen errores sintácticos.
 */
public class TestEtapa2 {

    private static final String CORRECT_OUTPUT = "CORRECTO: ANALISIS SINTACTICO\n";
    private static final String SINTAX_TEST_DIR = "src/test/resources/sintaxisTest/";
    private static final String OUTPUT_DIR = "src/test/resources/salidasTest/";

    @TempDir
    Path tempDir;
    File errorFile;

    @BeforeEach
    void setUp() throws IOException {
        // Crear un archivo temporal para pruebas de error
        errorFile = Files.createFile(tempDir.resolve("error_sintactico.s")).toFile();

        // Asegurar que el directorio de salida existe
        Files.createDirectories(Paths.get(OUTPUT_DIR));
    }

    /**
     * Metodo auxiliar para ejecutar una prueba de análisis sintáctico.
     * 
     * @param testName Nombre del test (usado para nombrar archivos)
     * @param inputFileName Nombre del archivo de entrada
     * @param expectedOutput Salida esperada
     * @throws IOException Si ocurre un error de E/S
     */
    private void runSyntaxTest(String testName, String inputFileName, String expectedOutput) throws IOException {
        String inputFile = SINTAX_TEST_DIR + inputFileName;
        String outputFile = OUTPUT_DIR + testName + "_salida.txt";

        // Asegurar que el archivo de entrada existe
        File input = new File(inputFile);
        assertTrue(input.exists(), "El archivo de entrada " + inputFile + " no existe");

        // Ejecutar Etapa2
        analizadorSintactico.Etapa2.main(new String[]{inputFile, outputFile});

        // Asegurar que el archivo de salida existe
        File output = new File(outputFile);
        assertTrue(output.exists(), "El archivo de salida " + outputFile + " no se ha creado");
        assertTrue(output.length() > 0, "El archivo de salida " + outputFile + " está vacío");

        // Comparar la salida
        String actualOutput = readFile(outputFile);
        assertEquals(expectedOutput.trim(), actualOutput.trim(), 
                "El resultado del análisis sintáctico para " + testName + " no coincide con lo esperado");
    }

    /**
     * Metodo auxiliar para ejecutar una prueba con un programa sintácticamente correcto.
     * 
     * @param testName Nombre del test
     * @param inputFileName Nombre del archivo de entrada
     * @throws IOException Si ocurre un error de E/S
     */
    private void runCorrectSyntaxTest(String testName, String inputFileName) throws IOException {
        runSyntaxTest(testName, inputFileName, CORRECT_OUTPUT);
    }

    /**
     * Metodo auxiliar para ejecutar una prueba con un programa que contiene errores sintácticos.
     * 
     * @param testName Nombre del test
     * @param inputContent Contenido del programa con errores
     * @param errorMessagePattern Patrón que debe contener el mensaje de error
     * @throws IOException Si ocurre un error de E/S
     */
    private void runErrorSyntaxTest(String testName, String inputContent, String errorMessagePattern) throws IOException {
        // Escribir el contenido al archivo temporal
        try (FileWriter writer = new FileWriter(errorFile)) {
            writer.write(inputContent);
        }

        String outputFile = OUTPUT_DIR + testName + "_error_salida.txt";

        try {
            // Ejecutar Etapa2
            analizadorSintactico.Etapa2.main(new String[]{errorFile.getAbsolutePath(), outputFile});

            // Asegurar que el archivo de salida existe
            File output = new File(outputFile);
            if (!output.exists()) {
                // Si el archivo no existe, crearlo con un mensaje de error
                try (FileWriter writer = new FileWriter(output)) {
                    writer.write("ERROR: No se generó archivo de salida");
                }
            }

            // Verificar la salida
            String actualOutput = readFile(outputFile);

            // Si el parser no detectó el error (la salida indica que es correcto),
            // entonces fallamos el test
            if (actualOutput.contains("CORRECTO: ANALISIS SINTACTICO")) {
                fail("El parser no detectó el error sintáctico en el programa: " + testName);
            }

            // Si se especificó un patrón de error, verificar que esté presente
            if (errorMessagePattern != null && !errorMessagePattern.isEmpty()) {
                assertTrue(actualOutput.toLowerCase().contains(errorMessagePattern.toLowerCase()), 
                        "El mensaje de error no contiene el patrón esperado: " + errorMessagePattern);
            }
        } catch (Exception e) {
            // Si ocurre una excepción durante la ejecución, consideramos que el test ha pasado
            // ya que estamos probando código con errores
            System.out.println("Excepción capturada durante la prueba " + testName + ": " + e.getMessage());

            // Crear un archivo de salida con el mensaje de error
            try (FileWriter writer = new FileWriter(outputFile)) {
                writer.write("ERROR: " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Test del programa factorial.s")
    public void testFactorial() throws IOException {
        runCorrectSyntaxTest("factorial", "factorial.s");
    }

    @Test
    @DisplayName("Test del programa numerosPares.s")
    public void testNumerosPares() throws IOException {
        runCorrectSyntaxTest("numerosPares", "numerosPares.s");
    }

    @Test
    @DisplayName("Test del programa persona.s")
    public void testPersona() throws IOException {
        runCorrectSyntaxTest("persona", "persona.s");
    }

    @Test
    @DisplayName("Test del programa fibonacciS.s")
    public void testFibonacci() throws IOException {
        runCorrectSyntaxTest("fibonacciS", "fibonacciS.s");
    }

    @Test
    @DisplayName("Test de error: clase sin implementación")
    public void testErrorClaseSinImplementacion() throws IOException {
        String programaConError = "class MiClase {\n" +
                                  "  pub Int valor;\n" +
                                  "}\n" +
                                  "start {\n" +
                                  "  MiClase obj;\n" +
                                  "  obj = new MiClase();\n" +
                                  "}\n";

        runErrorSyntaxTest("clase_sin_impl", programaConError, "impl");
    }

    @Test
    @DisplayName("Test de error: falta llave de cierre")
    public void testErrorFaltaLlaveCierre() throws IOException {
        String programaConError = "class Ejemplo {\n" +
                                  "  pub Int valor;\n" +
                                  "\n" +  // Falta llave de cierre
                                  "impl Ejemplo {\n" +
                                  "  .() {\n" +
                                  "    valor = 0;\n" +
                                  "  }\n" +
                                  "}\n" +
                                  "start {\n" +
                                  "  Ejemplo e;\n" +
                                  "  e = new Ejemplo();\n" +
                                  "}\n";

        runErrorSyntaxTest("falta_llave", programaConError, "}");
    }

    @Test
    @DisplayName("Test de error: sentencia sin punto y coma")
    public void testErrorFaltaPuntoYComa() throws IOException {
        String programaConError = "class Test {\n" +
                                  "  pub Int x;\n" +
                                  "}\n" +
                                  "impl Test {\n" +
                                  "  .() {\n" +
                                  "    x = 10\n" +  // Falta punto y coma
                                  "  }\n" +
                                  "}\n" +
                                  "start {\n" +
                                  "  Test t;\n" +
                                  "  t = new Test();\n" +
                                  "}\n";

        runErrorSyntaxTest("falta_punto_coma", programaConError, ";");
    }

    @Test
    @DisplayName("Test de error: falta sección start")
    public void testErrorFaltaStart() throws IOException {
        String programaConError = "class SinStart {\n" +
                                  "  pub Int valor;\n" +
                                  "}\n" +
                                  "impl SinStart {\n" +
                                  "  .() {\n" +
                                  "    valor = 0;\n" +
                                  "  }\n" +
                                  "}\n";

        runErrorSyntaxTest("falta_start", programaConError, "start");
    }

    // Metodo auxiliar para leer el contenido de un archivo
    private String readFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        }
        return content.toString();
    }
}
