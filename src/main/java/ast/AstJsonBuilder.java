package ast;

public class AstJsonBuilder {

    public static String tabs(int cantidad){
        return "\t".repeat(Math.max(0, cantidad));
    }

    public static String claveJson(String key){
        return "\"" + key + "\": ";
    }

    public static String valorJson(String value){
        return "\"" + value + "\"";
    }

    public static boolean esBloque(NodoSentencia nodoSentencia) {
        return nodoSentencia.getClass().getSimpleName().equals("NodoBloque");
    }

    public static String borrarTrailingCommas(String json) {
        String[] lines = json.split("\n");
        for (int i = 0; i < lines.length - 1; i++) {
            String trimmedNext = lines[i + 1].trim();
            if (trimmedNext.startsWith("]") || trimmedNext.startsWith("}")) {
                lines[i] = lines[i].replaceAll(",\\s*$", "");
            }
        }
        return String.join("\n", lines);
    }
}
