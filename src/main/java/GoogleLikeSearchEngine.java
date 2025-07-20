import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.*;
import javax.swing.Timer;
import java.util.List;

public class GoogleLikeSearchEngine extends JFrame {
    
    private JTextField searchField;
    private JPanel resultsPanel;
    private JScrollPane scrollPane;
    private JLabel statusLabel;
    private JProgressBar progressBar;
    private JButton searchButton;
    private JButton feelingLuckyButton;
    private Timer suggestionTimer;
    
    // Google-like colors
    private Color googleBlue = new Color(66, 133, 244);
    private Color googleRed = new Color(234, 67, 53);
    private Color googleYellow = new Color(251, 188, 5);
    private Color googleGreen = new Color(52, 168, 83);
    private Color lightGray = new Color(248, 249, 250);
    
    public GoogleLikeSearchEngine() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupAutoSuggestions();
    }
    
    private void initializeComponents() {
        setTitle("Java Search Engine - Google Style");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setBackground(Color.WHITE);
        
        // Search field with Google-like styling
        searchField = new JTextField();
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(223, 225, 229), 1),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        searchField.setPreferredSize(new Dimension(500, 44));
        
        // Google-style buttons
        searchButton = createGoogleButton("Search", googleBlue);
        feelingLuckyButton = createGoogleButton("I'm Feeling Lucky", lightGray);
        feelingLuckyButton.setForeground(Color.BLACK);
        
        // Results panel
        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setBackground(Color.WHITE);
        
        scrollPane = new JScrollPane(resultsPanel);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        // Status and progress
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        statusLabel.setForeground(new Color(112, 117, 122));
        
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);
        progressBar.setPreferredSize(new Dimension(400, 3));
        progressBar.setBackground(Color.WHITE);
        progressBar.setForeground(googleBlue);
    }
    
    private JButton createGoogleButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Header with Google-like logo and search
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(40, 20, 30, 20));
        
        // Logo
        JLabel logoLabel = new JLabel("Java Search", JLabel.CENTER);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 48));
        logoLabel.setForeground(googleBlue);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Search box container
        JPanel searchContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        searchContainer.setBackground(Color.WHITE);
        searchContainer.add(searchField);
        
        // Buttons container
        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonContainer.setBackground(Color.WHITE);
        buttonContainer.add(searchButton);
        buttonContainer.add(feelingLuckyButton);
        
        // Progress bar container
        JPanel progressContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        progressContainer.setBackground(Color.WHITE);
        progressContainer.add(progressBar);
        
        headerPanel.add(logoLabel);
        headerPanel.add(Box.createVerticalStrut(30));
        headerPanel.add(searchContainer);
        headerPanel.add(buttonContainer);
        headerPanel.add(progressContainer);
        
        // Results area
        JPanel resultsContainer = new JPanel(new BorderLayout());
        resultsContainer.setBackground(Color.WHITE);
        resultsContainer.setBorder(new EmptyBorder(0, 50, 20, 50));
        resultsContainer.add(scrollPane, BorderLayout.CENTER);
        
        // Status bar
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(lightGray);
        statusPanel.setBorder(new EmptyBorder(10, 50, 10, 50));
        statusPanel.add(statusLabel, BorderLayout.WEST);
        
        add(headerPanel, BorderLayout.NORTH);
        add(resultsContainer, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);
    }
    
    private void setupEventHandlers() {
        searchButton.addActionListener(e -> performWebSearch());
        feelingLuckyButton.addActionListener(e -> performLuckySearch());
        
        searchField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performWebSearch();
                }
            }
        });
    }
    
    private void setupAutoSuggestions() {
        // Auto-suggestion functionality (simplified)
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (suggestionTimer != null) {
                    suggestionTimer.stop();
                }
                suggestionTimer = new Timer(300, evt -> {
                    String query = searchField.getText().trim();
                    if (query.length() > 2) {
                        // In a real implementation, this would fetch suggestions from a web service
                        updateSuggestionHint(query);
                    }
                });
                suggestionTimer.setRepeats(false);
                suggestionTimer.start();
            }
        });
    }
    
    private void updateSuggestionHint(String query) {
        // Simple suggestion hint (in real implementation, would use web API)
        String[] commonSuggestions = {
            "java programming", "java tutorial", "java examples", 
            "web development", "search algorithms", "data structures"
        };
        
        for (String suggestion : commonSuggestions) {
            if (suggestion.toLowerCase().startsWith(query.toLowerCase())) {
                statusLabel.setText("Suggestion: " + suggestion);
                return;
            }
        }
        statusLabel.setText(" ");
    }
    
    private void performWebSearch() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            statusLabel.setText("Please enter a search query");
            return;
        }
        
        SwingWorker<List<SearchResult>, Void> worker = new SwingWorker<List<SearchResult>, Void>() {
            @Override
            protected List<SearchResult> doInBackground() throws Exception {
                SwingUtilities.invokeLater(() -> {
                    progressBar.setVisible(true);
                    statusLabel.setText("Searching the web...");
                    resultsPanel.removeAll();
                    resultsPanel.revalidate();
                    resultsPanel.repaint();
                });
                
                return searchWeb(query);
            }
            
            @Override
            protected void done() {
                try {
                    List<SearchResult> results = get();
                    displaySearchResults(query, results);
                    progressBar.setVisible(false);
                } catch (Exception e) {
                    statusLabel.setText("Search error: " + e.getMessage());
                    progressBar.setVisible(false);
                }
            }
        };
        
        worker.execute();
    }
    
    private void performLuckySearch() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            statusLabel.setText("Please enter a search query");
            return;
        }
        
        // Open first result directly (Google's "I'm Feeling Lucky")
        try {
            String searchUrl = "https://www.google.com/search?btnI=1&q=" + URLEncoder.encode(query, "UTF-8");
            Desktop.getDesktop().browse(new URI(searchUrl));
            statusLabel.setText("Opening first result for: " + query);
        } catch (Exception e) {
            statusLabel.setText("Error opening lucky search: " + e.getMessage());
        }
    }
    
    private List<SearchResult> searchWeb(String query) {
        List<SearchResult> results = new ArrayList<>();
        
        try {
            // Search multiple sources
            results.addAll(searchWikipedia(query));
            results.addAll(searchStackOverflow(query));
            results.addAll(searchGitHub(query));
            
            // Add web search suggestions
            results.add(new SearchResult(
                "Search Google for: " + query,
                "https://www.google.com/search?q=" + URLEncoder.encode(query, "UTF-8"),
                "Get comprehensive web results from Google's search engine",
                "Google Search"
            ));
            
            results.add(new SearchResult(
                "Search Bing for: " + query,
                "https://www.bing.com/search?q=" + URLEncoder.encode(query, "UTF-8"),
                "Alternative web search results from Microsoft Bing",
                "Bing Search"
            ));
            
            // Rank results by relevance (simplified ranking algorithm)
            rankResults(results, query);
            
        } catch (Exception e) {
            System.err.println("Search error: " + e.getMessage());
        }
        
        return results;
    }
    
    private List<SearchResult> searchWikipedia(String query) {
        List<SearchResult> results = new ArrayList<>();
        try {
            String encodedQuery = URLEncoder.encode(query, "UTF-8");
            String searchUrl = "https://en.wikipedia.org/w/api.php?action=query&list=search&srsearch=" + 
                              encodedQuery + "&format=json&srlimit=3";
            
            results.add(new SearchResult(
                query + " - Wikipedia",
                "https://en.wikipedia.org/wiki/Special:Search/" + encodedQuery,
                "Encyclopedia articles and information about " + query,
                "Wikipedia"
            ));
            
        } catch (Exception e) {
            System.err.println("Wikipedia search error: " + e.getMessage());
        }
        return results;
    }
    
    private List<SearchResult> searchStackOverflow(String query) {
        List<SearchResult> results = new ArrayList<>();
        try {
            String encodedQuery = URLEncoder.encode(query, "UTF-8");
            results.add(new SearchResult(
                query + " - Stack Overflow",
                "https://stackoverflow.com/search?q=" + encodedQuery,
                "Programming questions and answers related to " + query,
                "Stack Overflow"
            ));
        } catch (Exception e) {
            System.err.println("Stack Overflow search error: " + e.getMessage());
        }
        return results;
    }
    
    private List<SearchResult> searchGitHub(String query) {
        List<SearchResult> results = new ArrayList<>();
        try {
            String encodedQuery = URLEncoder.encode(query, "UTF-8");
            results.add(new SearchResult(
                query + " - GitHub",
                "https://github.com/search?q=" + encodedQuery,
                "Code repositories and projects related to " + query,
                "GitHub"
            ));
        } catch (Exception e) {
            System.err.println("GitHub search error: " + e.getMessage());
        }
        return results;
    }
    
    private void rankResults(List<SearchResult> results, String query) {
        // Simple ranking algorithm based on relevance
        results.sort((a, b) -> {
            int scoreA = calculateRelevanceScore(a, query);
            int scoreB = calculateRelevanceScore(b, query);
            return Integer.compare(scoreB, scoreA); // Higher score first
        });
    }
    
    private int calculateRelevanceScore(SearchResult result, String query) {
        int score = 0;
        String lowerQuery = query.toLowerCase();
        String lowerTitle = result.title.toLowerCase();
        String lowerDescription = result.description.toLowerCase();
        
        // Title matches get higher score
        if (lowerTitle.contains(lowerQuery)) score += 10;
        
        // Description matches
        if (lowerDescription.contains(lowerQuery)) score += 5;
        
        // Source preference (Wikipedia and Stack Overflow get bonus)
        if (result.source.equals("Wikipedia")) score += 3;
        if (result.source.equals("Stack Overflow")) score += 2;
        
        return score;
    }
    
    private void displaySearchResults(String query, List<SearchResult> results) {
        resultsPanel.removeAll();
        
        // Search info
        JLabel searchInfo = new JLabel("About " + results.size() + " results for \"" + query + "\"");
        searchInfo.setFont(new Font("Arial", Font.PLAIN, 13));
        searchInfo.setForeground(new Color(112, 117, 122));
        searchInfo.setBorder(new EmptyBorder(10, 0, 20, 0));
        resultsPanel.add(searchInfo);
        
        // Display results
        for (SearchResult result : results) {
            JPanel resultPanel = createResultPanel(result);
            resultsPanel.add(resultPanel);
            resultsPanel.add(Box.createVerticalStrut(20));
        }
        
        resultsPanel.revalidate();
        resultsPanel.repaint();
        
        statusLabel.setText("Search completed - " + results.size() + " results found");
    }
    
    private JPanel createResultPanel(SearchResult result) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // URL
        JLabel urlLabel = new JLabel(result.url);
        urlLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        urlLabel.setForeground(new Color(26, 115, 232));
        urlLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Title (clickable)
        JLabel titleLabel = new JLabel("<html><u>" + result.title + "</u></html>");
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        titleLabel.setForeground(new Color(26, 13, 171));
        titleLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        titleLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(result.url));
                } catch (Exception ex) {
                    statusLabel.setText("Error opening link: " + ex.getMessage());
                }
            }
            
            public void mouseEntered(MouseEvent e) {
                titleLabel.setForeground(googleBlue);
            }
            
            public void mouseExited(MouseEvent e) {
                titleLabel.setForeground(new Color(26, 13, 171));
            }
        });
        
        // Description
        JLabel descLabel = new JLabel("<html>" + result.description + "</html>");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        descLabel.setForeground(new Color(112, 117, 122));
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(urlLabel);
        panel.add(Box.createVerticalStrut(2));
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(descLabel);
        
        return panel;
    }
    
    // Search result data class
    private static class SearchResult {
        String title;
        String url;
        String description;
        String source;
        
        SearchResult(String title, String url, String description, String source) {
            this.title = title;
            this.url = url;
            this.description = description;
            this.source = source;
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel for better appearance
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            new GoogleLikeSearchEngine().setVisible(true);
        });
    }
}
