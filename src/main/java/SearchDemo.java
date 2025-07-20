import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;

public class SearchDemo {
    
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java SearchDemo <input_file>");
            return;
        }
        
        // Initialize the search index
        System.out.println("=== Java Search Engine Demo ===");
        System.out.println("Loading search index from: " + args[0]);
        LinkedHashMap<String, HashSet> searchIndex = Setup.initialise(args[0]);
        
        if (searchIndex == null || searchIndex.isEmpty()) {
            System.out.println("Failed to load search index.");
            return;
        }
        
        System.out.println("Search index loaded successfully!");
        System.out.println("Available words in index: " + searchIndex.size());
        System.out.println();
        
        // Demo queries
        String[] queries = {
            "IT",
            "København", 
            "Home",
            "Find",
            "IT AND højskolen",
            "Home OR Find",
            "nonexistent"
        };
        
        for (String query : queries) {
            System.out.println("----------------------------------------");
            System.out.println("Search query: \"" + query + "\"");
            System.out.println("----------------------------------------");
            
            HashSet<String> results = Searcher.search(query, searchIndex);
            
            if (results == null || results.isEmpty()) {
                System.out.println("No results found.");
                
                // Try to suggest similar words for single word queries
                String[] queryParts = query.split(" ");
                if (queryParts.length == 1) {
                    System.out.println("\nDid you mean:");
                    HashSet<String> suggestions = SimilarWords.retrieveSimilarWords(searchIndex, queryParts[0]);
                    if (suggestions != null && !suggestions.isEmpty()) {
                        for (String suggestion : suggestions) {
                            System.out.println("  - " + suggestion);
                        }
                    } else {
                        System.out.println("  No similar words found.");
                    }
                }
            } else {
                System.out.println("Found " + results.size() + " result(s):");
                int count = 1;
                for (String url : results) {
                    System.out.println(count + ". " + url);
                    count++;
                }
            }
            System.out.println();
        }
        
        System.out.println("Demo completed!");
    }
}
