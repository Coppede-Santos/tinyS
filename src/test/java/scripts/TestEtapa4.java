
package scripts;

import ErrorManage.ErrorTiny;
import analizadorSemantico.Etapa3;
import ast.Etapa4;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Clase de prueba para la Etapa4 del compilador, encargada de realizar
 * la construcción del árbol sintáctico abstracto del análisis semántico de
 * programas escritos en lenguaje TinyS.
 * Esta clase contiene pruebas para verificar que el analizador semántico
 * funciona correctamente tanto con programas semánticamente correctos
 * como con programas que contienen errores semánticos (de sentencias).
 */
public class TestEtapa4 {


    /**
     * Metodo auxiliar para ejecutar una prueba con un programa que contiene errores de sentencias.
     *
     * @param fileName Nombre del test
     * @throws IOException Si ocurre un error de E/S
     * @throws ErrorTiny   Si ocurre un error del compilador
     */
    private void testSemanticFile(String fileName, Boolean correcto) throws IOException, ErrorTiny {
        String basePath = System.getProperty("user.dir");
        String testFilePath = basePath + "/src/test/resources/test/" + fileName + ".s";
        String assertFilePath = basePath + "/src/test/resources/statementChecking/resultado_correcto.txt";
        if (!correcto) {
            assertFilePath = basePath + "/src/test/resources/statementChecking/resultado_" + fileName + ".txt";
        }
        String outputFilePath = basePath + "/src/test/resources/output/" + fileName + ".txt";

        // Ejecutar el analizador léxico
        Etapa4.main(new String[]{testFilePath, outputFilePath});

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

    private void testSemanticJson(String fileName) throws IOException, ErrorTiny {
        String basePath = System.getProperty("user.dir");
        String testFilePath = basePath + "/src/test/resources/test/" + fileName + ".s";
        String assertFilePath = basePath + "/src/test/resources/statementChecking/resultado_" + fileName + ".ast.json";

        String outputFilePath = basePath + "/src/test/resources/test/" + fileName + ".ast.json";

        // Ejecutar el analizador léxico
        Etapa4.main(new String[]{testFilePath, outputFilePath});

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

    // Tests de programas correctos

    @Test
    @DisplayName("Test del programa factorial.s")
    public void testFactorial() throws IOException, ErrorTiny {
        testSemanticFile("factorial", true);
        testSemanticJson("factorial");
    }

    @Test
    @DisplayName("Test del programa numerosPares.s")
    public void testNumerosPares() throws IOException, ErrorTiny {
        testSemanticFile("numerosPares", true);
        testSemanticJson("numerosPares");
    }

    @Test
    @DisplayName("Test del programa fibonacci.s")
    public void testFibonacci() throws IOException, ErrorTiny {
        testSemanticFile("fibonacci", true);
        testSemanticJson("fibonacci");
    }


    @Test
    @DisplayName("Test del programa fibonacciSinElse.s")
    public void testFibonacciSinElse() throws IOException, ErrorTiny {
        testSemanticFile("fibonacciSinElse", true);
        testSemanticJson("fibonacciSinElse");
    }

    @Test
    @DisplayName("Test del programa areaDeUnCirculo.s")
    public void testAreaDeUnCirculo() throws IOException, ErrorTiny {
        testSemanticFile("areaDeUnCirculo", true);
        testSemanticJson("areaDeUnCirculo");
    }

    @Test
    @DisplayName("Test del programa primo.s")
    public void testPrimo() throws IOException, ErrorTiny {
        testSemanticFile("primo", true);
        testSemanticJson("primo");
    }

    @Test
    @DisplayName("Test del programa constructores.s")
    public void testConstructores() throws IOException, ErrorTiny {
        testSemanticFile("constructores", true);
        testSemanticJson("constructores");
    }


    @Test
    @DisplayName("Test del programa metodoRedefinido.s")
    public void testMetodoRedefinido() throws IOException, ErrorTiny {
        testSemanticFile("metodoRedefinido", true);
        testSemanticJson("metodoRedefinido");
    }

    @Test
    @DisplayName("Test del programa metodoEstatico.s")
    public void testMetodoEstatico() throws IOException, ErrorTiny {
        testSemanticFile("metodoEstatico", true);
        testSemanticJson("metodoEstatico");
    }

    // TESTS DE PROGRAMAS CON ERRORES DE SENTENCIAS

    @Test
    @DisplayName("Test del programa paramNoDeclarado.s")
    public void testParamNoDeclarado() throws IOException, ErrorTiny {
        testSemanticFile("paramNoDeclarado", false);
    }

    @Test
    @DisplayName("Test del programa fnNoEstatico.s")
    public void testFnNoEstatico() throws IOException, ErrorTiny {
        testSemanticFile("fnNoEstatico", false);
    }

    @Test
    @DisplayName("Test del programa indiceInvalido.s")
    public void testIndiceInvalido() throws IOException, ErrorTiny {
        testSemanticFile("indiceInvalido", false);
    }

    @Test
    @DisplayName("Test del programa malAccesoAtr.s")
    public void testMalAccesoAtr() throws IOException, ErrorTiny {
        testSemanticFile("malAccesoAtr", false);
    }

    @Test
    @DisplayName("Test del programa malAccesoAtr2.s")
    public void testMalAccesoAtr2() throws IOException, ErrorTiny {
        testSemanticFile("malAccesoAtr2", false);
    }

    @Test
    @DisplayName("Test del programa malOrdenParams.s")
    public void testMalOrdenParams() throws IOException, ErrorTiny {
        testSemanticFile("malOrdenParams", false);
    }

    @Test
    @DisplayName("Test del programa metodoInexistente.s")
    public void testMetodoInexistente() throws IOException, ErrorTiny {
        testSemanticFile("metodoInexistente", false);
    }

    @Test
    @DisplayName("Test del programa retTipoInvalido.s")
    public void testRetTipoInvalido() throws IOException, ErrorTiny {
        testSemanticFile("retTipoInvalido", false);
    }


    @Test
    @DisplayName("Test del programa asignacionNill.s")
    public void testAsignacionNill() throws IOException, ErrorTiny {
        testSemanticFile("asignacionNill", true);
    }


}
