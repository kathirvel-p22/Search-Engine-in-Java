import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class RealWorldSearchEngine extends JFrame {
    
    private LinkedHashMap<String, HashSet> localIndex;
    private LinkedHashMap<String, HashSet> webIndex;
    private JTextField searchField;
    private JTextArea resultsArea;
    private JLabel statusLabel;
    private JProgressBar progressBar;
    private JButton searchButton, loadButton, crawlButton;
    private JComboBox<String> datasetCombo;
    private JTextField urlField;
    private WebCrawler crawler;
    
    // Colors
    private Color primaryColor = new Color(0, 123, 255);
    private Color successColor = new Color(40, 167, 69);
    private Color warningColor = new Color(255, 193, 7);
    private Color dangerColor = new Color(220, 53, 69);
    private Color webColor = new Color(138, 43, 226);
    
    public RealWorldSearchEngine() {
        crawler = new WebCrawler();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    private void initializeComponents() {
        setTitle("Real-World Java Search Engine");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 900);
        setLocationRelativeTo(null);
        
        searchField = new JTextField(30);
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        
        urlField = new JTextField("https://example.com", 25);
        urlField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        searchButton = createButton("Search All", primaryColor);
        loadButton = createButton("Load Local", successColor);
        crawlButton = createButton("Crawl Web", webColor);
        
        String[] datasets = {
            "TestInput/itcwww-tiny.txt",
            "TestInput/itcwww-small.txt", 
            "TestInput/itcwww-medium.txt",
            "TestInput/itcwww-big.txt"
        };
        datasetCombo = new JComboBox<>(datasets);
        
        resultsArea = new JTextArea(25, 80);
        resultsArea.setEditable(false);
        resultsArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        
        statusLabel = new JLabel("Ready! Load local data or crawl web for real-world search.");
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);
    }
    
    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        return button;
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(new EmptyBorder(15, 15, 10, 15));
        
        JLabel titleLabel = new JLabel("Real-World Java Search Engine", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(primaryColor);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Controls
        JPanel controlsPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        
        JPanel localPanel = new JPanel(new FlowLayout());
        localPanel.add(new JLabel("Local Dataset:"));
        localPanel.add(datasetCombo);
        localPanel.add(loadButton);
        
        JPanel webPanel = new JPanel(new FlowLayout());
        webPanel.add(new JLabel("Web Crawl URL:"));
        webPanel.add(urlField);
        webPanel.add(crawlButton);
        
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(new JLabel("Search Query:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        controlsPanel.add(localPanel);
        controlsPanel.add(webPanel);
        controlsPanel.add(searchPanel);
        
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(15));
        headerPanel.add(controlsPanel);
        headerPanel.add(progressBar);
        
        // Results
        JScrollPane scrollPane = new JScrollPane(resultsArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Search Results"));
        
        // Status
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
        statusPanel.add(statusLabel, BorderLayout.WEST);
        
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);
    }
    
    private void setupEventHandlers() {
        loadButton.addActionListener(e -> loadLocalDataset());
        crawlButton.addActionListener(e -> crawlWebData());
        searchButton.addActionListener(e -> performComprehensiveSearch());
        
        searchField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performComprehensiveSearch();
                }
            }
        });
    }
    
    private void loadLocalDataset() {
        String dataset = (String) datasetCombo.getSelectedItem();
        
        SwingWorker<LinkedHashMap<String, HashSet>, Void> worker = new SwingWorker<LinkedHashMap<String, HashSet>, Void>() {
            protected LinkedHashMap<String, HashSet> doInBackground() throws Exception {
                SwingUtilities.invokeLater(() -> {
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                    progressBar.setString("Loading local dataset...");
                    statusLabel.setText("Loading: " + dataset);
                });
                return Setup.initialise(dataset);
            }
            
            protected void done() {
                try {
                    localIndex = get();
                    progressBar.setVisible(false);
                    if (localIndex != null) {
                        statusLabel.setText("Local dataset loaded: " + localIndex.size() + " words indexed");
                        statusLabel.setForeground(successColor);
                    }
                } catch (Exception e) {
                    statusLabel.setText("Error loading dataset: " + e.getMessage());
                    statusLabel.setForeground(dangerColor);
                    progressBar.setVisible(false);
                }
            }
        };
        worker.execute();
    }
    
    private void crawlWebData() {
        String url = urlField.getText().trim();
        if (url.isEmpty()) {
            statusLabel.setText("Please enter a valid URL to crawl");
            statusLabel.setForeground(warningColor);
            return;
        }
        
        SwingWorker<LinkedHashMap<String, HashSet>, Void> worker = new SwingWorker<LinkedHashMap<String, HashSet>, Void>() {
            protected LinkedHashMap<String, HashSet> doInBackground() throws Exception {
                SwingUtilities.invokeLater(() -> {
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                    progressBar.setString("Crawling web data...");
                    statusLabel.setText("Crawling: " + url);
                });
                LinkedHashMap<String, HashSet<String>> crawlResult = crawler.crawlWebsite(url, 2);
                LinkedHashMap<String, HashSet> result = new LinkedHashMap<>();
                for (Map.Entry<String, HashSet<String>> entry : crawlResult.entrySet()) {
                    result.put(entry.getKey(), entry.getValue());
                }
                return result;
            }
            
            protected void done() {
                try {
                    webIndex = get();
                    progressBar.setVisible(false);
                    if (webIndex != null) {
                        statusLabel.setText("Web crawl completed: " + webIndex.size() + " words from live web data");
                        statusLabel.setForeground(successColor);
                        
                        // Save web data
                        crawler.saveIndexToFile("WebData/crawled-" + System.currentTimeMillis() + ".txt");
                    }
                } catch (Exception e) {
                    statusLabel.setText("Web crawl error: " + e.getMessage());
                    statusLabel.setForeground(dangerColor);
                    progressBar.setVisible(false);
                }
            }
        };
        worker.execute();
    }
    
    private void performComprehensiveSearch() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            statusLabel.setText("Please enter a search query");
            statusLabel.setForeground(warningColor);
            return;
        }
        
        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            protected String doInBackground() throws Exception {
                SwingUtilities.invokeLater(() -> {
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                    progressBar.setString("Searching all sources...");
                });
                
                StringBuilder results = new StringBuilder();
                for(int i = 0; i < 80; i++) results.append("=");
                results.append("\n");
                results.append("COMPREHENSIVE SEARCH RESULTS FOR: \"").append(query).append("\"\n");
                for(int i = 0; i < 80; i++) results.append("=");
                results.append("\n\n");
                
                // Search local dataset
                if (localIndex != null) {
                    HashSet<String> localResults = Searcher.search(query, localIndex);
                    results.append("LOCAL DATASET RESULTS:\n");
                    results.append("-".repeat(40)).append("\n");
                    if (localResults != null && !localResults.isEmpty()) {
                        results.append("Found ").append(localResults.size()).append(" results:\n");
                        int count = 1;
                        for (String url : localResults) {
                            results.append(count++).append(". ").append(url).append("\n");
                            if (count > 20) break;
                        }
                    } else {
                        results.append("No results found in local dataset.\n");
                    }
                    results.append("\n");
                }
                
                // Search web data
                if (webIndex != null) {
                    HashSet<String> webResults = Searcher.search(query, webIndex);
                    results.append("LIVE WEB DATA RESULTS:\n");
                    results.append("-".repeat(40)).append("\n");
                    if (webResults != null && !webResults.isEmpty()) {
                        results.append("Found ").append(webResults.size()).append(" results from crawled web data:\n");
                        int count = 1;
                        for (String url : webResults) {
                            results.append(count++).append(". ").append(url).append("\n");
                            if (count > 20) break;
                        }
                    } else {
                        results.append("No results found in web data.\n");
                    }
                    results.append("\n");
                }
                
                // Add search suggestions
                results.append("SEARCH ENHANCEMENT SUGGESTIONS:\n");
                results.append("-".repeat(40)).append("\n");
                results.append("• Try web search: https://www.google.com/search?q=").append(query.replace(" ", "+")).append("\n");
                results.append("• Try academic search: https://scholar.google.com/scholar?q=").append(query.replace(" ", "+")).append("\n");
                results.append("• Try Wikipedia: https://en.wikipedia.org/wiki/Special:Search/").append(query.replace(" ", "_")).append("\n");
                results.append("• Try GitHub: https://github.com/search?q=").append(query.replace(" ", "+")).append("\n\n");
                
                // Add statistics
                results.append("SEARCH STATISTICS:\n");
                results.append("-".repeat(40)).append("\n");
                results.append("• Query: \"").append(query).append("\"\n");
                results.append("• Search time: ").append(new Date()).append("\n");
                results.append("• Local index size: ").append(localIndex != null ? localIndex.size() : 0).append(" words\n");
                results.append("• Web index size: ").append(webIndex != null ? webIndex.size() : 0).append(" words\n");
                results.append("• Total searchable content: ").append((localIndex != null ? localIndex.size() : 0) + (webIndex != null ? webIndex.size() : 0)).append(" unique words\n");
                
                return results.toString();
            }
            
            protected void done() {
                try {
                    String searchResults = get();
                    resultsArea.setText(searchResults);
                    resultsArea.setCaretPosition(0);
                    progressBar.setVisible(false);
                    statusLabel.setText("Comprehensive search completed for: \"" + query + "\"");
                    statusLabel.setForeground(successColor);
                } catch (Exception e) {
                    statusLabel.setText("Search error: " + e.getMessage());
                    statusLabel.setForeground(dangerColor);
                    progressBar.setVisible(false);
                }
            }
        };
        worker.execute();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RealWorldSearchEngine().setVisible(true);
        });
    }
}
