
import java.util.*;

class TokenBucket {

    int tokens;
    int maxTokens;
    double refillRate; // tokens per second
    long lastRefillTime;

    public TokenBucket(int maxTokens, double refillRate) {
        this.maxTokens = maxTokens;
        this.tokens = maxTokens;
        this.refillRate = refillRate;
        this.lastRefillTime = System.currentTimeMillis();
    }

    // Refill tokens based on elapsed time
    private void refill() {

        long now = System.currentTimeMillis();
        double seconds = (now - lastRefillTime) / 1000.0;

        int refillTokens = (int) (seconds * refillRate);

        if (refillTokens > 0) {
            tokens = Math.min(maxTokens, tokens + refillTokens);
            lastRefillTime = now;
        }
    }

    // Try consuming one token
    public synchronized boolean allowRequest() {

        refill();

        if (tokens > 0) {
            tokens--;
            return true;
        }

        return false;
    }

    public int getRemainingTokens() {
        refill();
        return tokens;
    }
}

public class problem6 {

    // clientId → token bucket
    private Map<String, TokenBucket> clientBuckets;

    private int maxRequests;
    private int windowSeconds;

    public problem6(int maxRequests, int windowSeconds) {

        this.maxRequests = maxRequests;
        this.windowSeconds = windowSeconds;

        clientBuckets = new HashMap<>();
    }

    public synchronized boolean checkRateLimit(String clientId) {

        TokenBucket bucket = clientBuckets.get(clientId);

        if (bucket == null) {

            double refillRate = (double) maxRequests / windowSeconds;

            bucket = new TokenBucket(maxRequests, refillRate);

            clientBuckets.put(clientId, bucket);
        }

        boolean allowed = bucket.allowRequest();

        if (allowed) {
            System.out.println("Allowed (" +
                    bucket.getRemainingTokens() +
                    " requests remaining)");
        } else {
            System.out.println("Denied (Rate limit exceeded)");
        }

        return allowed;
    }

    public void getRateLimitStatus(String clientId) {

        TokenBucket bucket = clientBuckets.get(clientId);

        if (bucket == null) {
            System.out.println("Client has no requests yet.");
            return;
        }

        int used = maxRequests - bucket.getRemainingTokens();

        System.out.println("{used: " + used +
                ", limit: " + maxRequests +
                ", remaining: " + bucket.getRemainingTokens() +
                "}");
    }

    public static void main(String[] args) {

        problem6 limiter = new problem6(5, 60); // 5 requests per minute

        String client = "abc123";

        for (int i = 0; i < 7; i++) {

            limiter.checkRateLimit(client);
        }

        limiter.getRateLimitStatus(client);
    }
}
