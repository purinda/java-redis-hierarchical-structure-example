package com.purinda;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.purinda.definitions.Rule.RuleType;
import com.purinda.models.SuspensionRule;
import com.purinda.repositories.HierarchyReferenceManagerService;
import com.purinda.repositories.PlayerNameManagerService;
import com.purinda.repositories.SuspensionRuleManagerService;

import redis.clients.jedis.Jedis;

public class JrhsApp {
    private static Jedis jedis;
    private static final String JSON_DATA = "{"
            + "    \"hierarchyReferences\": ["
            + "        {"
            + "            \"hierarchyRefId\": 100,"
            + "            \"eventClassId\": 200,"
            + "            \"eventTypeId\": 300"
            + "        },"
            + "        {"
            + "            \"hierarchyRefId\": 101,"
            + "            \"eventClassId\": 200,"
            + "            \"eventTypeId\": 301"
            + "        },"
            + "        {"
            + "            \"hierarchyRefId\": 102,"
            + "            \"eventClassId\": 202,"
            + "            \"eventTypeId\": 302"
            + "        }"
            + "    ],"
            + "    \"suspensionRules\": ["
            + "        {"
            + "            \"hierarchyRefId\": 100,"
            + "            \"ruleType\": \"SUSPEV\""
            + "        },"
            + "        {"
            + "            \"hierarchyRefId\": 101,"
            + "            \"ruleType\": \"SUSPMKT\""
            + "        },"
            + "        {"
            + "            \"hierarchyRefId\": 102,"
            + "            \"ruleType\": \"SUSPMKT\""
            + "        }"
            + "    ],"
            + "    \"playerNames\": ["
            + "        {"
            + "            \"hierarchyRefId\": 100,"
            + "            \"name\": \"Alice\""
            + "        },"
            + "        {"
            + "            \"hierarchyRefId\": 100,"
            + "            \"name\": \"Bob\""
            + "        },"
            + "        {"
            + "            \"hierarchyRefId\": 102,"
            + "            \"name\": \"Charlie\""
            + "        }"
            + "    ]"
            + "}";

    public static void main(String[] args) {
        System.out.println("JRHS App Started");
        System.out.println();

        // Create client using service name of the redis container
        jedis = new Jedis("redis", 6379);

        // Run examples
        runHierarchyReferencesExamples();
        System.out.println();

        runPlayerNamesExamples();
        System.out.println();

        runSuspensionRulesExamples();
        System.out.println();

        // Process the JSON data
        processJsonData(JSON_DATA);

        jedis.close();
    }

    public static void runHierarchyReferencesExamples() {
        // Set hierarchy reference
        System.out.println("Set Hierarchy References");
        HierarchyReferenceManagerService hrService = new HierarchyReferenceManagerService(jedis);
        hrService.setHierarchyReference(100, 10, 10, "A");
        hrService.setHierarchyReference(101, 10, 20, "A");

        // Get hierarchy reference
        Map<String, String> hierarchyReference = hrService.getHierarchyReference(101);
        System.out.println("Getting a single Hierarchy Reference: " + hierarchyReference);

        // Get all hierarchy references
        System.out.println("Getting all Hierarchy References: " + hrService.getAllHierarchyReferences());

        // Delete hierarchy reference
        // hrService.deleteHierarchyReference(101);
    }

    public static void runPlayerNamesExamples() {
        // Set player names
        PlayerNameManagerService pnService = new PlayerNameManagerService(jedis);

        System.out.println("Set Player Names ");

        pnService.addPlayerName(100, "Will Smith");
        pnService.addPlayerName(100, "Jane Doe");
        pnService.addPlayerName(101, "Sam Smith");

        System.out.println("Getting a single Player Name: " + pnService.getPlayerNames(100));
        System.out.println("Getting all Player Names: " + pnService.getAllPlayerNames());
    }

    public static void runSuspensionRulesExamples() {
        System.out.println("Set Suspension Rules");
        SuspensionRuleManagerService srService = new SuspensionRuleManagerService(jedis);
        srService.setSuspensionRule(100, RuleType.SUSPEV);
        srService.setSuspensionRule(101, RuleType.SUSPMKT);

        System.out.println("Getting a single Suspension Rule: " + srService.getSuspensionRule(100));
        System.out.println("Getting all suspension rules: " + srService.getAllSuspensionRules());
    }

    public static void processJsonData(String jsonData) {
        ObjectMapper mapper = new ObjectMapper();

        System.out.println("Process JSON blob below, store in Redis then reads it back");
        printFormattedJson(JSON_DATA);

        try {
            JsonNode rootNode = mapper.readTree(jsonData);

            // Process Hierarchy References
            JsonNode hierarchyRefsNode = rootNode.path("hierarchyReferences");
            if (!hierarchyRefsNode.isMissingNode()) {
                processHierarchyReferences(hierarchyRefsNode);
            }

            // Process Suspension Rules
            JsonNode suspensionRulesNode = rootNode.path("suspensionRules");
            if (!suspensionRulesNode.isMissingNode()) {
                processSuspensionRules(suspensionRulesNode);
            }

            // Process Player Names
            JsonNode playerNamesNode = rootNode.path("playerNames");
            if (!playerNamesNode.isMissingNode()) {
                processPlayerNames(playerNamesNode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processHierarchyReferences(JsonNode hierarchyRefsNode) {
        HierarchyReferenceManagerService hrService = new HierarchyReferenceManagerService(jedis);
        for (JsonNode refNode : hierarchyRefsNode) {
            int hierRefId = refNode.get("hierarchyRefId").asInt();
            int evClassId = refNode.get("eventClassId").asInt();
            int evTypeId = refNode.get("eventTypeId").asInt();
            hrService.setHierarchyReference(hierRefId, evClassId, evTypeId, "A"); // Assuming status is always "A"
        }

        // Get Hierarchies
        System.out.println("Getting all Hierarchy References: " + hrService.getAllHierarchyReferences());
    }

    private static void processSuspensionRules(JsonNode suspensionRulesNode) {
        SuspensionRuleManagerService srService = new SuspensionRuleManagerService(jedis);
        for (JsonNode ruleNode : suspensionRulesNode) {
            int ruleId = ruleNode.get("hierarchyRefId").asInt();
            RuleType ruleType = RuleType.valueOf(ruleNode.get("ruleType").asText());
            srService.setSuspensionRule(ruleId, ruleType);
        }

        // Get Suspension Rules
        System.out.println("Getting all suspension rules: " + srService.getAllSuspensionRules());
    }

    private static void processPlayerNames(JsonNode playerNamesNode) {
        PlayerNameManagerService pnService = new PlayerNameManagerService(jedis);
        for (JsonNode nameNode : playerNamesNode) {
            int playerId = nameNode.get("hierarchyRefId").asInt();
            String name = nameNode.get("name").asText();
            pnService.addPlayerName(playerId, name);
        }

        // Get Player Names
        System.out.println("Getting all Player Names: " + pnService.getAllPlayerNames());
    }

    // Utility function
    public static void printFormattedJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            Object jsonObj = mapper.readValue(json, Object.class);
            String prettyJson = mapper.writeValueAsString(jsonObj);

            System.out.println(prettyJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
