import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class JsonReader {

    public static void main(String[] args) throws Exception {
        // Load JSON from resources
        InputStream is = JsonReader.class.getClassLoader().getResourceAsStream("TestCaseData.json");
        if (is == null) {
            System.err.println("JSON file not found!");
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(is);
        JsonNode testCases = root.get("recorderTestInputList");

        for (int i = 0; i < testCases.size(); i++) {
            JsonNode testCase = testCases.get(i);
            System.out.println("=== Test Case #" + (i + 1) + " ===");

            // Flatten appInfo
            Map<String, String> appInfoMap = flatten(testCase.get("appInfo"));
            System.out.println("-- App Info --");
            appInfoMap.forEach((k, v) -> System.out.println(k + " => " + v));

            // Iterate controlInfoList
            JsonNode controlList = testCase.get("controlInfoList");
            for (int j = 0; j < controlList.size(); j++) {
                JsonNode control = controlList.get(j);

                Map<String, String> controlMap = flatten(control);
                Map<String, String> propertiesMap = flatten(control.get("properties"));

                System.out.println("-- Control Info #" + (j + 1) + " --");
                controlMap.forEach((k, v) -> System.out.println(k + " => " + v));

                System.out.println("-- Properties Map #" + (j + 1) + " --");
                propertiesMap.forEach((k, v) -> System.out.println(k + " => " + v));
            }
            System.out.println();
        }
    }

    private static Map<String, String> flatten(JsonNode node) {
        Map<String, String> result = new LinkedHashMap<>();
        flattenRecursive("", node, result);
        return result;
    }

    private static void flattenRecursive(String prefix, JsonNode node, Map<String, String> map) {
        if (node == null || node.isNull()) return;

        if (node.isObject()) {
            node.fieldNames().forEachRemaining(field -> {
                JsonNode child = node.get(field);
                String newPrefix = prefix.isEmpty() ? field : prefix + "." + field;
                flattenRecursive(newPrefix, child, map);
            });
        } else if (node.isArray()) {
            int index = 0;
            for (JsonNode item : node) {
                flattenRecursive(prefix + "[" + index + "]", item, map);
                index++;
            }
        } else {
            map.put(prefix, node.asText());
        }
    }
}
