/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Binary;

/**
 *
 * @author JE
 */

public final class FileConstants {
    // User data files
    public static final String USERS_FILE = "users.dat";
    public static final String SESSIONS_FILE = "sessions.dat";
    
    // Vehicle and station type files
    public static final String FUEL_TYPES_FILE = "fuel_types.dat";
    public static final String CHARGER_TYPES_FILE = "charger_types.dat";
    
    // Location data files
    public static final String CITIES_FILE = "cities.dat";
    public static final String CONNECTIONS_FILE = "connections.dat";
    
    // System configuration
    public static final String SYSTEM_CONFIG_FILE = "system_config.dat";
    
    // Backup file suffix
    public static final String BACKUP_SUFFIX = ".backup";
    
    private FileConstants() {
        // Utility class - prevent instantiation
    }
}