import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.LinkedHashMap;

public class WebBrowserSearchGUI extends JFrame {
    
    private LinkedHashMap<String, HashSet> searchIndex;
    private JTextField searchField;
    private JTextArea resultsArea;
    private JLabel statusLabel;
    private JProgressBar progressBar;
    private JButton searchButton;
    private JButton loadButton;
    private JButton browseButton;
    private JComboBox<String> datasetCombo;
    private JTextField urlField;
    private Timer pulseTimer;
    private boolean isPulsing = false;
    
    // Colors
    private Color primaryColor = new Color(0, 123, 255);
    private Color successColor = new Color(40, 167, 69);
    private Color warningColor = new Color(255, 193, 7);
    private Color dangerColor = new Color(220, 53, 69);
    private Color browserColor = new Color(138, 43, 226);
    
    public WebBrowserSearchGUI() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupAnimations();
    }
    
    private void initializeComponents() {
        setTitle("Java Search Engine with Web Browser");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);
        
        // Search field with custom styling
        searchField = new JTextField(25);
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(primaryColor, 2),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        // URL field for direct browsing
        urlField = new JTextField(30);
        urlField.setFont(new Font("Arial", Font.PLAIN, 14));
        urlField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(browserColor, 2),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        urlField.setText("https://www.google.com");
        
        // Styled buttons
        searchButton = createStyledButton("Search", primaryColor);
        loadButton = createStyledButton("Load Dataset", successColor);
        browseButton = createStyledButton("Browse Web", browserColor);
        
        // Dataset selection
        String[] datasets = {
            "TestInput/itcwww-tiny.txt",
            "TestInput/itcwww-small.txt", 
            "TestInput/itcwww-medium.txt",
            "TestInput/itcwww-big.txt"
        };
        datasetCombo = new JComboBox<>(datasets);
        datasetCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        datasetCombo.setPreferredSize(new Dimension(200, 35));
        
        // Results area
        resultsArea = new JTextArea(15, 60);
        resultsArea.setEditable(false);
        resultsArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        resultsArea.setBackground(new Color(248, 249, 250));
        resultsArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Status and progress
        statusLabel = new JLabel("Ready! Load a dataset to search, or browse the web directly.");
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        statusLabel.setForeground(Color.GRAY);
        
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);
        progressBar.setPreferredSize(new Dimension(400, 25));
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(130, 40));
        
        // Hover effect
        Color hoverColor = bgColor.brighter();
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(15, 15));
        
        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(new EmptyBorder(20, 20, 10, 20));
        headerPanel.setBackground(Color.WHITE);
        
        // Title
        JLabel titleLabel = new JLabel("Java Search Engine with Web Browser", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(primaryColor);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Dataset Panel
        JPanel datasetPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        datasetPanel.setBackground(Color.WHITE);
        datasetPanel.add(new JLabel("Dataset:"));
        datasetPanel.add(datasetCombo);
        datasetPanel.add(loadButton);
        
        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        // Web Browser Panel
        JPanel browserPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        browserPanel.setBackground(new Color(248, 249, 250));
        browserPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(browserColor),
            "Web Browser",
            0, 0,
            new Font("Arial", Font.BOLD, 12),
            browserColor
        ));
        browserPanel.add(new JLabel("URL:"));
        browserPanel.add(urlField);
        browserPanel.add(browseButton);
        
        // Quick Links Panel
        JPanel quickLinksPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        quickLinksPanel.setBackground(new Color(248, 249, 250));
        
        JButton googleBtn = createQuickLinkButton("Google", "https://www.google.com");
        JButton githubBtn = createQuickLinkButton("GitHub", "https://www.github.com");
        JButton stackBtn = createQuickLinkButton("Stack Overflow", "https://stackoverflow.com");
        JButton wikiBtn = createQuickLinkButton("Wikipedia", "https://www.wikipedia.org");
        
        quickLinksPanel.add(new JLabel("Quick Links:"));
        quickLinksPanel.add(googleBtn);
        quickLinksPanel.add(githubBtn);
        quickLinksPanel.add(stackBtn);
        quickLinksPanel.add(wikiBtn);
        
        browserPanel.add(quickLinksPanel);
        
        // Progress Panel
        JPanel progressPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        progressPanel.setBackground(Color.WHITE);
        progressPanel.add(progressBar);
        
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(15));
        headerPanel.add(datasetPanel);
        headerPanel.add(searchPanel);
        headerPanel.add(browserPanel);
        headerPanel.add(progressPanel);
        
        // Results Panel
        JPanel resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(primaryColor, 2),
            "Search Results & Web Browser Info",
            0, 0,
            new Font("Arial", Font.BOLD, 16),
            primaryColor
        ));
        
        JScrollPane scrollPane = new JScrollPane(resultsArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        resultsPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Status Panel
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(new EmptyBorder(10, 20, 15, 20));
        statusPanel.setBackground(new Color(248, 249, 250));
        statusPanel.add(statusLabel, BorderLayout.WEST);
        
        // Add all panels
        add(headerPanel, BorderLayout.NORTH);
        add(resultsPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);
        
        getContentPane().setBackground(Color.WHITE);
    }
    
    private JButton createQuickLinkButton(String text, String url) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 11));
        button.setBackground(new Color(108, 117, 125));
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(90, 25));
        
        button.addActionListener(e -> {
            urlField.setText(url);
            openWebsite(url);
        });
        
        return button;
    }
    
    private void setupEventHandlers() {
        loadButton.addActionListener(e -> loadDataset());
        searchButton.addActionListener(e -> performSearch());
        browseButton.addActionListener(e -> openWebsite(urlField.getText()));
        
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performSearch();
                }
            }
        });
        
        urlField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    openWebsite(urlField.getText());
                }
            }
        });
    }
    
    private void setupAnimations() {
        // Pulsing animation for buttons
        pulseTimer = new Timer(100, new ActionListener() {
            private int step = 0;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isPulsing) {
                    step++;
                    float brightness = 0.8f + 0.2f * (float)Math.sin(step * 0.3);
                    
                    if (searchIndex != null) {
                        Color pulseColor = new Color(
                            (int)(primaryColor.getRed() * brightness),
                            (int)(primaryColor.getGreen() * brightness),
                            (int)(primaryColor.getBlue() * brightness)
                        );
                        searchButton.setBackground(pulseColor);
                    }
                    
                    Color browsePulseColor = new Color(
                        (int)(browserColor.getRed() * brightness),
                        (int)(browserColor.getGreen() * brightness),
                        (int)(browserColor.getBlue() * brightness)
                    );
                    browseButton.setBackground(browsePulseColor);
                } else {
                    searchButton.setBackground(primaryColor);
                    browseButton.setBackground(browserColor);
                }
            }
        });
        pulseTimer.start();
        isPulsing = true;
    }
    
    private void openWebsite(String url) {
        if (url == null || url.trim().isEmpty()) {
            showError("Please enter a valid URL!");
            return;
        }
        
        // Ensure URL has protocol
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;
        }
        
        try {
            // Update results area with browser info
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < 60; i++) sb.append("=");
            sb.append("\n");
            sb.append("WEB BROWSER - Opening Website\n");
            for(int i = 0; i < 60; i++) sb.append("=");
            sb.append("\n\n");
            
            sb.append("Opening URL: ").append(url).append("\n\n");
            sb.append("Browser Status: Launching default web browser...\n");
            sb.append("Time: ").append(new java.util.Date()).append("\n\n");
            
            sb.append("Popular Websites You Can Try:\n");
            sb.append("• https://www.google.com - Search Engine\n");
            sb.append("• https://www.github.com - Code Repository\n");
            sb.append("• https://stackoverflow.com - Programming Q&A\n");
            sb.append("• https://www.wikipedia.org - Encyclopedia\n");
            sb.append("• https://www.youtube.com - Video Platform\n");
            sb.append("• https://www.amazon.com - E-commerce\n");
            sb.append("• https://www.facebook.com - Social Media\n");
            sb.append("• https://www.twitter.com - Microblogging\n\n");
            
            sb.append("Note: The website will open in your default web browser.\n");
            sb.append("You can continue using this search engine while browsing!\n");
            
            resultsArea.setText(sb.toString());
            resultsArea.setCaretPosition(0);
            
            // Open in default browser
            Desktop desktop = Desktop.getDesktop();
            desktop.browse(new URI(url));
            
            statusLabel.setText("SUCCESS: Opened " + url + " in your default browser!");
            statusLabel.setForeground(successColor);
            flashLabel(statusLabel, 2);
            
        } catch (Exception e) {
            showError("Failed to open website: " + e.getMessage());
        }
    }
    
    private void loadDataset() {
        String selectedDataset = (String) datasetCombo.getSelectedItem();
        
        SwingWorker<LinkedHashMap<String, HashSet>, Void> worker = new SwingWorker<LinkedHashMap<String, HashSet>, Void>() {
            @Override
            protected LinkedHashMap<String, HashSet> doInBackground() throws Exception {
                SwingUtilities.invokeLater(() -> {
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                    progressBar.setString("Loading dataset...");
                    statusLabel.setText("Loading: " + selectedDataset);
                    statusLabel.setForeground(warningColor);
                    loadButton.setEnabled(false);
                });
                
                return Setup.initialise(selectedDataset);
            }
            
            @Override
            protected void done() {
                try {
                    searchIndex = get();
                    progressBar.setVisible(false);
                    loadButton.setEnabled(true);
                    
                    if (searchIndex != null && !searchIndex.isEmpty()) {
                        statusLabel.setText("SUCCESS: Dataset loaded! " + searchIndex.size() + " words indexed. Ready to search!");
                        statusLabel.setForeground(successColor);
                        searchButton.setEnabled(true);
                        searchField.setEnabled(true);
                        
                        // Flash success animation
                        flashLabel(statusLabel, 3);
                        
                    } else {
                        statusLabel.setText("ERROR: Failed to load dataset. Please check the file path.");
                        statusLabel.setForeground(dangerColor);
                    }
                } catch (Exception e) {
                    statusLabel.setText("ERROR: " + e.getMessage());
                    statusLabel.setForeground(dangerColor);
                    progressBar.setVisible(false);
                    loadButton.setEnabled(true);
                }
            }
        };
        
        worker.execute();
    }
    
    private void performSearch() {
        if (searchIndex == null) {
            showError("Please load a dataset first, or use the web browser to search online!");
            return;
        }
        
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            showError("Please enter a search query!");
            return;
        }
        
        SwingWorker<HashSet<String>, Void> worker = new SwingWorker<HashSet<String>, Void>() {
            @Override
            protected HashSet<String> doInBackground() throws Exception {
                SwingUtilities.invokeLater(() -> {
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                    progressBar.setString("Searching...");
                    statusLabel.setText("Searching for: \"" + query + "\"");
                    statusLabel.setForeground(primaryColor);
                    searchButton.setEnabled(false);
                });
                
                Thread.sleep(300); // Animation delay
                return Searcher.search(query, searchIndex);
            }
            
            @Override
            protected void done() {
                try {
                    HashSet<String> results = get();
                    progressBar.setVisible(false);
                    searchButton.setEnabled(true);
                    
                    displayResults(query, results);
                } catch (Exception e) {
                    statusLabel.setText("Search error: " + e.getMessage());
                    statusLabel.setForeground(dangerColor);
                    progressBar.setVisible(false);
                    searchButton.setEnabled(true);
                }
            }
        };
        
        worker.execute();
    }
    
    private void displayResults(String query, HashSet<String> results) {
        StringBuilder sb = new StringBuilder();
        
        for(int i = 0; i < 60; i++) sb.append("=");
        sb.append("\n");
        sb.append("SEARCH RESULTS FOR: \"").append(query).append("\"\n");
        for(int i = 0; i < 60; i++) sb.append("=");
        sb.append("\n\n");
        
        if (results == null || results.isEmpty()) {
            sb.append("No results found in local dataset.\n\n");
            
            // Suggest web search
            sb.append("TIP: Try searching online instead!\n");
            sb.append("Click 'Browse Web' and try these URLs:\n");
            sb.append("• https://www.google.com/search?q=").append(query.replace(" ", "+")).append("\n");
            sb.append("• https://www.bing.com/search?q=").append(query.replace(" ", "+")).append("\n");
            sb.append("• https://duckduckgo.com/?q=").append(query.replace(" ", "+")).append("\n\n");
            
            // Try suggestions from local dataset
            String[] queryParts = query.split(" ");
            if (queryParts.length == 1) {
                sb.append("Similar words in local dataset:\n");
                HashSet<String> suggestions = SimilarWords.retrieveSimilarWords(searchIndex, queryParts[0]);
                if (suggestions != null && !suggestions.isEmpty()) {
                    for (String suggestion : suggestions) {
                        sb.append("  - ").append(suggestion).append("\n");
                    }
                } else {
                    sb.append("  No similar words found in local dataset.\n");
                }
            }
            
            statusLabel.setText("No local results. Try web search for: \"" + query + "\"");
            statusLabel.setForeground(warningColor);
        } else {
            sb.append("Found ").append(results.size()).append(" result(s) in local dataset:\n\n");
            
            int count = 1;
            for (String url : results) {
                sb.append(String.format("%3d. %s\n", count, url));
                count++;
                if (count > 50) { // Limit for performance
                    sb.append("\n... and ").append(results.size() - 50).append(" more results\n");
                    break;
                }
            }
            
            sb.append("\nTIP: Click on any URL above to visit it, or use the web browser section!\n");
            
            statusLabel.setText("SUCCESS: Found " + results.size() + " local results for: \"" + query + "\"");
            statusLabel.setForeground(successColor);
            flashLabel(statusLabel, 2);
        }
        
        resultsArea.setText(sb.toString());
        resultsArea.setCaretPosition(0);
    }
    
    private void showError(String message) {
        statusLabel.setText("ERROR: " + message);
        statusLabel.setForeground(dangerColor);
        flashLabel(statusLabel, 4);
    }
    
    private void flashLabel(JLabel label, int times) {
        Timer flashTimer = new Timer(200, null);
        flashTimer.addActionListener(new ActionListener() {
            int count = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                label.setVisible(count % 2 == 0);
                count++;
                if (count >= times * 2) {
                    flashTimer.stop();
                    label.setVisible(true);
                }
            }
        });
        flashTimer.start();
    }
    
    public static void main(String[] args) {
        // Use default look and feel
        
        SwingUtilities.invokeLater(() -> {
            new WebBrowserSearchGUI().setVisible(true);
        });
    }
}
