```java
import java.util.*;

public class problem2 {

    // productId -> stock count
    private Map<String, Integer> stockMap;

    // productId -> waiting list (FIFO)
    private Map<String, Queue<Integer>> waitingList;

    public FlashSaleInventoryManager() {
        stockMap = new HashMap<>();
        waitingList = new HashMap<>();
    }

    // Add product with stock
    public void addProduct(String productId, int stock) {
        stockMap.put(productId, stock);
        waitingList.put(productId, new LinkedList<>());
    }

    // Check stock availability
    public int checkStock(String productId) {
        return stockMap.getOrDefault(productId, 0);
    }

    // Purchase item (thread-safe)
    public synchronized void purchaseItem(String productId, int userId) {

        int stock = stockMap.getOrDefault(productId, 0);

        if (stock > 0) {

            stockMap.put(productId, stock - 1);

            System.out.println("User " + userId +
                    " purchase successful. Remaining stock: "
                    + (stock - 1));

        } else {

            Queue<Integer> queue = waitingList.get(productId);
            queue.offer(userId);

            System.out.println("Stock finished. User " + userId +
                    " added to waiting list. Position #" + queue.size());
        }
    }

    // Display waiting list
    public void showWaitingList(String productId) {

        Queue<Integer> queue = waitingList.get(productId);

        System.out.println("\nWaiting List for " + productId + ":");

        for (Integer user : queue) {
            System.out.println("User ID: " + user);
        }
    }

    // Main test
    public static void main(String[] args) {

        FlashSaleInventoryManager manager = new FlashSaleInventoryManager();

        manager.addProduct("IPHONE15_256GB", 5);

        System.out.println("Stock Available: " +
                manager.checkStock("IPHONE15_256GB"));

        manager.purchaseItem("IPHONE15_256GB", 12345);
        manager.purchaseItem("IPHONE15_256GB", 67890);
        manager.purchaseItem("IPHONE15_256GB", 11111);
        manager.purchaseItem("IPHONE15_256GB", 22222);
        manager.purchaseItem("IPHONE15_256GB", 33333);

        // Stock finished
        manager.purchaseItem("IPHONE15_256GB", 99999);
        manager.purchaseItem("IPHONE15_256GB", 88888);

        manager.showWaitingList("IPHONE15_256GB");
    }
}
```
