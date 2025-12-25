
package scripts;

import ErrorManage.ErrorTiny;
import analizadorSemantico.Etapa3;
import analizadorSintactico.Etapa2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Clase de prueba para la Etapa3 del compilador, encargada de realizar
 * la construcción de la tabla de símbolos del análisis semántico de 
 * programas escritos en lenguaje TinyS.
 * Esta clase contiene pruebas para verificar que el analizador semántico
 * funciona correctamente tanto con programas semánticamente correctos
 * como con programas que contienen errores semánticos (de declaraciones).
 */
public class TestEtapa3 {


    /**
     * Metodo auxiliar para ejecutar una prueba con un programa que contiene errores de declaraciones.
     * 
     * @param fileName Nombre del test
     * @throws IOException Si ocurre un error de E/S
     * @throws ErrorTiny Si ocurre un error del compilador
     *
     */
    private void testSemanticFile(String fileName, Boolean correcto) throws IOException, ErrorTiny {
        String basePath = System.getProperty("user.dir");
        String testFilePath = basePath + "/src/test/resources/test/" + fileName + ".s";
        String assertFilePath = basePath + "/src/test/resources/declarationChecking/resultado_correcto.txt";
        if (!correcto) {
            assertFilePath = basePath + "/src/test/resources/declarationChecking/resultado_" + fileName + ".txt";
        }
        String outputFilePath = basePath + "/src/test/resources/output/" + fileName + ".txt";

        // Ejecutar el analizador léxico
        Etapa3.main(new String[]{testFilePath, outputFilePath});

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
        testSemanticFile("factorial",true);
    }

    @Test
    @DisplayName("Test del programa numerosPares.s")
    public void testNumerosPares() throws IOException, ErrorTiny {
        testSemanticFile("numerosPares",true);
    }

    @Test
    @DisplayName("Test del programa fibonacci.s")
    public void testFibonacci() throws IOException, ErrorTiny {
        testSemanticFile("fibonacci",true);
    }


    @Test
    @DisplayName("Test del programa fibonacciSinElse.s")
    public void testFibonacciSinElse() throws IOException, ErrorTiny {
        testSemanticFile("fibonacciSinElse",true);
    }

    @Test
    @DisplayName("Test del programa areaDeUnCirculo.s")
    public void testAreaDeUnCirculo() throws IOException, ErrorTiny{
        testSemanticFile("areaDeUnCirculo",true);
    }

    @Test
    @DisplayName("Test del programa primo.s")
    public void testPrimo() throws IOException, ErrorTiny{
        testSemanticFile("primo",true);
    }

    @Test
    @DisplayName("Test del programa fizzBuzz.s")
    public void testFizzBuzz() throws IOException, ErrorTiny{
        testSemanticFile("fizzBuzz",true);
    }
    @Test
    @DisplayName("Test del programa palindromo.s")
    public void testPalindromo() throws IOException, ErrorTiny{
        testSemanticFile("palindromo",true);
    }

    @Test
    @DisplayName("Test del programa parImpar.s")
    public void testParImpar() throws IOException, ErrorTiny{
        testSemanticFile("parImpar",true);
    }

    @Test
    @DisplayName("Test del programa sentenciasIfElse.s")
    public void testSentenciasIfElse() throws IOException, ErrorTiny{
        testSemanticFile("sentenciasIfElse",true);
    }

    @Test
    @DisplayName("Test del programa valorMaxArreglo.s")
    public void testValorMaxArreglo() throws IOException, ErrorTiny{
        testSemanticFile("valorMaxArreglo",false);
    }

    @Test
    @DisplayName("Test del programa bloquesSueltos.s")
    public  void testBloquesSueltos() throws IOException, ErrorTiny{
        testSemanticFile("bloquesSueltos",true);
    }

    @Test
    @DisplayName("Test del programa dandlingElseCorrecto.s")
    public  void testDandlingElseCorrecto() throws IOException, ErrorTiny{
        testSemanticFile("dandlingElseCorrecto",true);
    }

    @Test
    @DisplayName("Test del programa whileCorrecto.s")
    public  void testWhileCorrecto() throws IOException, ErrorTiny{
        testSemanticFile("whileCorrecto",true);
    }

    @Test
    @DisplayName("Test del programa constructores.s")
    public  void testConstructores() throws IOException, ErrorTiny{
        testSemanticFile("constructores",true);
    }



    @Test
    @DisplayName("Test del programa implMalFormado.s")
    public   void testImplMalFormado() throws IOException, ErrorTiny{
        testSemanticFile("implMalFormado",false);
    }


    @Test
    @DisplayName("Test del programa asignacionInvalida.s")
    public   void testAsignacionInvalida() throws IOException, ErrorTiny{
        testSemanticFile("asignacionInvalida",true);
    }

    @Test
    @DisplayName("Test del programa classArrayInt.s")
    public   void testClassArrayInt() throws IOException, ErrorTiny{
        testSemanticFile("classArrayInt",false);
    }



}
