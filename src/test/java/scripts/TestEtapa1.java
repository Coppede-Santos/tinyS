package scripts;

import analizadorLexico.Errores.ErrorLex;
import analizadorLexico.Etapa1;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestEtapa1 {


    private void testLexicalFile(String fileName) throws IOException, ErrorLex {
        String basePath = System.getProperty("user.dir");
        String testFilePath = basePath + "/src/test/resources/test/" + fileName + ".s";
        String assertFilePath = basePath + "/src/test/resources/lexical/resultado_" + fileName + ".txt";
        String outputFilePath = basePath + "/src/test/resources/output/" + fileName + ".txt";

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
    @DisplayName("Test del programa comentarios.s")
    public void testComentarios() throws IOException, ErrorLex {
        testLexicalFile("comentarios");
    }
    @Test
    @DisplayName("Test del programa comentarios2.s")
    public void testSuma() throws IOException, ErrorLex {
        testLexicalFile("comentarios2");
    }

    @Test
    @DisplayName("Test del programa contador.s")
    public void testContador() throws IOException, ErrorLex {
        testLexicalFile("contador");
    }
    @Test
    @DisplayName("Test del programa factorial.s")
    public void testFactorial() throws IOException, ErrorLex {
        testLexicalFile("factorial");
    }
    @Test
    @DisplayName("Test del programa identificadoresErroneos.s")
    public void testIdentificadoresErroneos() throws IOException, ErrorLex {
        testLexicalFile("identificadoresErroneos");
    }
    @Test
    @DisplayName("Test del programa numerosPares.s")
    public void testNumerosPares() throws IOException, ErrorLex {
        testLexicalFile("numerosPares");
    }
    @Test
    @DisplayName("Test del programa primo.s")
    public void testprimo() throws IOException, ErrorLex {
        testLexicalFile("primo");
    }
    @Test
    @DisplayName("Test del programa buffer.s")
    public void testBuffer() throws IOException, ErrorLex {
        testLexicalFile("buffer");
    }
    @Test
    @DisplayName("Test del programa secuenciasCompletasIO.s")
    public void testsecuenciasCompletasIO() throws IOException, ErrorLex {
        testLexicalFile("secuenciasCompletasIO");
    }
    @Test
    @DisplayName("Test del programa palabrasClaves.s")
    public void testPalabrasClaves() throws IOException, ErrorLex {
        testLexicalFile("palabrasClaves");
    }
    @Test
    @DisplayName("Test del programa fibonacci.s")
    public void testFibonacci() throws IOException, ErrorLex {
        testLexicalFile("fibonacci");
    }

    @Test
    public void testSimple() throws IOException, ErrorLex {
        testLexicalFile("contador");
    }

    @Test
    @DisplayName("Test del programa archivoVacio.s")
    public void testArchivoVacio() throws IOException, ErrorLex {
        testLexicalFile("archivoVacio");
    }

    @Test
    @DisplayName("Test del programa areaDeUnCirculo.s")
    public void testAreaDeUnCirculo() throws IOException, ErrorLex {
        testLexicalFile("areaDeUnCirculo");
    }

    @Test
    @DisplayName("Test del programa cadenas.s")
    public void testCadenas() throws IOException, ErrorLex {
        testLexicalFile("cadenas");
    }

    @Test
    @DisplayName("Test del programa cadenasIncorrectas.s")
    public void testCadenasIncorrectas() throws IOException, ErrorLex {
        testLexicalFile("cadenasIncorrectas");
    }

    @Test
    @DisplayName("Test del programa calculosCombinados.s")
    public void testCalculosCombinados() throws IOException, ErrorLex {
        testLexicalFile("calculosCombinados");
    }

    @Test
    @DisplayName("Test del programa caracteresInvalidos.s")
    public void testCaracteresInvalidos() throws IOException, ErrorLex {
        testLexicalFile("caracteresInvalidos");
    }

    @Test
    @DisplayName("Test del programa comentarioSinCerrar.s")
    public void testComentarioSinCerrar() throws IOException, ErrorLex {
        testLexicalFile("comentarioSinCerrar");
    }

    @Test
    @DisplayName("Test del programa enteroInvalido.s")
    public void testEnteroInvalido() throws IOException, ErrorLex {
        testLexicalFile("enteroInvalido");
    }

    @Test
    @DisplayName("Test del programa testFibonacciConErrorEnLineaEsperada.s")
    public void testFibonacciConErrorEnLineaEsperada() throws IOException, ErrorLex {
        testLexicalFile("archivoVacio");
    }

    @Test
    @DisplayName("Test del programa metodosSeparados.s")
    public void testMetodosSeparados() throws IOException, ErrorLex {
        testLexicalFile("metodosSeparados");
    }

    @Test
    @DisplayName("Test del programa string.s")
    public void testIdentificadores() throws IOException, ErrorLex {
        testLexicalFile("string");
    }

    @Test
    @DisplayName("Test del programa identificadores.s")
    public void testLiterales() throws IOException, ErrorLex {
        testLexicalFile("identificadores");
    }
    @Test
    @DisplayName("Test del programa identificadoresErroneos2.s")
    public void testLiterales2() throws IOException, ErrorLex {
        testLexicalFile("identificadoresErroneos2");
    }

    @Test
    @DisplayName("Test del programa identificadoresErroneos3.s")
    public void testIdentificadoresErroneos3() throws IOException, ErrorLex {
        testLexicalFile("identificadoresErroneos3");
    }

    @Test
    @DisplayName("Test del programa identificadoresErroneos4.s")
    public void testIdentificadoresErroneos2() throws IOException, ErrorLex {
        testLexicalFile("identificadoresErroneos4");
    }

    @Test
    @DisplayName("Test del programa operadores.s")
    public void testOperadores() throws IOException, ErrorLex{
        testLexicalFile("operadores");
    }
    @Test
    @DisplayName("Test del programa romperPalabrasClaves.s")
    public void testromperPalabrasClaves() throws IOException, ErrorLex{
        testLexicalFile("romperPalabrasClaves");
    }

    @Test
    @DisplayName("Test del programa strSinTerminar.s")
    public void testStrSinTerminar() throws IOException, ErrorLex{
        testLexicalFile("strSinTerminar");
    }

    @Test
    @DisplayName("Test del programa strSinTerminar2.s")
    public void testStrSinTerminar2() throws IOException, ErrorLex{
        testLexicalFile("strSinTerminar2");
    }




}
