
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
    private void testTinyS(String fileName) throws IOException, ErrorTiny {
        String basePath = System.getProperty("user.dir");
        String testFilePath = basePath + "/src/test/resources/test/codeGen/" + fileName + ".s";

        String assertFilePath = basePath + "/src/test/resources/codeGen/resultado_" + fileName + ".asm";

        String outputFilePath = basePath + "/src/test/resources/test/codeGen/" + fileName + ".asm";

        // Ejecutar el analizador léxico
        TinyS.main(new String[]{testFilePath});

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
        testTinyS("fibonacci");
    }

    @Test
    @DisplayName("Test constructorParams.s")
    void testConstructorParams() throws IOException, ErrorTiny {
        testTinyS("constructorParams");
    }

    @Test
    @DisplayName("Test metodosHeredados.s")
    void testMetodosHeredados() throws IOException, ErrorTiny {
        testTinyS("metodosHeredados");
    }

    @Test
    @DisplayName("Test factorial.s")
    void testFactorial() throws IOException, ErrorTiny {
        testTinyS("factorial");
    }

    @Test
    @DisplayName("Test arraysCorrecto.s")
    void testArraysCorrecto() throws IOException, ErrorTiny {
        testTinyS("arraysCorrecto");
    }

    // Test para expresiones.s
    @Test
    @DisplayName("Test expresiones.s")
    void testExpresiones() throws IOException, ErrorTiny {
        testTinyS("expresiones");
    }

    @Test
    @DisplayName("Test stresArray.s")
    void testStresArray() throws IOException, ErrorTiny {
        testTinyS("stresArray");
    }

    @Test
    @DisplayName("Test condicionales.s")
    void testcondicionales() throws IOException, ErrorTiny {
        testTinyS("condicionales");
    }

    @Test
    @DisplayName("Test contador.s")
    void testcontador() throws IOException, ErrorTiny {
        testTinyS("contador");
    }

    @Test
    @DisplayName("Test ifAnidado.s")
    void testifAnidado() throws IOException, ErrorTiny {
        testTinyS("ifAnidado");
    }

    @Test
    @DisplayName("Test banco.s")
    void testBanco() throws IOException, ErrorTiny {
        testTinyS("banco");
    }

    @Test
    @DisplayName("Test nilTest.s")
    void testNilTest() throws IOException, ErrorTiny {
        testTinyS("nilTest");
    }

    @Test
    @DisplayName("Test atributoPrivado.s")
    void testAtributoPrivado() throws IOException, ErrorTiny {
        testTinyS("atributoPrivado");
    }

    @Test
    @DisplayName("Test atributoPub.s")
    void testAtributoPub() throws IOException, ErrorTiny {
        testTinyS("atributoPub");
    }

    @Test
    @DisplayName("Test metodoEstatico.s")
    void testMetodoEstatico() throws IOException, ErrorTiny {
        testTinyS("metodoEstatico");
    }

    @Test
    @DisplayName("Test probarNil.s")
    void testProbarNil() throws IOException, ErrorTiny {
        testTinyS("probarNil");
    }

    @Test
    @DisplayName("Test divisionPorCero.s")
    void testDivisionPorCero() throws IOException, ErrorTiny {
        testTinyS("divisionPorCero");
    }


    @Test
    @DisplayName("Test ArrayNegativo.s")
    void testArrayNegativo() throws IOException, ErrorTiny {
        testTinyS("ArrayNegativo");
    }

    @Test
    @DisplayName("Test muchasImpl.s")
    void testMuchasImpl() throws IOException, ErrorTiny {
        testTinyS("muchasImpl");
    }

    @Test
    @DisplayName("Test start.s")
    void testStart() throws IOException, ErrorTiny {
        testTinyS("start");
    }

    @Test
    @DisplayName("Test IOObject.s")
    void testIOObject() throws IOException, ErrorTiny {
        testTinyS("IOObject");
    }
    @Test
    @DisplayName("Test metodos_arreglos.s")
    void testMetodosArreglos() throws IOException, ErrorTiny {
        testTinyS("metodos_arreglos");
    }

    @Test
    @DisplayName("Test expresionesString.s")
    void testExpresionesString() throws IOException, ErrorTiny {
        testTinyS("expresionesString");
    }

    @Test
    @DisplayName("Test a-bubbleSort.s")
    void testBubbleSort() throws IOException, ErrorTiny {
        testTinyS("a-bubbleSort");
    }

    @Test
    @DisplayName("Test a-bubbleSortDouble.s")
    void testBubbleSortDouble() throws IOException, ErrorTiny {
        testTinyS("a-bubbleSortDouble");
    }

    @Test
    @DisplayName("Test LinkedList.s")
    void testLinkedList() throws IOException, ErrorTiny {
        testTinyS("LinkedList");
    }

    @Test
    @DisplayName("Test atributos.s")
    void testAtributos() throws IOException, ErrorTiny {
        testTinyS("atributos");
    }

}
