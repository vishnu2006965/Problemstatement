import java.util.*;

public class app1 {

    // Stores registered usernames
    private HashMap<String, Integer> usernameMap = new HashMap<>();

    // Tracks attempted usernames
    private HashMap<String, Integer> attemptFrequency = new HashMap<>();

    // Constructor with some sample users
    public app1() {
        usernameMap.put("john_doe", 101);
        usernameMap.put("admin", 1);
        usernameMap.put("vishnu", 102);
    }

    // Check username availability
    public boolean checkAvailability(String username) {

        // Increase attempt count
        attemptFrequency.put(username,
                attemptFrequency.getOrDefault(username, 0) + 1);

        return !usernameMap.containsKey(username);
    }

    // Suggest alternatives
    public List<String> suggestAlternatives(String username) {

        List<String> suggestions = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            String suggestion = username + i;

            if (!usernameMap.containsKey(suggestion)) {
                suggestions.add(suggestion);
            }
        }

        // modify character example
        suggestions.add(username.replace("_", "."));

        return suggestions;
    }

    // Get most attempted username
    public String getMostAttempted() {

        String mostAttempted = null;
        int max = 0;

        for (Map.Entry<String, Integer> entry : attemptFrequency.entrySet()) {

            if (entry.getValue() > max) {
                max = entry.getValue();
                mostAttempted = entry.getKey();
            }
        }

        return mostAttempted + " (" + max + " attempts)";
    }

    // Main method
    public static void main(String[] args) {

        app1 checker = new app1();

        System.out.println("john_doe available? "
                + checker.checkAvailability("john_doe"));

        System.out.println("jane_smith available? "
                + checker.checkAvailability("jane_smith"));

        System.out.println("Suggestions for john_doe: "
                + checker.suggestAlternatives("john_doe"));

        System.out.println("Most attempted username: "
                + checker.getMostAttempted());
    }
}