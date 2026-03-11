import java.util.*;

public class app2 {

    // Product stock storage
    private HashMap<String, Integer> stockMap = new HashMap<>();

    // Waiting list for sold-out products
    private HashMap<String, Queue<Integer>> waitingList = new HashMap<>();

    // Initialize product
    public app2() {
        stockMap.put("IPHONE15_256GB", 100);
        waitingList.put("IPHONE15_256GB", new LinkedList<>());
    }

    // Check stock availability
    public int checkStock(String productId) {

        return stockMap.getOrDefault(productId, 0);
    }

    // Purchase item (thread-safe)
    public synchronized String purchaseItem(String productId, int userId) {

        int stock = stockMap.getOrDefault(productId, 0);

        if (stock > 0) {

            stockMap.put(productId, stock - 1);

            return "Success for user " + userId +
                    ", remaining stock: " + (stock - 1);
        } else {

            Queue<Integer> queue = waitingList.get(productId);
            queue.add(userId);

            return "Added to waiting list. Position #" + queue.size();
        }
    }

    // Display waiting list
    public void showWaitingList(String productId) {

        Queue<Integer> queue = waitingList.get(productId);

        System.out.println("Waiting list: " + queue);
    }

    public static void main(String[] args) {

        app2 manager =
                new app2 ();

        System.out.println("Stock: "
                + manager.checkStock("IPHONE15_256GB"));

        System.out.println(manager.purchaseItem("IPHONE15_256GB", 12345));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 67890));

        // Simulate sold out
        for (int i = 0; i < 100; i++) {
            manager.purchaseItem("IPHONE15_256GB", i);
        }

        System.out.println(manager.purchaseItem("IPHONE15_256GB", 99999));

        manager.showWaitingList("IPHONE15_256GB");
    }
}