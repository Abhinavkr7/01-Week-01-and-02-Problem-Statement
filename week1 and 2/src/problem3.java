
import java.util.*;

class DNSEntry {

    String domain;
    String ipAddress;
    long expiryTime;

    public DNSEntry(String domain, String ipAddress, long ttlSeconds) {
        this.domain = domain;
        this.ipAddress = ipAddress;
        this.expiryTime = System.currentTimeMillis() + (ttlSeconds * 1000);
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}

public class problem3 {

    private final int MAX_CACHE_SIZE = 5;

    // LRU Cache using LinkedHashMap
    private LinkedHashMap<String, DNSEntry> cache;

    private int cacheHits = 0;
    private int cacheMisses = 0;

    public problem3() {

        cache = new LinkedHashMap<String, DNSEntry>(16, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                return size() > MAX_CACHE_SIZE;
            }
        };

        startCleanupThread();
    }

    // Resolve domain
    public synchronized String resolve(String domain) {

        DNSEntry entry = cache.get(domain);

        if (entry != null) {

            if (!entry.isExpired()) {
                cacheHits++;
                System.out.println("Cache HIT → " + entry.ipAddress);
                return entry.ipAddress;
            }

            cache.remove(domain);
            System.out.println("Cache EXPIRED → querying upstream...");
        }

        cacheMisses++;

        String ip = queryUpstreamDNS(domain);

        cache.put(domain, new DNSEntry(domain, ip, 10));

        return ip;
    }

    // Simulate upstream DNS lookup
    private String queryUpstreamDNS(String domain) {

        try {
            Thread.sleep(100); // simulate network delay
        } catch (InterruptedException e) {}

        String ip = "172.217." + new Random().nextInt(255) + "." + new Random().nextInt(255);

        System.out.println("Cache MISS → Upstream DNS → " + ip);

        return ip;
    }

    // Cleanup expired entries
    private void startCleanupThread() {

        Thread cleaner = new Thread(() -> {

            while (true) {

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {}

                synchronized (this) {

                    Iterator<Map.Entry<String, DNSEntry>> iterator = cache.entrySet().iterator();

                    while (iterator.hasNext()) {

                        Map.Entry<String, DNSEntry> entry = iterator.next();

                        if (entry.getValue().isExpired()) {
                            iterator.remove();
                        }
                    }
                }
            }
        });

        cleaner.setDaemon(true);
        cleaner.start();
    }

    // Cache statistics
    public void getCacheStats() {

        int total = cacheHits + cacheMisses;

        double hitRate = total == 0 ? 0 : (cacheHits * 100.0 / total);

        System.out.println("\nCache Stats:");
        System.out.println("Hits: " + cacheHits);
        System.out.println("Misses: " + cacheMisses);
        System.out.println("Hit Rate: " + hitRate + "%");
    }

    public static void main(String[] args) throws Exception {

        problem3 dns = new problem3();

        dns.resolve("google.com");
        dns.resolve("google.com");

        Thread.sleep(11000);

        dns.resolve("google.com");

        dns.getCacheStats();
    }
}
