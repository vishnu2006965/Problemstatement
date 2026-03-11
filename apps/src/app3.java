import java.util.*;

// Entry class for DNS cache
class DNSEntry {

    String domain;
    String ipAddress;
    long expiryTime;

    public DNSEntry(String domain, String ipAddress, int ttlSeconds) {
        this.domain = domain;
        this.ipAddress = ipAddress;
        this.expiryTime = System.currentTimeMillis() + (ttlSeconds * 1000);
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}

// DNS Cache class
class DNSCache {

    private final int MAX_SIZE = 5;

    private LinkedHashMap<String, DNSEntry> cache =
            new LinkedHashMap<>(16, 0.75f, true);

    private int hits = 0;
    private int misses = 0;

    // Resolve domain
    public synchronized String resolve(String domain) {

        DNSEntry entry = cache.get(domain);

        if (entry != null && !entry.isExpired()) {
            hits++;
            return "Cache HIT → " + entry.ipAddress;
        }

        misses++;

        // Simulate upstream DNS query
        String ip = queryUpstreamDNS(domain);

        cache.put(domain, new DNSEntry(domain, ip, 10));

        // LRU removal if cache full
        if (cache.size() > MAX_SIZE) {
            String firstKey = cache.keySet().iterator().next();
            cache.remove(firstKey);
        }

        return "Cache MISS → " + ip;
    }

    // Fake upstream DNS lookup
    private String queryUpstreamDNS(String domain) {
        Random rand = new Random();
        return "172.217.14." + rand.nextInt(255);
    }

    // Remove expired entries
    public void cleanExpiredEntries() {

        Iterator<Map.Entry<String, DNSEntry>> it = cache.entrySet().iterator();

        while (it.hasNext()) {

            Map.Entry<String, DNSEntry> entry = it.next();

            if (entry.getValue().isExpired()) {
                it.remove();
            }
        }
    }

    // Cache statistics
    public void getCacheStats() {

        int total = hits + misses;
        double hitRate = total == 0 ? 0 : ((double) hits / total) * 100;

        System.out.println("Cache Hits: " + hits);
        System.out.println("Cache Misses: " + misses);
        System.out.println("Hit Rate: " + hitRate + "%");
    }
}

// Main class
public class app3 {

    public static void main(String[] args) throws Exception {

        DNSCache dnsCache = new DNSCache();

        System.out.println(dnsCache.resolve("google.com"));
        System.out.println(dnsCache.resolve("google.com"));
        System.out.println(dnsCache.resolve("amazon.com"));
        System.out.println(dnsCache.resolve("google.com"));

        Thread.sleep(11000); // wait for TTL expiry

        System.out.println(dnsCache.resolve("google.com"));

        dnsCache.getCacheStats();
    }
}