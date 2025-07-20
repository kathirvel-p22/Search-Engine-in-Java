import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class ConsoleSearchEngine {
    
    private static LinkedHashMap<String, HashSet> searchIndex;
    
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java ConsoleSearchEngine <input_file>");
            System.out.println("Example: java ConsoleSearchEngine TestInput/itcwww-tiny.txt");
            return;
        }
        
        // Initialize the search index from the input file
        System.out.println("Loading search index from: " + args[0]);
        searchIndex = Setup.initialise(args[0]);
        
        if (searchIndex == null || searchIndex.isEmpty()) {
            System.out.println("Failed to load search index. Please check the input file.");
            return;
        }
        
        System.out.println("Search index loaded successfully!");
        System.out.println("Available words in index: " + searchIndex.size());
        
        // Start interactive search
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n=== Java Search Engine ===");
        System.out.println("Enter search queries (or 'quit' to exit):");
        System.out.println("Supported operations:");
        System.out.println("  - Single word: 'word'");
        System.out.println("  - AND search: 'word1 AND word2'");
        System.out.println("  - OR search: 'word1 OR word2'");
        System.out.println();
        
        while (true) {
            System.out.print("Search> ");
            String query = scanner.nextLine().trim();
            
            if (query.equalsIgnoreCase("quit") || query.equalsIgnoreCase("exit")) {
                System.out.println("Goodbye!");
                break;
            }
            
            if (query.isEmpty()) {
                continue;
            }
            
            // Perform search
            HashSet<String> results = Searcher.search(query, searchIndex);
            
            // Display results
            displayResults(query, results);
        }
        
        scanner.close();
    }
    
    private static void displayResults(String query, HashSet<String> results) {
        System.out.println("\nSearch query: \"" + query + "\"");
        System.out.println("----------------------------------------");
        
        if (results == null || results.isEmpty()) {
            System.out.println("No results found.");
            
            // Try to suggest similar words using Levenshtein distance
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
}
