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
}
