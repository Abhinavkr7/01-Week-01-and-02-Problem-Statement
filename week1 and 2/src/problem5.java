import java.util.*;

class PageViewEvent {
    String url;
    String userId;
    String source;

    public PageViewEvent(String url, String userId, String source) {
        this.url = url;
        this.userId = userId;
        this.source = source;
    }
}

public class problem5 {

    // pageUrl -> visit count
    private Map<String, Integer> pageViews = new HashMap<>();

    // pageUrl -> unique visitors
    private Map<String, Set<String>> uniqueVisitors = new HashMap<>();

    // traffic source -> count
    private Map<String, Integer> trafficSources = new HashMap<>();


    // Process incoming event
    public void processEvent(PageViewEvent event) {

        // Count page visits
        pageViews.put(
                event.url,
                pageViews.getOrDefault(event.url, 0) + 1
        );

        // Track unique visitors
        uniqueVisitors
                .computeIfAbsent(event.url, k -> new HashSet<>())
                .add(event.userId);

        // Track traffic sources
        trafficSources.put(
                event.source,
                trafficSources.getOrDefault(event.source, 0) + 1
        );
    }


    // Get top N pages
    private List<Map.Entry<String, Integer>> getTopPages(int n) {

        PriorityQueue<Map.Entry<String, Integer>> heap =
                new PriorityQueue<>(
                        Map.Entry.comparingByValue()
                );

        for (Map.Entry<String, Integer> entry : pageViews.entrySet()) {

            heap.offer(entry);

            if (heap.size() > n) {
                heap.poll();
            }
        }

        List<Map.Entry<String, Integer>> result = new ArrayList<>(heap);

        result.sort((a, b) -> b.getValue() - a.getValue());

        return result;
    }


    // Dashboard display
    public void getDashboard() {

        System.out.println("\n===== REAL-TIME DASHBOARD =====");

        List<Map.Entry<String, Integer>> topPages = getTopPages(10);

        System.out.println("\nTop Pages:");

        int rank = 1;

        for (Map.Entry<String, Integer> entry : topPages) {

            String page = entry.getKey();
            int views = entry.getValue();
            int unique = uniqueVisitors.get(page).size();

            System.out.println(
                    rank + ". " + page +
                            " - " + views + " views (" +
                            unique + " unique)"
            );

            rank++;
        }

        System.out.println("\nTraffic Sources:");

        for (Map.Entry<String, Integer> entry : trafficSources.entrySet()) {

            System.out.println(
                    entry.getKey() + " → " + entry.getValue()
            );
        }

        System.out.println("===============================");
    }


    public static void main(String[] args) throws Exception {

        problem5 analytics = new problem5();

        analytics.processEvent(new PageViewEvent(
                "/article/breaking-news",
                "user_123",
                "google"));

        analytics.processEvent(new PageViewEvent(
                "/article/breaking-news",
                "user_456",
                "facebook"));

        analytics.processEvent(new PageViewEvent(
                "/sports/championship",
                "user_111",
                "google"));

        analytics.processEvent(new PageViewEvent(
                "/sports/championship",
                "user_222",
                "direct"));

        analytics.processEvent(new PageViewEvent(
                "/sports/championship",
                "user_111",
                "facebook"));

        analytics.getDashboard();
    }
}

