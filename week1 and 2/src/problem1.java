```java
import java.util.*;

public class problem1 {

    // username -> userId mapping
    private Map<String, Integer> usernameMap;

    // username -> number of attempts
    private Map<String, Integer> attemptFrequency;

    public UsernameChecker() {
        usernameMap = new HashMap<>();
        attemptFrequency = new HashMap<>();
    }

    // Register a username
    public void registerUser(String username, int userId) {
        usernameMap.put(username, userId);
    }

    // Check username availability
    public boolean checkAvailability(String username) {

        // Track attempts
        attemptFrequency.put(username,
                attemptFrequency.getOrDefault(username, 0) + 1);

        return !usernameMap.containsKey(username);
    }

    // Suggest alternative usernames
    public List<String> suggestAlternatives(String username) {

        List<String> suggestions = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            String suggestion = username + i;

            if (!usernameMap.containsKey(suggestion)) {
                suggestions.add(suggestion);
            }
        }

        // Replace underscore with dot suggestion
        String modified = username.replace("_", ".");
        if (!usernameMap.containsKey(modified)) {
            suggestions.add(modified);
        }

        return suggestions;
    }

    // Get most attempted username
    public String getMostAttempted() {

        String mostAttempted = null;
        int maxAttempts = 0;

        for (Map.Entry<String, Integer> entry : attemptFrequency.entrySet()) {

            if (entry.getValue() > maxAttempts) {
                maxAttempts = entry.getValue();
                mostAttempted = entry.getKey();
            }
        }

        return mostAttempted;
    }

    // Display all usernames
    public void displayUsers() {
        System.out.println("\nRegistered Users:");
        for (String username : usernameMap.keySet()) {
            System.out.println(username);
        }
    }

    // Main method for testing
    public static void main(String[] args) {

        UsernameChecker checker = new UsernameChecker();

        // Register some users
        checker.registerUser("john_doe", 101);
        checker.registerUser("admin", 102);
        checker.registerUser("player1", 103);

        // Availability check
        System.out.println("john_doe available? " +
                checker.checkAvailability("john_doe"));

        System.out.println("jane_smith available? " +
                checker.checkAvailability("jane_smith"));

        // Suggestions
        System.out.println("\nSuggestions for john_doe:");
        System.out.println(checker.suggestAlternatives("john_doe"));

        // Simulate attempts
        checker.checkAvailability("admin");
        checker.checkAvailability("admin");
        checker.checkAvailability("admin");

        // Most attempted username
        System.out.println("\nMost Attempted Username: "
                + checker.getMostAttempted());
    }
}
```
