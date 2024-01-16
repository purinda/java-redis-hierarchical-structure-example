package com.purinda;
import java.util.Map;

import com.purinda.repositories.HierarchyReferenceManagerService;
import com.purinda.repositories.PlayerNameManagerService;

import redis.clients.jedis.Jedis;

public class JrhsApp {
    private static Jedis jedis;

    public static void main(String[] args) {
        System.out.println("JRHS App Started");

        // Create client using service name of the redis container
        jedis = new Jedis("redis", 6379);

        // Run examples
        runHierarchyReferencesExamples();
        runPlayerNamesExamples();

        jedis.close();
    }

    public static void runHierarchyReferencesExamples() {
        // Set hierarchy reference
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
        pnService.addPlayerName(100, "Will Smith");
        pnService.addPlayerName(100, "Jane Doe");
        pnService.addPlayerName(101, "Sam Smith");

        System.out.println("Getting a single Player Name: " + pnService.getPlayerNames(100));
        System.out.println("Getting all Player Names: " + pnService.getAllPlayerNames());
    }
}

