import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.LinkedHashMap;

public class SimpleAnimatedGUI extends JFrame {
    
    private LinkedHashMap<String, HashSet> searchIndex;
    private JTextField searchField;
    private JTextArea resultsArea;
    private JLabel statusLabel;
    private JProgressBar progressBar;
    private JButton searchButton;
    private JButton loadButton;
    private JComboBox<String> datasetCombo;
    private Timer pulseTimer;
    private boolean isPulsing = false;
    
    // Colors
    private Color primaryColor = new Color(0, 123, 255);
    private Color successColor = new Color(40, 167, 69);
    private Color warningColor = new Color(255, 193, 7);
    private Color dangerColor = new Color(220, 53, 69);
    
    public SimpleAnimatedGUI() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupAnimations();
    }
    
    private void initializeComponents() {
        setTitle("Animated Java Search Engine");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        
        // Search field with custom styling
        searchField = new JTextField(30);
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(primaryColor, 2),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        // Styled buttons
        searchButton = createStyledButton("Search", primaryColor);
        loadButton = createStyledButton("Load Dataset", successColor);
        
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
        resultsArea = new JTextArea(20, 60);
        resultsArea.setEditable(false);
        resultsArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        resultsArea.setBackground(new Color(248, 249, 250));
        resultsArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Status and progress
        statusLabel = new JLabel("Ready! Please load a dataset to begin searching.");
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
        button.setPreferredSize(new Dimension(120, 40));
        
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
        JLabel titleLabel = new JLabel("Animated Java Search Engine", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(primaryColor);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Dataset Panel
        JPanel datasetPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        datasetPanel.setBackground(Color.WHITE);
        datasetPanel.add(new JLabel("Select Dataset:"));
        datasetPanel.add(datasetCombo);
        datasetPanel.add(loadButton);
        
        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.add(new JLabel("Search Query:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        // Progress Panel
        JPanel progressPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        progressPanel.setBackground(Color.WHITE);
        progressPanel.add(progressBar);
        
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(20));
        headerPanel.add(datasetPanel);
        headerPanel.add(searchPanel);
        headerPanel.add(progressPanel);
        
        // Results Panel
        JPanel resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(primaryColor, 2),
            "Search Results",
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
    
    private void setupEventHandlers() {
        loadButton.addActionListener(e -> loadDataset());
        searchButton.addActionListener(e -> performSearch());
        
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performSearch();
                }
            }
        });
    }
    
    private void setupAnimations() {
        // Pulsing animation for search button
        pulseTimer = new Timer(100, new ActionListener() {
            private int step = 0;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isPulsing && searchIndex != null) {
                    step++;
                    float brightness = 0.8f + 0.2f * (float)Math.sin(step * 0.3);
                    Color pulseColor = new Color(
                        (int)(primaryColor.getRed() * brightness),
                        (int)(primaryColor.getGreen() * brightness),
                        (int)(primaryColor.getBlue() * brightness)
                    );
                    searchButton.setBackground(pulseColor);
                } else {
                    searchButton.setBackground(primaryColor);
                }
            }
        });
        pulseTimer.start();
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
                        isPulsing = true;
                        
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
            showError("Please load a dataset first!");
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
                    isPulsing = false;
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
                    isPulsing = true;
                    
                    displayResults(query, results);
                } catch (Exception e) {
                    statusLabel.setText("Search error: " + e.getMessage());
                    statusLabel.setForeground(dangerColor);
                    progressBar.setVisible(false);
                    searchButton.setEnabled(true);
                    isPulsing = true;
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
            sb.append("No results found.\n\n");
            
            // Try suggestions
            String[] queryParts = query.split(" ");
            if (queryParts.length == 1) {
                sb.append("Did you mean:\n");
                HashSet<String> suggestions = SimilarWords.retrieveSimilarWords(searchIndex, queryParts[0]);
                if (suggestions != null && !suggestions.isEmpty()) {
                    for (String suggestion : suggestions) {
                        sb.append("  - ").append(suggestion).append("\n");
                    }
                } else {
                    sb.append("  No similar words found.\n");
                }
            }
            
            statusLabel.setText("No results found for: \"" + query + "\"");
            statusLabel.setForeground(dangerColor);
        } else {
            sb.append("Found ").append(results.size()).append(" result(s):\n\n");
            
            int count = 1;
            for (String url : results) {
                sb.append(String.format("%3d. %s\n", count, url));
                count++;
                if (count > 100) { // Limit for performance
                    sb.append("\n... and ").append(results.size() - 100).append(" more results\n");
                    break;
                }
            }
            
            statusLabel.setText("SUCCESS: Found " + results.size() + " results for: \"" + query + "\"");
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
            new SimpleAnimatedGUI().setVisible(true);
        });
    }
}
