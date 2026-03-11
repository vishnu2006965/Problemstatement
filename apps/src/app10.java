import java.util.*;

class VideoData {

    String videoId;
    String content;

    public VideoData(String videoId, String content) {
        this.videoId = videoId;
        this.content = content;
    }
}

public class app10 {

    // L1 Cache (10k videos) – Memory
    private LinkedHashMap<String, VideoData> L1 =
            new LinkedHashMap<>(10000, 0.75f, true) {
                protected boolean removeEldestEntry(Map.Entry<String, VideoData> eldest) {
                    return size() > 10000;
                }
            };

    // L2 Cache (100k videos) – SSD simulation
    private LinkedHashMap<String, VideoData> L2 =
            new LinkedHashMap<>(100000, 0.75f, true) {
                protected boolean removeEldestEntry(Map.Entry<String, VideoData> eldest) {
                    return size() > 100000;
                }
            };

    // L3 Database
    private HashMap<String, VideoData> database = new HashMap<>();

    // Access count tracking
    private HashMap<String, Integer> accessCount = new HashMap<>();

    // Hit counters
    private int L1Hits = 0;
    private int L2Hits = 0;
    private int L3Hits = 0;

    public MultiLevelCache() {


        for (int i = 1; i <= 200000; i++) {

            String id = "video_" + i;
            database.put(id, new VideoData(id, "Content of " + id));
        }
    }

    public VideoData getVideo(String videoId) {

        long start = System.currentTimeMillis();

        // L1 lookup
        if (L1.containsKey(videoId)) {

            L1Hits++;
            System.out.println("L1 Cache HIT (0.5ms)");
            return L1.get(videoId);
        }

        System.out.println("L1 Cache MISS");

        // L2 lookup
        if (L2.containsKey(videoId)) {

            L2Hits++;
            System.out.println("L2 Cache HIT (5ms)");

            VideoData video = L2.get(videoId);

            // promote to L1
            L1.put(videoId, video);

            return video;
        }

        System.out.println("L2 Cache MISS");

        // L3 database lookup
        VideoData video = database.get(videoId);

        if (video != null) {

            L3Hits++;
            System.out.println("L3 Database HIT (150ms)");

            // add to L2
            L2.put(videoId, video);

            accessCount.put(videoId,
                    accessCount.getOrDefault(videoId, 0) + 1);
        }

        long end = System.currentTimeMillis();

        System.out.println("Total time: " + (end - start) + " ms");

        return video;
    }

    // Cache statistics
    public void getStatistics() {

        int total = L1Hits + L2Hits + L3Hits;

        System.out.println("\nCache Statistics");

        if (total == 0) {
            System.out.println("No accesses yet.");
            return;
        }

        System.out.println("L1 Hit Rate: " +
                (L1Hits * 100.0 / total) + "%");

        System.out.println("L2 Hit Rate: " +
                (L2Hits * 100.0 / total) + "%");

        System.out.println("L3 Hit Rate: " +
                (L3Hits * 100.0 / total) + "%");

        System.out.println("Overall Requests: " + total);
    }

    public static void main(String[] args) {

        app10 cache = new app10();

        cache.getVideo("video_123");
        cache.getVideo("video_123");
        cache.getVideo("video_999");

        cache.getStatistics();
    }
}