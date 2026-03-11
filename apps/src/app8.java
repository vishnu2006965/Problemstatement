import java.util.*;

class Transaction {

    int id;
    int amount;
    String merchant;
    String account;
    long timestamp;

    public Transaction(int id, int amount, String merchant, String account, long timestamp) {
        this.id = id;
        this.amount = amount;
        this.merchant = merchant;
        this.account = account;
        this.timestamp = timestamp;
    }
}

public class app8 {

    // -------- Classic Two Sum --------
    public static List<String> findTwoSum(List<Transaction> transactions, int target) {

        HashMap<Integer, Transaction> map = new HashMap<>();
        List<String> result = new ArrayList<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {

                Transaction prev = map.get(complement);
                result.add("(" + prev.id + "," + t.id + ")");
            }

            map.put(t.amount, t);
        }

        return result;
    }

    // -------- Two Sum with Time Window (1 hour) --------
    public static List<String> findTwoSumWithTimeWindow(List<Transaction> transactions, int target) {

        HashMap<Integer, List<Transaction>> map = new HashMap<>();
        List<String> result = new ArrayList<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {

                for (Transaction prev : map.get(complement)) {

                    if (Math.abs(t.timestamp - prev.timestamp) <= 3600) {

                        result.add("(" + prev.id + "," + t.id + ")");
                    }
                }
            }

            map.putIfAbsent(t.amount, new ArrayList<>());
            map.get(t.amount).add(t);
        }

        return result;
    }

    // -------- K-Sum --------
    public static List<List<Integer>> kSum(List<Transaction> transactions, int k, int target, int start) {

        List<List<Integer>> result = new ArrayList<>();

        if (k == 2) {

            HashMap<Integer, Integer> map = new HashMap<>();

            for (int i = start; i < transactions.size(); i++) {

                int amount = transactions.get(i).amount;
                int complement = target - amount;

                if (map.containsKey(complement)) {

                    List<Integer> pair = new ArrayList<>();
                    pair.add(transactions.get(map.get(complement)).id);
                    pair.add(transactions.get(i).id);

                    result.add(pair);
                }

                map.put(amount, i);
            }

            return result;
        }

        for (int i = start; i < transactions.size(); i++) {

            Transaction current = transactions.get(i);

            List<List<Integer>> subsets =
                    kSum(transactions, k - 1, target - current.amount, i + 1);

            for (List<Integer> subset : subsets) {

                List<Integer> list = new ArrayList<>();
                list.add(current.id);
                list.addAll(subset);

                result.add(list);
            }
        }

        return result;
    }

    // -------- Duplicate Detection --------
    public static void detectDuplicates(List<Transaction> transactions) {

        HashMap<String, Set<String>> map = new HashMap<>();

        for (Transaction t : transactions) {

            String key = t.amount + "_" + t.merchant;

            map.putIfAbsent(key, new HashSet<>());
            map.get(key).add(t.account);
        }

        System.out.println("\nDuplicate Transactions:");

        for (Map.Entry<String, Set<String>> entry : map.entrySet()) {

            if (entry.getValue().size() > 1) {

                System.out.println(
                        "Amount & Merchant: " + entry.getKey()
                                + " Accounts: " + entry.getValue()
                );
            }
        }
    }

    // -------- Main Program --------
    public static void main(String[] args) {

        List<Transaction> transactions = new ArrayList<>();

        transactions.add(new Transaction(1, 500, "StoreA", "acc1", 36000));
        transactions.add(new Transaction(2, 300, "StoreB", "acc2", 36900));
        transactions.add(new Transaction(3, 200, "StoreC", "acc3", 37800));
        transactions.add(new Transaction(4, 500, "StoreA", "acc4", 38000));

        System.out.println("Classic Two Sum (target = 500):");
        System.out.println(findTwoSum(transactions, 500));

        System.out.println("\nTwo Sum with Time Window:");
        System.out.println(findTwoSumWithTimeWindow(transactions, 500));

        System.out.println("\nK-Sum (k=3, target=1000):");
        System.out.println(kSum(transactions, 3, 1000, 0));

        detectDuplicates(transactions);
    }
}