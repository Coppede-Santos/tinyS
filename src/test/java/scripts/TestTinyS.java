
package scripts;

import ErrorManage.ErrorTiny;
import analizadorSemantico.Etapa3;
import ast.Etapa4;
import generacionDeCodigo.TinyS;
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
public class TestTinyS {


    /**
     * Metodo auxiliar para ejecutar una prueba con un programa que contiene errores de sentencias.
     *
     * @param fileName Nombre del test
     * @throws IOException Si ocurre un error de E/S
     * @throws ErrorTiny   Si ocurre un error del compilador
     */
    private void testTinyS(String fileName, Boolean correcto) throws IOException, ErrorTiny {
        String basePath = System.getProperty("user.dir");
        String testFilePath = basePath + "/src/test/resources/test/codeGen/" + fileName + ".s";
        String assertFilePath = basePath + "/src/test/resources/codeGen/resultado_correcto.txt";
        if (!correcto) {
            assertFilePath = basePath + "/src/test/resources/codeGen/resultado_" + fileName + ".txt";
        }
        String outputFilePath = basePath + "/src/test/resources/output/" + fileName + ".txt";

        // Ejecutar el analizador léxico
        TinyS.main(new String[]{testFilePath, outputFilePath});

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
    @DisplayName("Test fibonacci.s")
    void testFibonacci() throws IOException, ErrorTiny {
        testTinyS("fibonacci", true);
    }

    @Test
    @DisplayName("Test constructorParams.s")
    void testConstructorParams() throws IOException, ErrorTiny {
        testTinyS("constructorParams", true);
    }

    @Test
    @DisplayName("Test metodosHeredados.s")
    void testMetodosHeredados() throws IOException, ErrorTiny {
        testTinyS("metodosHeredados", true);
    }

    @Test
    @DisplayName("Test factorial.s")
    void testFactorial() throws IOException, ErrorTiny {
        testTinyS("factorial", true);
    }

    @Test
    @DisplayName("Test arraysCorrecto.s")
    void testArraysCorrecto() throws IOException, ErrorTiny {
        testTinyS("arraysCorrecto", true);
    }

    // Test para expresiones.s
    @Test
    @DisplayName("Test expresiones.s")
    void testExpresiones() throws IOException, ErrorTiny {
        testTinyS("expresiones", true);
    }



}
