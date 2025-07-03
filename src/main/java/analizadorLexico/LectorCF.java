package analizadorLexico;


import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * La clase {@code LectorCF} se encarga de la lectura de caracteres desde un archivo,
 * utilizando un buffer para mejorar la eficiencia.
 */

public class LectorCF {
    private RandomAccessFile raf;  // Atributo de la clase
    private int current=0;
    private StringBuilder source= new StringBuilder();

    /**
     * Abre el archivo especificado en la ruta dada para lectura.
     *
     * @param ruta La ruta del archivo a leer.
     * @throws IOException Si ocurre un error al abrir el archivo.
     */

    public void lectorArchivo(String ruta) throws IOException {
        try {
            this.raf = new RandomAccessFile(ruta, "r"); // Abrimos el archivo una sola vez
        }catch (IOException e){
            throw new IOException(e.getMessage());
        }
    }

    /**
     * Recarga el buffer con caracteres desde el archivo, comenzando desde la posición actual.
     *
     * @return Una cadena que contiene los caracteres leídos del archivo.
     *         Si se alcanza el final del archivo, se añade el carácter "€" al final de la cadena.
     * @throws IOException Si ocurre un error durante la lectura del archivo.
     */

    public String rechargeBuffer() throws IOException {
        int caracter;
        try {
            this.raf.seek(current);
        }catch (IOException e){
            throw new IOException(e.getMessage());
        }
        int i;
        for (i = current; i <= current + 2048; i++) {
            if ((caracter = raf.read()) != -1) {
                source.append((char) caracter);
            } else {
                source.append("€");
                return source.toString();
            }
        }
        current = i;
        return source.toString();
    }


}
