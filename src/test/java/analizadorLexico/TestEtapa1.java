package analizadorLexico;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestEtapa1 {

    private void testLexicalFile(String fileName) throws IOException, ErrorLex {
        String basePath = System.getProperty("user.dir");
        String testFilePath = basePath + "/src/test/resources/test/" + fileName + ".s";
        String assertFilePath = basePath + "/src/test/resources/lexical/resultado_" + fileName + ".txt";
        String outputFilePath = basePath + "/src/test/resources/test/" + fileName + ".txt";

        // Ejecutar el analizador léxico
        Etapa1.main(new String[]{testFilePath, outputFilePath});

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
    public void testTotal() throws IOException, ErrorLex{
        String[] test = {"comentarios",
                "contador",
                "factorial",
                "identificadoresErroneos",
                "numerosPares",
                "primo",
                "testBuffer",
                "testPalabrasClaves",
                "secuenciasCompletasIO",
                "testFibonacci"};
        for (String i : test) {
            testLexicalFile(i);
        }
    }

    @Test
    public void testSimple() throws IOException, ErrorLex {
        testLexicalFile("contador");
    }
}
