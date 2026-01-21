package generacionDeCodigo;

import java.util.LinkedList;

public class CodeGen {
    LinkedList<String> text = new LinkedList<>();
    LinkedList<String> data = new LinkedList<>();
    LinkedList<String> codigo = new LinkedList<>();

    public void agregarLinea(String linea) {
        codigo.add(linea);
    }

    public void agregarData(String data) {
        this.data.add(data);
    }

    public void agregarText(String text){ this.text.add(text);}
}
