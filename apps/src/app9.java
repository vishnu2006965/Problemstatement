import java.util.*;

class TokenBucket{

    int maxTokens;
    double tokens;
    double refillRate;
    long lastRefillTime;

    public TokenBucket(int maxTokens, double refillRate) {
        this.maxTokens = maxTokens;
        this.tokens = maxTokens;
        this.refillRate = refillRate;
        this.lastRefillTime = System.currentTimeMillis();
    }

    // Refill tokens based on time passed
    private void refill() {

        long now = System.currentTimeMillis();
        double seconds = (now - lastRefillTime) / 1000.0;

        double tokensToAdd = seconds * refillRate;

        tokens = Math.min(maxTokens, tokens + tokensToAdd);

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

    public int getRemainingTokens() {
        return (int) tokens;
    }
}

public class app9
{

    // Hash table for client tracking
    private HashMap<String, TokenBucket> clientBuckets = new HashMap<>();

    private static final int LIMIT = 1000;
    private static final double REFILL_RATE = LIMIT / 3600.0; // tokens per second

    // Check rate limit
    public synchronized String checkRateLimit(String clientId) {

        clientBuckets.putIfAbsent(clientId, new TokenBucket(LIMIT, REFILL_RATE));

        TokenBucket bucket = clientBuckets.get(clientId);

        if (bucket.allowRequest()) {

            return "Allowed (" + bucket.getRemainingTokens() + " requests remaining)";
        } else {

            return "Denied (Rate limit exceeded)";
        }
    }

    // Show rate limit status
    public void getRateLimitStatus(String clientId) {

        TokenBucket bucket = clientBuckets.get(clientId);

        if (bucket == null) {
            System.out.println("Client not found");
            return;
        }

        int used = LIMIT - bucket.getRemainingTokens();

        long resetTime = bucket.lastRefillTime + 3600 * 1000;

        System.out.println(
                "{used: " + used +
                        ", limit: " + LIMIT +
                        ", reset: " + resetTime + "}"
        );
    }

    public static void main(String[] args) {

        app9 limiter = new app9();

        String client = "abc123";

        System.out.println(limiter.checkRateLimit(client));
        System.out.println(limiter.checkRateLimit(client));
        System.out.println(limiter.checkRateLimit(client));

        limiter.getRateLimitStatus(client);
    }
}