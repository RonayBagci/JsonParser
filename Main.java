import java.util.Map;

public class Main {

    public static void main(String[] args) {
    
        LexicalAnalyzer lexicalAnallyzer = new LexicalAnalyzer();
        lexicalAnallyzer.readJson("test.json");

        lexicalAnallyzer.getTokenList().forEach(token -> System.out.println(token));

        System.out.println("**************************");

        JsonParser jsonParser = new JsonParser(lexicalAnallyzer.getTokenList());

        Map<String, Object> json = (Map<String, Object>) jsonParser.parse();

        // "address" değerini al
        Map<String, Object> address = (Map<String, Object>) json.get("address");

        // "address" içindeki "city" değerini al
        String city = (String) address.get("city");

        System.out.println("The json object is : " + json);
        System.out.println("City: " + city);
    }
}