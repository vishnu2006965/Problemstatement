import java.util.*;

class TokenBucket {

    double tokens;
    double maxTokens;
    double refillRate;
    long lastRefillTime;

    public TokenBucket(double maxTokens, double refillRate) {

        this.maxTokens = maxTokens;
        this.refillRate = refillRate;
        this.tokens = maxTokens;
        this.lastRefillTime = System.currentTimeMillis();
    }

    // refill tokens based on time
    private void refill() {

        long now = System.currentTimeMillis();

        double seconds = (now - lastRefillTime) / 1000.0;

        double newTokens = seconds * refillRate;

        tokens = Math.min(maxTokens, tokens + newTokens);

        lastRefillTime = now;
    }

    public synchronized boolean allowRequest() {

        refill();

        if (tokens >= 1) {
            tokens--;
            return true;
        }

        return false;
    }

    public int getRemaining() {
        return (int) tokens;
    }
}

public class app6 {

    // clientId → token bucket
    private HashMap<String, TokenBucket> clientBuckets = new HashMap<>();

    private static final int MAX_REQUESTS = 1000;
    private static final double REFILL_RATE = MAX_REQUESTS / 3600.0;

    public synchronized String checkRateLimit(String clientId) {

        clientBuckets.putIfAbsent(
                clientId,
                new TokenBucket(MAX_REQUESTS, REFILL_RATE)
        );

        TokenBucket bucket = clientBuckets.get(clientId);

        if (bucket.allowRequest()) {

            return "Allowed (" +
                    bucket.getRemaining() +
                    " requests remaining)";
        } else {

            return "Denied (Rate limit exceeded)";
        }
    }

    public void getRateLimitStatus(String clientId) {

        TokenBucket bucket = clientBuckets.get(clientId);

        if (bucket == null) {
            System.out.println("Client not found");
            return;
        }

        int used = MAX_REQUESTS - bucket.getRemaining();

        System.out.println(
                "{used: " + used +
                        ", limit: " + MAX_REQUESTS +
                        "}"
        );
    }

    public static void main(String[] args) {

        app6 limiter = new app6();

        String client = "abc123";

        for (int i = 0; i < 5; i++) {

            System.out.println(
                    limiter.checkRateLimit(client)
            );
        }

        limiter.getRateLimitStatus(client);
    }
}