import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
    
        LexicalAnalyzer lexicalAnallyzer = new LexicalAnalyzer();
        lexicalAnallyzer.readJson("test.json");

        System.out.println("The tokens are listed below:");

        lexicalAnallyzer.getTokenList().forEach(token -> System.out.println(token));

        System.out.println("**************************");

        JsonParser jsonParser = new JsonParser(lexicalAnallyzer.getTokenList());

        Map<String, Object> json = (Map<String, Object>) jsonParser.parse();

        List<Object> users = (List<Object>) json.get("users");
        

        System.out.println("The json object is : " + json);
        System.out.println("***************");
        
        for(Object userObject:users){
            Map<String,Object> user = (Map<String,Object>) userObject;
            System.out.println("User id :" + user.get("id"));
        }



    }
}
