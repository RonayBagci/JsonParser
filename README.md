# JsonParser

Basic Java JSON Deserialization showcase.

JSON OBJECTS => Java Objects

Here an example of test.json:

```json
{
    "name": "Ali",
    "age": 25,
    "isStudent": false,
    "grades": [75.4, 90, 85],
    "address": {
        "city": "İstanbul",
        "country": "Turkey",
        "street":null 
    }
}
```

And converted Java object:

```Java
{address={country=Turkey, city=İstanbul, street=null}, name=Ali, grades=[75.4, 90.0, 85.0], age=25.0, isStudent=false}
```

And you can get specific value from the json file with using mapping get function.

Given example from Main.java class

```Java
JsonParser jsonParser = new JsonParser(lexicalAnallyzer.getTokenList());

        Map<String, Object> json = (Map<String, Object>) jsonParser.parse();

        Map<String, Object> address = (Map<String, Object>) json.get("address");

        String city = (String) address.get("city");

        System.out.println("The json object is : " + json);
        System.out.println("City: " + city);
```

And output is :
```code
The json object is : {address={country=Turkey, city=İstanbul, street=null}, name=Ali, grades=[75.4, 90.0, 85.0], age=25.0, isStudent=false}
City: İstanbul
```
