<<<<<<< HEAD
# Java Search Engine - Advanced Web Search Platform

🔍 **A comprehensive Java-based search engine with Google-like interface and real-time web search capabilities**

## 🌟 Features

### Core Search Engine
- **Local Dataset Search**: Search through parsed HTML files and documents
- **Boolean Operations**: Support for AND/OR search queries
- **Smart Suggestions**: Levenshtein distance-based similar word recommendations
- **Multiple Dataset Support**: Tiny, small, medium, and big dataset compatibility

### Google-Like Interface
- **Authentic Google Design**: Clean, professional interface mimicking Google's search page
- **Auto-Suggestions**: Real-time search suggestions as you type
- **"I'm Feeling Lucky"**: Direct access to first search result (opens Google)
- **Responsive UI**: Modern Swing-based interface with animations

### Real-Time Web Search
- **Multi-Platform Search**: Integrated search across:
  - Wikipedia (Encyclopedia articles)
  - Stack Overflow (Programming Q&A)
  - GitHub (Code repositories)
  - Google Search (Direct Google results)
  - Bing Search (Alternative search results)
- **Live Web Crawling**: Crawl websites up to specified depth
- **Smart Ranking**: Relevance-based result ranking algorithm

### Advanced Features
- **Web Browser Integration**: Open any website directly from the application
- **Progress Indicators**: Real-time search progress with loading animations
- **Result Statistics**: Comprehensive search analytics and metrics
- **Clickable Results**: Direct browser integration for seamless web browsing

## 🚀 Available Interfaces

1. **GoogleLikeSearchEngine.java** - Google-style web search interface
2. **RealWorldSearchEngine.java** - Advanced search with local + web data integration
3. **WebBrowserSearchGUI.java** - Enhanced GUI with web browsing capabilities
4. **SimpleAnimatedGUI.java** - Animated Swing interface for local search
5. **ConsoleSearchEngine.java** - Command-line interface for basic search

## 📋 Requirements

- Java 8 or higher
- Internet connection (for web search features)
- Sample datasets (included in TestInput directory)

## 🎯 Quick Start

### Run Google-Like Search Engine
```bash
cd src/main/java
javac GoogleLikeSearchEngine.java
java GoogleLikeSearchEngine
```

### Run Real-World Search Engine
```bash
cd src/main/java
javac WebCrawler.java RealWorldSearchEngine.java
java RealWorldSearchEngine
```

### Run Console Version
```bash
cd src/main/java
javac ConsoleSearchEngine.java
java ConsoleSearchEngine ../../../TestInput/itcwww-small.txt
```

## 🔧 Usage Examples

### Boolean Search Queries
- `java AND programming` - Find results containing both terms
- `web OR development` - Find results containing either term
- `machine learning` - Simple search for the phrase

### Web Search Features
- Enter any search query in the Google-like interface
- Use "I'm Feeling Lucky" for direct Google results
- Browse integrated results from multiple platforms
- Click any result to open in your default browser

## 📁 Project Structure

```
Java-Search-Engine/
├── src/main/java/
│   ├── GoogleLikeSearchEngine.java    # Google-style interface
│   ├── RealWorldSearchEngine.java     # Advanced web search
│   ├── WebBrowserSearchGUI.java       # Web browsing GUI
│   ├── SimpleAnimatedGUI.java         # Animated local search
│   ├── ConsoleSearchEngine.java       # Command-line interface
│   ├── WebCrawler.java               # Web crawling functionality
│   ├── Searcher.java                 # Core search logic
│   ├── Setup.java                    # Data initialization
│   ├── SimilarWords.java             # Word suggestion algorithm
│   └── GUI.java                      # Original JavaFX interface
├── TestInput/                        # Sample datasets
│   ├── itcwww-tiny.txt
│   ├── itcwww-small.txt
│   ├── itcwww-medium.txt
│   └── itcwww-big.txt
└── README.md
```

## 🎨 Screenshots & Demo

- **Google-Like Interface**: Clean, professional search page with auto-suggestions
- **Real-Time Results**: Live search results from multiple web sources
- **Animated UI**: Smooth animations and progress indicators
- **Web Integration**: Seamless browser integration for result viewing

## 🔍 Search Algorithm Features

- **Relevance Ranking**: Advanced scoring algorithm for result ordering
- **Multi-Source Integration**: Combines local and web search results
- **Error Handling**: Robust error handling for network and file operations
- **Performance Optimization**: Efficient search and indexing algorithms

## 🌐 Web Search Integration

- **Wikipedia API**: Encyclopedia article search
- **Stack Overflow**: Programming question search
- **GitHub**: Code repository search
- **Google/Bing**: Direct web search integration
- **Custom Web Crawler**: Live website content indexing

## 🛠️ Technical Details

- **Language**: Java (Java 8+ compatible)
- **GUI Framework**: Swing (JavaFX alternative included)
- **Web Integration**: HttpURLConnection for web requests
- **Data Structures**: LinkedHashMap for efficient indexing
- **Algorithms**: Levenshtein distance for word similarity

## 📈 Performance

- **Fast Local Search**: Optimized indexing for quick local searches
- **Asynchronous Web Search**: Non-blocking web requests
- **Memory Efficient**: Smart memory management for large datasets
- **Scalable Architecture**: Supports datasets of various sizes

## 🤝 Contributing

Feel free to contribute to this project by:
- Adding new search sources
- Improving the UI/UX
- Enhancing search algorithms
- Adding new features

## 📄 License

This project is open source and available under the MIT License.

## 👨‍💻 Author

**kathirvel-p22** - Advanced Java Search Engine with Google-like capabilities

---

*A comprehensive search solution combining local dataset search with real-time web search capabilities, featuring a Google-like interface and advanced search algorithms.*
=======
# Search-Engine-in-Java
>>>>>>> 892534b6e8395153f3348b4d058258bb530a12a5
