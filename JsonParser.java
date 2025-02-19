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

        String token = peekToken();
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
        consumeToken("SYMBOL: LEFT BRACE"); // Skip '{'

        while (currentIndex < tokens.size()) {
            String token = peekToken();

            // Eğer boş token veya satır sonu gibi bir şey varsa atla
            if (token.trim().isEmpty()) {
                currentIndex++;
                continue;
            }

            if (token.equals("SYMBOL: RIGHT BRACE")) {
                consumeToken("SYMBOL: RIGHT BRACE"); // Skip '}'
                return jsonObject;
            }

            // Anahtar okuma (opsiyonel DOUBLE QUOTE tokenını atla)
            if (token.equals("SYMBOL: DOUBLE QUOTE")) {
                nextToken();
                token = peekToken();
            }

            if (!token.startsWith("STRING VALUE: ")) {
                throw new RuntimeException("Invalid JSON: Expected key but found: " + token);
            }
            String key = token.substring("STRING VALUE: ".length());
            // Eğer key çift tırnak içindeyse kaldır
            if (key.startsWith("\"") && key.endsWith("\"")) {
                key = key.substring(1, key.length() - 1);
            }
            nextToken(); // Anahtar okundu

            // Opsiyonel DOUBLE QUOTE tokenını atla (kapama tırnağı)
            token = peekToken();
            if (token.equals("SYMBOL: DOUBLE QUOTE")) {
                nextToken();
            }

            token = peekToken();
            if (!token.equals("SYMBOL: COLON")) {
                throw new RuntimeException("Invalid JSON: Expected ':' but found: " + token);
            }
            consumeToken("SYMBOL: COLON"); // Skip ':'

            Object value = parseValue();
            jsonObject.put(key, value);

            // Sonrasında ',' veya '}' bekle
            if (currentIndex >= tokens.size()) break;
            token = peekToken();
            if (token.equals("SYMBOL: COMMA")) {
                consumeToken("SYMBOL: COMMA");
            } else if (token.equals("SYMBOL: RIGHT BRACE")) {
                continue;
            } else {
                throw new RuntimeException("Invalid JSON: Expected ',' or '}' but found: " + token);
            }
        }
        throw new RuntimeException("Invalid JSON: Unclosed object");
    }

    private List<Object> parseArray() {
        List<Object> jsonArray = new ArrayList<>();
        consumeToken("SYMBOL: LEFT BRACKET"); // Skip '['

        while (currentIndex < tokens.size()) {
            String token = peekToken();

            if (token.equals("SYMBOL: RIGHT BRACKET")) {
                consumeToken("SYMBOL: RIGHT BRACKET"); // Skip ']'
                return jsonArray;
            }

            Object value = parseValue();
            jsonArray.add(value);

            if (currentIndex >= tokens.size()) break;
            token = peekToken();
            if (token.equals("SYMBOL: COMMA")) {
                consumeToken("SYMBOL: COMMA");
            } else if (!token.equals("SYMBOL: RIGHT BRACKET")) {
                throw new RuntimeException("Invalid JSON: Expected ',' or ']' but found: " + token);
            }
        }

        throw new RuntimeException("Invalid JSON: Unclosed array");
    }

    private Object parseValue() {
        if (currentIndex >= tokens.size()) {
            throw new RuntimeException("Unexpected end of JSON input");
        }
        String token = peekToken();

        // Eğer değer çift tırnak ile başlıyorsa, opsiyonel tırnak tokenlarını atlayıp değeri oku
        if (token.equals("SYMBOL: DOUBLE QUOTE")) {
            nextToken();
            token = peekToken();
        }

        if (token.startsWith("STRING VALUE: ")) {
            nextToken();
            String value = token.substring("STRING VALUE: ".length());
            // Eğer tırnaklar varsa kaldır
            if (value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length() - 1);
            }
            if (currentIndex < tokens.size() && peekToken().equals("SYMBOL: DOUBLE QUOTE")) {
                nextToken();
            }
            return value;
        } else if (token.startsWith("NUMBER: ")) {
            nextToken();
            return Double.parseDouble(token.substring("NUMBER: ".length()));
        } else if (token.equals("BOOLEAN: TRUE")) {
            nextToken();
            return true;
        } else if (token.equals("BOOLEAN: FALSE")) {
            nextToken();
            return false;
        } else if (token.equals("UNKNOWN: null")) {
            nextToken();
            return null;
        } else if (token.equals("SYMBOL: LEFT BRACE")) {
            return parseObject();
        } else if (token.equals("SYMBOL: LEFT BRACKET")) {
            return parseArray();
        } else {
            throw new RuntimeException("Invalid JSON: Unexpected token: " + token);
        }
    }

    // Yardımcı metotlar: peekToken ve consumeToken, nextToken.
    private String peekToken() {
        return tokens.get(currentIndex).trim();
    }

    private String nextToken() {
        return tokens.get(currentIndex++);
    }

    private void consumeToken(String expected) {
        String token = nextToken();
        if (!token.equals(expected)) {
            throw new RuntimeException("Invalid JSON: Expected token " + expected + " but found " + token);
        }
    }
}