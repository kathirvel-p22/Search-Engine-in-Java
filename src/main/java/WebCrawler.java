import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebCrawler {
    
    private static final int MAX_PAGES = 50;
    private static final int TIMEOUT = 5000;
    private Set<String> visitedUrls = new HashSet<>();
    private LinkedHashMap<String, HashSet<String>> webIndex = new LinkedHashMap<>();
    
    public LinkedHashMap<String, HashSet<String>> crawlWebsite(String startUrl, int maxDepth) {
        System.out.println("Starting web crawl from: " + startUrl);
        crawlPage(startUrl, 0, maxDepth);
        System.out.println("Web crawl completed. Indexed " + webIndex.size() + " unique words from " + visitedUrls.size() + " pages.");
        return webIndex;
    }
    
    private void crawlPage(String url, int currentDepth, int maxDepth) {
        if (currentDepth > maxDepth || visitedUrls.size() >= MAX_PAGES || visitedUrls.contains(url)) {
            return;
        }
        
        try {
            visitedUrls.add(url);
            System.out.println("Crawling: " + url + " (Depth: " + currentDepth + ")");
            
            String content = fetchPageContent(url);
            if (content != null) {
                extractWordsFromContent(content, url);
                
                if (currentDepth < maxDepth) {
                    Set<String> links = extractLinks(content, url);
                    for (String link : links) {
                        if (visitedUrls.size() < MAX_PAGES) {
                            crawlPage(link, currentDepth + 1, maxDepth);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error crawling " + url + ": " + e.getMessage());
        }
    }
    
    private String fetchPageContent(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(TIMEOUT);
            connection.setReadTimeout(TIMEOUT);
            connection.setRequestProperty("User-Agent", "Java Search Engine Bot 1.0");
            
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder content = new StringBuilder();
                String line;
                
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                reader.close();
                return content.toString();
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch: " + urlString + " - " + e.getMessage());
        }
        return null;
    }
    
    private void extractWordsFromContent(String content, String url) {
        // Remove HTML tags
        String textContent = content.replaceAll("<[^>]+>", " ");
        
        // Extract words (alphanumeric sequences)
        Pattern wordPattern = Pattern.compile("\\b[a-zA-Z0-9]+\\b");
        Matcher matcher = wordPattern.matcher(textContent.toLowerCase());
        
        while (matcher.find()) {
            String word = matcher.group();
            if (word.length() >= 3) { // Only index words with 3+ characters
                webIndex.computeIfAbsent(word, k -> new HashSet<>()).add(url);
            }
        }
    }
    
    private Set<String> extractLinks(String content, String baseUrl) {
        Set<String> links = new HashSet<>();
        Pattern linkPattern = Pattern.compile("href=[\"']([^\"']+)[\"']", Pattern.CASE_INSENSITIVE);
        Matcher matcher = linkPattern.matcher(content);
        
        while (matcher.find()) {
            String link = matcher.group(1);
            String absoluteUrl = resolveUrl(link, baseUrl);
            if (absoluteUrl != null && isValidUrl(absoluteUrl)) {
                links.add(absoluteUrl);
            }
        }
        return links;
    }
    
    private String resolveUrl(String link, String baseUrl) {
        try {
            if (link.startsWith("http")) {
                return link;
            } else if (link.startsWith("/")) {
                URL base = new URL(baseUrl);
                return base.getProtocol() + "://" + base.getHost() + link;
            } else {
                URL base = new URL(baseUrl);
                String basePath = base.getPath();
                if (!basePath.endsWith("/")) {
                    basePath = basePath.substring(0, basePath.lastIndexOf("/") + 1);
                }
                return base.getProtocol() + "://" + base.getHost() + basePath + link;
            }
        } catch (Exception e) {
            return null;
        }
    }
    
    private boolean isValidUrl(String url) {
        return url.startsWith("http") && 
               !url.contains("#") && 
               !url.endsWith(".pdf") && 
               !url.endsWith(".jpg") && 
               !url.endsWith(".png") && 
               !url.endsWith(".gif") &&
               !url.endsWith(".css") &&
               !url.endsWith(".js");
    }
    
    public void saveIndexToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Map.Entry<String, HashSet<String>> entry : webIndex.entrySet()) {
                for (String url : entry.getValue()) {
                    writer.println("*PAGE:" + url);
                    writer.println(entry.getKey());
                }
            }
            System.out.println("Web index saved to: " + filename);
        } catch (IOException e) {
            System.err.println("Error saving index: " + e.getMessage());
        }
    }
}
