/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

/**
 *
 * @author JE
 */
public class AppConfig {
    
    
    // Application metadata
    public static final String APP_NAME = "Route Planning System";
    public static final String APP_VERSION = "1.0.0";
    public static final String APP_DESCRIPTION = "Energy Transition Vehicle Route Management";
    
    // System settings
    public static final String DEFAULT_DATA_DIRECTORY = "data";
    public static final int MAX_ROUTE_SEARCH_DEPTH = 10;
    public static final int CONSOLE_LINE_WIDTH = 80;
    
    // Default system data
    public static final String DEFAULT_ADMIN_USERNAME = "admin";
    public static final String DEFAULT_ADMIN_PASSWORD = "admin123";
    public static final String DEFAULT_USER_USERNAME = "user";
    public static final String DEFAULT_USER_PASSWORD = "user123";
    
    // File settings
    public static final long MAX_FILE_SIZE_MB = 100;
    public static final boolean AUTO_BACKUP_ENABLED = true;
    public static final int BACKUP_RETENTION_DAYS = 30;
    
    // UI settings
    public static final boolean CLEAR_CONSOLE_ENABLED = true;
    public static final boolean COLOR_OUTPUT_ENABLED = false; // Set to true if terminal supports ANSI colors
    public static final int MENU_TIMEOUT_SECONDS = 300; // 5 minutes
    
    // Security settings
    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MAX_LOGIN_ATTEMPTS = 3;
    public static final boolean REQUIRE_STRONG_PASSWORDS = false;
    
    // Performance settings
    public static final int GRAPH_CACHE_SIZE = 1000;
    public static final boolean ENABLE_GRAPH_CACHING = true;
    public static final int MAX_CONCURRENT_ROUTES = 100;
    
    private AppConfig() {
        // Utility class - prevent instantiation
    }
    
    /**
     * Gets application banner text
     * @return formatted banner
     */
    public static String getAppBanner() {
        return String.format("""
            ╔══════════════════════════════════════════════════════════════════════════════╗
            ║                          %s                           ║
            ║                                Version %s                                ║
            ║                                                                              ║
            ║              %s               ║
            ╚══════════════════════════════════════════════════════════════════════════════╝
            """, APP_NAME, APP_VERSION, APP_DESCRIPTION);
    }
    
    /**
     * Validates configuration settings
     * @return true if configuration is valid
     */
    public static boolean validateConfig() {
        boolean isValid = true;
        
        // Validate directory settings
        if (DEFAULT_DATA_DIRECTORY == null || DEFAULT_DATA_DIRECTORY.trim().isEmpty()) {
            System.err.println("Invalid data directory configuration");
            isValid = false;
        }
        
        // Validate credentials
        if (DEFAULT_ADMIN_PASSWORD.length() < MIN_PASSWORD_LENGTH) {
            System.err.println("Default admin password too short");
            isValid = false;
        }
        
        return isValid;
    }
}
