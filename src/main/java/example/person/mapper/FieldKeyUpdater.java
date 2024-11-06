package example.person.mapper;

import java.util.HashMap;
import java.util.Map;

public class FieldKeyUpdater {
    public static Map<String, Object> updateFieldKeys(Map<String, Object> fields) {
        Map<String, Object> updatedFields = new HashMap<>();

        fields.forEach((key, value) -> {
            String newKey = key;

            // Example: changing specific keys
            if (key.equals("first_name")) {
                newKey = "firstName";
            } else if (key.equals("last_name")) {
                newKey = "lastName";
            }
            // Add more key mappings as needed

            updatedFields.put(newKey, value);
        });

        return updatedFields;
    }
}
