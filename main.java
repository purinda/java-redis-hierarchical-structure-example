import java.util.Map;

import src.Client;

public class JRHSApp {
    public static void main(String[] args) {
        Client client = new Client("redis", 6379);

        // Set hierarchy reference
        client.setHierarchyReference(101, 10, 20, "A");

        // Get hierarchy reference
        Map<String, String> hierarchyReference = client.getHierarchyReference(101);
        System.out.println("Hierarchy Reference: " + hierarchyReference);

        // Delete hierarchy reference
        client.deleteHierarchyReference(101);

        client.close();
    }
}
