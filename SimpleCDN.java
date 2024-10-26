import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimpleCDN {

    // ContentCache class for caching content
    public static class ContentCache {
        private HashMap<String, String> cache;

        public ContentCache() {
            cache = new HashMap<>();
        }

        public void addToCache(String key, String content) {
            cache.put(key, content);
        }

        public String getFromCache(String key) {
            return cache.get(key);
        }

        public boolean isInCache(String key) {
            return cache.containsKey(key);
        }
    }

    // LoadBalancer class for distributing requests among servers
    public static class LoadBalancer {
        private List<String> servers;
        private Random random;

        public LoadBalancer() {
            servers = new ArrayList<>();
            random = new Random();
        }

        public void addServer(String server) {
            servers.add(server);
        }

        public String getServer() {
            int index = random.nextInt(servers.size());
            return servers.get(index);
        }
    }

    // ContentDeliverySystem class for managing caching and delivery
    public static class ContentDeliverySystem {
        private ContentCache cache;
        private LoadBalancer loadBalancer;

        public ContentDeliverySystem() {
            cache = new ContentCache();
            loadBalancer = new LoadBalancer();
        }

        public void addServer(String server) {
            loadBalancer.addServer(server);
        }

        public String requestContent(String key) {
            // Check cache first
            if (cache.isInCache(key)) {
                return cache.getFromCache(key);
            }

            // If not in cache, get content from a server
            String server = loadBalancer.getServer();
            String content = getContentFromServer(server, key);

            // Cache the content
            cache.addToCache(key, content);

            return content;
        }

        private String getContentFromServer(String server, String key) {
            // Simulate fetching content from a server
            return "Content from " + server + " for key " + key;
        }
    }

    // Main method to demonstrate the functionality
    public static void main(String[] args) {
        ContentDeliverySystem system = new ContentDeliverySystem();

        // Add servers to load balancer
        system.addServer("Server1");
        system.addServer("Server2");
        system.addServer("Server3");

        // Request content
        String content = system.requestContent("exampleKey");
        System.out.println(content);

        // Request the same content again to test caching
        content = system.requestContent("exampleKey");
        System.out.println(content);
    }
}
