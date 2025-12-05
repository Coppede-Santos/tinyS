package scripts;


import analizadorLexico.Errores.ErrorLex;
import org.junit.jupiter.api.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Nested
public class TestUnit {


    Escaner escaner = new Escaner();
    LectorCF lector = new LectorCF();
    /*
    @BeforeAll
    static void beforeAll() {
        System.out.println("Ejecución de pruebas iniciada");
    }

    // Configuración inicial (si es necesario)
    @BeforeEach
    void setUp() {
        // Inicialización de objetos o dependencias necesarias antes de cada prueba.
    }

     */

    @Test
    void testCasoExitoso() throws IOException, ErrorLex {
        String source;
        escaner.setEscaner(lector);
        lector.lectorArchivo("C:\\Users\\nacho\\IdeaProjects\\tinyS/src/test/resources/lexicalTest/comentarios.s");
        source = lector.rechargeBuffer();
        escaner.setBuffer(source);

        List<Token> tokens = new ArrayList<>();
        Token tokenActual;

        do {
            tokenActual = escaner.nextToken();
            tokens.add(tokenActual);

        } while (tokenActual.getType() != TokenType.EOF);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("tokens_output.txt"))) {

            for (Token i : tokens) {
                String tokenString = i.toString();

                //System.out.println("TokenType."+ i.getType() +",");
                System.out.println(tokenString);


                writer.write(tokenString);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    @Test
    void testPrueba () throws IOException, ErrorLex{
        // Obtener la ruta del archivo de recursos usando el ClassLoader
        String resourcePath = getClass().getClassLoader()
                .getResource("test/palabrasClaves.s")
                .getPath();

        // Ruta del archivo de salida (puedes cambiarla)
        String outputPath = "src/test/resources/lexicalTest/assert/resultado_palabrasClaves.txt";

        // Llamar a Etapa1.main con el archivo de entrada y salida
        Etapa1.main(new String[]{resourcePath, outputPath});
    }

}
