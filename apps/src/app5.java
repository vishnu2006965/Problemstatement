import java.util.*;

class PageEvent {

    String url;
    String userId;
    String source;

    public PageEvent(String url, String userId, String source) {
        this.url = url;
        this.userId = userId;
        this.source = source;
    }
}

public class app5 {

    // page → visit count
    private HashMap<String, Integer> pageViews = new HashMap<>();

    // page → unique visitors
    private HashMap<String, Set<String>> uniqueVisitors = new HashMap<>();

    // traffic source counts
    private HashMap<String, Integer> trafficSources = new HashMap<>();

    // Process incoming event
    public void processEvent(PageEvent event) {

        // Count page view
        pageViews.put(event.url,
                pageViews.getOrDefault(event.url, 0) + 1);

        // Track unique visitors
        uniqueVisitors.putIfAbsent(event.url, new HashSet<>());
        uniqueVisitors.get(event.url).add(event.userId);

        // Count traffic source
        trafficSources.put(event.source,
                trafficSources.getOrDefault(event.source, 0) + 1);
    }

    // Get top 10 pages
    public List<Map.Entry<String, Integer>> getTopPages() {

        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>(
                        Map.Entry.comparingByValue()
                );

        for (Map.Entry<String, Integer> entry : pageViews.entrySet()) {

            pq.offer(entry);

            if (pq.size() > 10) {
                pq.poll();
            }
        }

        List<Map.Entry<String, Integer>> result = new ArrayList<>();

        while (!pq.isEmpty()) {
            result.add(pq.poll());
        }

        Collections.reverse(result);

        return result;
    }

    // Display dashboard
    public void getDashboard() {

        System.out.println("\n===== REAL-TIME DASHBOARD =====");

        System.out.println("\nTop Pages:");

        List<Map.Entry<String, Integer>> topPages = getTopPages();

        int rank = 1;

        for (Map.Entry<String, Integer> entry : topPages) {

            String url = entry.getKey();
            int views = entry.getValue();
            int unique = uniqueVisitors.get(url).size();

            System.out.println(rank + ". " + url +
                    " - " + views +
                    " views (" + unique + " unique)");

            rank++;
        }

        System.out.println("\nTraffic Sources:");

        for (Map.Entry<String, Integer> entry : trafficSources.entrySet()) {

            System.out.println(entry.getKey() +
                    " → " + entry.getValue());
        }
    }

    public static void main(String[] args) throws Exception {

        app5 dashboard = new app5();

        // Simulated events
        dashboard.processEvent(new PageEvent(
                "/article/breaking-news", "user_123", "google"));

        dashboard.processEvent(new PageEvent(
                "/article/breaking-news", "user_456", "facebook"));

        dashboard.processEvent(new PageEvent(
                "/sports/championship", "user_111", "google"));

        dashboard.processEvent(new PageEvent(
                "/sports/championship", "user_222", "direct"));

        dashboard.processEvent(new PageEvent(
                "/sports/championship", "user_333", "google"));

        dashboard.processEvent(new PageEvent(
                "/tech/ai-news", "user_999", "twitter"));

        // Update dashboard every 5 seconds
        while (true) {

            dashboard.getDashboard();

            Thread.sleep(5000);
        }
    }
}