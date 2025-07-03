package analizadorLexico;// TestEtapa2.java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;

public class TestEtapa2 {

    @Test
    public void testFactorial() throws IOException {
        String inputFile = "src/test/resources/sintaxisTest/factorial.s";
        String outputFile = "src/test/resources/salidasTest/factorial_salida.txt";
        //String expectedOutputFile = "salidasTest/factorial_expected.txt";

        // Ejecutar Etapa2
        analizadorSintactico.Etapa2.main(new String[]{inputFile, outputFile});

        // Comparar la salida
        String actualOutput = readFile(outputFile);
        //String expectedOutput = readFile(expectedOutputFile);
        String expectedOutput = "CORRECTO: ANALISIS SINTACTICO\n";

        assertEquals(expectedOutput.trim(), actualOutput.trim(), "La salida no coincide");
    }
    @Test
    public void testNumerosPares() throws IOException {
        String inputFile = "src/test/resources/sintaxisTest/numerosPares.s";
        String outputFile = "src/test/resources/salidasTest/numerosPares_salida.txt";
        //String expectedOutputFile = "salidasTest/factorial_expected.txt";

        // Ejecutar Etapa2
        analizadorSintactico.Etapa2.main(new String[]{inputFile, outputFile});

        // Comparar la salida
        String actualOutput = readFile(outputFile);
        //String expectedOutput = readFile(expectedOutputFile);
        String expectedOutput = "CORRECTO: ANALISIS SINTACTICO\n";

        assertEquals(expectedOutput.trim(), actualOutput.trim(), "La salida no coincide");
    }

    @Test
    public void testPersona() throws IOException {
        String inputFile = "src/test/resources/sintaxisTest/persona.s";
        String outputFile = "src/test/resources/salidasTest/persona_salida.txt";
        //String expectedOutputFile = "salidasTest/factorial_expected.txt";

        // Ejecutar Etapa2
        analizadorSintactico.Etapa2.main(new String[]{inputFile, outputFile});

        // Comparar la salida
        String actualOutput = readFile(outputFile);
        //String expectedOutput = readFile(expectedOutputFile);
        String expectedOutput = "CORRECTO: ANALISIS SINTACTICO\n";

        assertEquals(expectedOutput.trim(), actualOutput.trim(), "La salida no coincide");
    }

    @Test
    public void testFibonacci() throws IOException {
        String inputFile = "src/test/resources/sintaxisTest/fibonacciS.s";
        String outputFile = "src/test/resources/salidasTest/fibonacciS_salida.txt";
        //String expectedOutputFile = "salidasTest/factorial_expected.txt";

        // Ejecutar Etapa2
        analizadorSintactico.Etapa2.main(new String[]{inputFile, outputFile});

        // Comparar la salida
        String actualOutput = readFile(outputFile);
        //String expectedOutput = readFile(expectedOutputFile);
        String expectedOutput = "CORRECTO: ANALISIS SINTACTICO\n";

        assertEquals(expectedOutput.trim(), actualOutput.trim(), "La salida no coincide");
    }

    // Método auxiliar para leer el contenido de un archivo
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