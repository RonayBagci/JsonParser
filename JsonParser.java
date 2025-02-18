import java.util.*;

public class JsonParser {

    private List<String> tokens;
    private int currentIndex;

    public JsonParser(List<String> tokens) {
        this.tokens = tokens;
        this.currentIndex = 0;
    }

    public Object parse() {
        if (tokens == null || tokens.isEmpty()) {
            throw new IllegalArgumentException("Token list is null or empty");
        }

        String token = tokens.get(currentIndex);
        if (token.equals("SYMBOL: LEFT BRACE")) {
            return parseObject();
        } else if (token.equals("SYMBOL: LEFT BRACKET")) {
            return parseArray();
        } else {
            throw new RuntimeException("Invalid JSON: Expected '{' or '['");
        }
    }

    private Map<String, Object> parseObject() {
        Map<String, Object> jsonObject = new HashMap<>();
        currentIndex++; 
    
        while (currentIndex < tokens.size()) {
            String token = tokens.get(currentIndex);
    
            if (token.equals("SYMBOL: RIGHT BRACE")) {
                currentIndex++; 
                return jsonObject;
            }
    
            
            if (token.equals("SYMBOL: DOUBLE QUOTE")) {
                currentIndex++; 
                token = tokens.get(currentIndex); 
            }
    
            
            if (!token.startsWith("STRING VALUE: ")) {
                throw new RuntimeException("Invalid JSON: Expected key");
            }
            String key = token.substring("STRING VALUE: ".length());
            currentIndex++;
    
           
            if (tokens.get(currentIndex).equals("SYMBOL: DOUBLE QUOTE")) {
                currentIndex++; 
            }
    
            
            if (!tokens.get(currentIndex).equals("SYMBOL: COLON")) {
                throw new RuntimeException("Invalid JSON: Expected ':'");
            }
            currentIndex++;
    
           
            Object value = parseValue();
            jsonObject.put(key, value);
    
            
            token = tokens.get(currentIndex);
            if (token.equals("SYMBOL: COMMA")) {
                currentIndex++;
            } else if (!token.equals("SYMBOL: RIGHT BRACE")) {
                throw new RuntimeException("Invalid JSON: Expected ',' or '}'");
            }
        }
    
        throw new RuntimeException("Invalid JSON: Unclosed object");
    }
    

    private List<Object> parseArray() {
        List<Object> jsonArray = new ArrayList<>();
        currentIndex++; // Skip '['

        while (currentIndex < tokens.size()) {
            String token = tokens.get(currentIndex);

            if (token.equals("SYMBOL: RIGHT BRACKET")) {
                currentIndex++;
                return jsonArray;
            }

            
            Object value = parseValue();
            jsonArray.add(value);

            
            token = tokens.get(currentIndex);
            if (token.equals("SYMBOL: COMMA")) {
                currentIndex++;
            } else if (!token.equals("SYMBOL: RIGHT BRACKET")) {
                throw new RuntimeException("Invalid JSON: Expected ',' or ']'");
            }
        }

        throw new RuntimeException("Invalid JSON: Unclosed array");
    }

    private Object parseValue() {
        if (currentIndex >= tokens.size()) {
            throw new RuntimeException("Unexpected end of JSON input");
        }
        String token = tokens.get(currentIndex);
    
        
        if (token.equals("SYMBOL: DOUBLE QUOTE")) {
            currentIndex++; 
            token = tokens.get(currentIndex); 
        }
    
        if (token.startsWith("STRING VALUE: ")) {
            currentIndex++;
            String value = token.substring("STRING VALUE: ".length());
            
            
            if (tokens.get(currentIndex).equals("SYMBOL: DOUBLE QUOTE")) {
                currentIndex++;
            }
            
            return value;
        } else if (token.startsWith("NUMBER: ")) {
            currentIndex++;
            return Double.parseDouble(token.substring("NUMBER: ".length()));
        } else if (token.equals("BOOLEAN: TRUE")) {
            currentIndex++;
            return true;
        } else if (token.equals("BOOLEAN: FALSE")) {
            currentIndex++;
            return false;
        } else if (token.equals("UNKNOWN: null")) {
            currentIndex++;
            return null;
        } else if (token.equals("SYMBOL: LEFT BRACE")) {
            return parseObject();
        } else if (token.equals("SYMBOL: LEFT BRACKET")) {
            return parseArray();
        } else {
            throw new RuntimeException("Invalid JSON: Unexpected token: " + token);
        }
    }
    
}