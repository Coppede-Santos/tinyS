
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
     * @throws ErrorTiny Si ocurre un error del compilador
     *
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
        String  assertFilePath = basePath + "/src/test/resources/statementChecking/resultado_" + fileName + ".ast.json";

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
    @DisplayName("Test del programa constructores.s")
    public  void testConstructores() throws IOException, ErrorTiny{
        testSemanticFile("constructores",true);
    }


    @Test
    @DisplayName("Test del programa metodoRedefinido.s")
    public   void testMetodoRedefinido() throws IOException, ErrorTiny {
        testSemanticFile("metodoRedefinido", true);
    }

    @Test
    @DisplayName("Test del programa metodoEstatico.s")
    public   void testMetodoEstatico() throws IOException, ErrorTiny {
        testSemanticFile("metodoEstatico", true);
    }

    // TESTS DE PROGRAMAS CON ERRORES DE DECLARACIONES

    // ClaseSinConstructorError

    /**
     *
     * @throws analizadorSemantico.Errores.ClaseSinConstructorError
     */
    @Test
    @DisplayName("Test del programa fizzBuzz.s")
    public void testFizzBuzz() throws IOException, ErrorTiny{
        testSemanticFile("fizzBuzz",false);
    }

    /**
     *
     * @throws analizadorSemantico.Errores.ClaseSinConstructorError
     */
    @Test
    @DisplayName("Test del programa asignacionInvalida.s")
    public void testAsignacionInvalida() throws IOException, ErrorTiny{
        testSemanticFile("asignacionInvalida",false);
    }

    // ClaseNoDeclaradaError

    /**
     *
     * @throws analizadorSemantico.Errores.ClaseNoDeclaradaError
     */
    @Test
    @DisplayName("Test del programa valorMaxArreglo.s")
    public void testValorMaxArreglo() throws IOException, ErrorTiny{
        testSemanticFile("valorMaxArreglo",false);
    }

    /**
     *
     * @throws analizadorSemantico.Errores.ClaseNoDeclaradaError
     */
    @Test
    @DisplayName("Test del programa dandlingElseCorrecto.s")
    public void testDandlingElseCorrecto() throws IOException, ErrorTiny{
        testSemanticFile("dandlingElseCorrecto",false);
    }

    /**
     *
     * @throws analizadorSemantico.Errores.ClaseNoDeclaradaError
     */
    @Test
    @DisplayName("Test del programa implMalFormado.s")
    public void testImplMalFormado() throws IOException, ErrorTiny{
        testSemanticFile("implMalFormado",false);
    }

    /**
     *
     * @throws analizadorSemantico.Errores.ClaseNoDeclaradaError
     */
    @Test
    @DisplayName("Test del programa classArrayInt.s")
    public   void testClassArrayInt() throws IOException, ErrorTiny{
        testSemanticFile("classArrayInt",false);
    }

    /**
     *
     * @throws analizadorSemantico.Errores.ClaseNoDeclaradaError
     */
    @Test
    @DisplayName("Test del programa ImplAntesDeClass.s")
    public   void testImplAntesDeClass() throws IOException, ErrorTiny {
        testSemanticFile("implAntesDeClass", false);
    }

    /**
     *
     * @throws analizadorSemantico.Errores.ClaseNoDeclaradaError
     */
    @Test
    @DisplayName("Test del programa ancestroInexistente.s")
    public   void testAncestroInexistente() throws IOException, ErrorTiny {
        testSemanticFile("ancestroInexistente", false);
    }

    // VariableRedefinidaError
    /**
     *
     * @throws analizadorSemantico.Errores.VariableRedefinidaError
     */
    @Test
    @DisplayName("Test del programa variableredeclarada.s")
    public   void testVariableredeclarada() throws IOException, ErrorTiny{
        testSemanticFile("variableredeclarada",false);
    }

    // ParametroRedefinidoError
    /**
     *
     * @throws analizadorSemantico.Errores.ParametroRedefinidoError
     */
    @Test
    @DisplayName("Test del programa parametroRedefinido.s")
    public   void testParametroRedefinido() throws IOException, ErrorTiny {
        testSemanticFile("parametroRedefinido", false);
    }

    // ClaseRedeclaradaError
    /**
     *
     * @throws analizadorSemantico.Errores.ClaseRedeclaradaError
     */
    @Test
    @DisplayName("Test del programa claseRedeclarada.s")
    public   void testClaseRedeclarada() throws IOException, ErrorTiny {
        testSemanticFile("claseRedeclarada", false);
    }

    /**
     *
     * @throws analizadorSemantico.Errores.ClaseRedeclaradaError
     */
    @Test
    @DisplayName("Test del programa redefinirTipoPrimitivo.s")
    public   void testRedefinirTipoPrimitivi() throws IOException, ErrorTiny {
        testSemanticFile("redefinirTipoPrimitivo", false);
    }

    // RedefinirMetodoError

    /**
     *
     * @throws analizadorSemantico.Errores.RedefinirMetodoError
     */
    @Test
    @DisplayName("Test del programa redefinirMetodo.s")
    public   void testRedefinirMetodo() throws IOException, ErrorTiny {
        testSemanticFile("redefinirMetodo", false);
    }

    // MetodoRedeclaradoError

    /**
     *
     * @throws analizadorSemantico.Errores.MetodoRedeclaradoError
     */
    @Test
    @DisplayName("Test del programa metodoDuplicado.s")
    public   void testMetodoDuplicado() throws IOException, ErrorTiny {
        testSemanticFile("metodoDuplicado", false);
    }

    // RedefinirAtributoError

    /**
     *
     * @throws analizadorSemantico.Errores.RedefinirAtributoError
     */
    @Test
    @DisplayName("Test del programa atributoHeredado.s")
    public   void testAtributoHeredado() throws IOException, ErrorTiny {
        testSemanticFile("atributoHeredado", false);
    }

    // HerenciaInvalidaError

    /**
     *
     * @throws analizadorSemantico.Errores.HerenciaInvalidaError
     */
    @Test
    @DisplayName("Test del programa herenciaInvalida.s")
    public   void testHerenciaInvalida() throws IOException, ErrorTiny {
        testSemanticFile("herenciaInvalida", false);
    }

    @Test
    @DisplayName("Test del json fibonacci.s")
    public void testFibonacciJson() throws IOException, ErrorTiny {
        testSemanticJson("fibonacci");
    }

    @Test
    @DisplayName("Test del json factorial.s")
    public void testFactorialJson() throws IOException, ErrorTiny {
        testSemanticJson("factorial");
    }

    @Test
    @DisplayName("Test del json numerosPares.s")
    public void testNumerosParesJson() throws IOException, ErrorTiny {
        testSemanticJson("numerosPares");
    }

    @Test
    @DisplayName("Test del json constructores.s")
    public void testConstructoresJson() throws IOException, ErrorTiny {
        testSemanticJson("constructores");
    }

    @Test
    @DisplayName("Test del json areaDeUnCirculo.s")
    public void testAreaDeUnCirculoJson() throws IOException, ErrorTiny {
        testSemanticJson("areaDeUnCirculo");
    }


    @Test
    @DisplayName("Test del json primo.s")
    public void testPrimoJson() throws IOException, ErrorTiny {
        testSemanticJson("primo");
    }

    @Test
    @DisplayName("Test del json metodoRedefinido.s")
    public void testMetodoRedefinidoJson() throws IOException, ErrorTiny {
        testSemanticJson("metodoRedefinido");
    }

    @Test
    @DisplayName("Test del json palindromo.s")
    public void testPalindromoJson() throws IOException, ErrorTiny {
        testSemanticJson("palindromo");
    }







}
