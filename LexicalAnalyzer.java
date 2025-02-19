import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.*;

public class LexicalAnalyzer {

    private List<String> tokenList; 

    public LexicalAnalyzer() {
        this.tokenList = new ArrayList<>();
    }

    public void readJson(String path) {
        try {
            File jsonFile = new File(path);
            Scanner scanner = new Scanner(jsonFile);

            while (scanner.hasNextLine()) {
                String data = scanner.nextLine().trim();
                classifyTokens(data);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File couldn't open");
            e.printStackTrace();
        }
    }

    public Map<String, String> defineSymbolTokens() {
        Map<String, String> symbolTokens = new HashMap<>();
        symbolTokens.put("{", "LEFT BRACE");
        symbolTokens.put("}", "RIGHT BRACE");
        symbolTokens.put("[", "LEFT BRACKET");
        symbolTokens.put("]", "RIGHT BRACKET");
        symbolTokens.put(":", "COLON");
        symbolTokens.put(",", "COMMA");
        symbolTokens.put("\"", "DOUBLE QUOTE");
        return symbolTokens;
    }

    public Map<String, String> defineBooleanTokens() {
        Map<String, String> booleanTokens = new HashMap<>();
        booleanTokens.put("true", "BOOLEAN: TRUE");
        booleanTokens.put("false", "BOOLEAN: FALSE");
        return booleanTokens;
    }

    public void classifyTokens(String data) {
        Pattern tokenPattern = Pattern.compile(
            "\"(\\\\.|[^\"])*\"|" +       // String değerler: çift tırnak içindeki tüm karakterler
            "\\{|\\}|\\[|\\]|:|,|" +       // Semboller
            "-?\\d+(\\.\\d+)?|" +          // Sayılar
            "\\btrue\\b|\\bfalse\\b|null"  // Boolean ve null
        );

        Matcher matcher = tokenPattern.matcher(data);

        while (matcher.find()) {
            String token = matcher.group().trim();

            if (token.startsWith("\"") && token.endsWith("\"")) {
                tokenList.add("STRING VALUE: " + token);
            } else if (defineSymbolTokens().containsKey(token)) {
                tokenList.add("SYMBOL: " + defineSymbolTokens().get(token));
            } else if (defineBooleanTokens().containsKey(token)) {
                tokenList.add(defineBooleanTokens().get(token));
            } else if (token.matches("-?(?:0|[1-9]\\d*)(?:\\.\\d+)?(?:[eE][+-]?\\d+)?")) {
                tokenList.add("NUMBER: " + token);
            } else if (!token.isEmpty()) {
                tokenList.add("UNKNOWN: " + token);
            }
        }
    }

    public List<String> getTokenList() {
        return tokenList;
    }
}
