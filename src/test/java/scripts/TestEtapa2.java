
package scripts;

import ErrorManage.ErrorTiny;
import analizadorSintactico.Etapa2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
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


    /**
     * Metodo auxiliar para ejecutar una prueba con un programa que contiene errores sintácticos.
     * 
     * @param fileName Nombre del test
     * @throws IOException Si ocurre un error de E/S
     * @throws ErrorTiny Si ocurre un error del compilador
     *
     */
    private void testSintaxFile(String fileName, Boolean correcto) throws IOException, ErrorTiny {
        String basePath = System.getProperty("user.dir");
        String testFilePath = basePath + "/src/test/resources/test/" + fileName + ".s";
        String assertFilePath = basePath + "/src/test/resources/sintax/resultado_correcto.txt";
        if (!correcto) {
            assertFilePath = basePath + "/src/test/resources/sintax/resultado_" + fileName + ".txt";
        }
        String outputFilePath = basePath + "/src/test/resources/output/" + fileName + ".txt";

        // Ejecutar el analizador léxico
        Etapa2.main(new String[]{testFilePath, outputFilePath});

        // Leer el archivo generado
        String actualOutput = Files.readString(Paths.get(outputFilePath));

        // Leer el archivo esperado
        String expectedOutput = Files.readString(Paths.get(assertFilePath));

        // Comparar línea por línea
        String[] actualLines = actualOutput.split("\n");
        String[] expectedLines = expectedOutput.split("\n");

        assertEquals(expectedLines.length, actualLines.length,
                "La cantidad de líneas no coincide para " + fileName);

        for (int i = 0; i < expectedLines.length; i++) {
            assertEquals(expectedLines[i].trim(), actualLines[i].trim(),
                    "Error en la línea " + (i + 1) + " del archivo " + fileName);
        }
    }


    @Test
    @DisplayName("Test del programa factorial.s")
    public void testFactorial() throws IOException, ErrorTiny {
        testSintaxFile("factorial",true);
    }

    @Test
    @DisplayName("Test del programa numerosPares.s")
    public void testNumerosPares() throws IOException, ErrorTiny {
        testSintaxFile("numerosPares",true);
    }

    @Test
    @DisplayName("Test del programa fibonacci.s")
    public void testFibonacci() throws IOException, ErrorTiny {
        testSintaxFile("fibonacci",true);
    }
    @Test
    @DisplayName("Test del programa fibonacciSinCerrar.s")
    public void testFibonacciSinCerrar() throws IOException, ErrorTiny {
        testSintaxFile("fibonacciSinCerrar",false);
    }

    @Test
    @DisplayName("Test del programa fibonacciSinElse.s")
    public void testFibonacciSinElse() throws IOException, ErrorTiny {
        testSintaxFile("fibonacciSinElse",false);
    }

    @Test
    @DisplayName("Test del programa fibonacciSinImpl.s")
    public void testFibonacciSinImpl() throws IOException, ErrorTiny {
        testSintaxFile("fibonacciSinImpl",false);
    }

    @Test
    @DisplayName("Test del programa fibonacciErrorExp.s")
    public void testFibonacciErrorExp() throws IOException, ErrorTiny {
        testSintaxFile("fibonacciErrorExp",false);
    }

    @Test
    @DisplayName("Test del programa fibonacciSinAsignar.s")
    public void testFibonacciSinAsignar() throws IOException, ErrorTiny {
        testSintaxFile("fibonacciSinAsignar",false);
    }



    @Test
    @DisplayName("Test del programa areaDeUnCirculo.s")
    public void testAreaDeUnCirculo() throws IOException, ErrorTiny{
        testSintaxFile("areaDeUnCirculo",true);
    }

    @Test
    @DisplayName("Test del programa primo.s")
    public void testPrimo() throws IOException, ErrorTiny{
        testSintaxFile("primo",true);
    }








}
