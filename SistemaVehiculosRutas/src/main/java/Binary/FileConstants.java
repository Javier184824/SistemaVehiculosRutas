/*
 * Nombre del Archivo: FileConstants.java
 * 
 * Descripcion: Clase de constantes que define los nombres de archivos
 *              utilizados en el sistema de persistencia de datos. Centraliza
 *              todas las referencias a archivos de datos para facilitar
 *              el mantenimiento y evitar errores de nombres de archivos.
 *              Incluye archivos para usuarios, sesiones, tipos de combustible,
 *              tipos de cargadores, ciudades, conexiones y configuración
 *              del sistema.
 * 
 * Nombre de los Integrantes:
 * Javier Lee Liang
 * Paulo César Herrera Arias
 * José Emilio Alvarado Mendez
 * Josué Santiago Hidalgo Sandoval
 */
package Binary;

/**
 * Clase de constantes para nombres de archivos del sistema
 * 
 * Esta clase define todas las constantes de nombres de archivos utilizados
 * en el sistema de persistencia de datos. Centralizar estas constantes
 * facilita el mantenimiento y evita errores de tipeo en nombres de archivos.
 * 
 * Características:
 * - Clase final para evitar herencia
 * - Constructor privado para evitar instanciación
 * - Constantes públicas estáticas para acceso global
 * - Organización por categorías de datos
 */
public final class FileConstants {
    // Archivos de datos de usuarios
    public static final String USERS_FILE = "users.dat";
    public static final String SESSIONS_FILE = "sessions.dat";
    
    // Archivos de tipos de vehículos y estaciones
    public static final String FUEL_TYPES_FILE = "fuel_types.dat";
    public static final String CHARGER_TYPES_FILE = "charger_types.dat";
    
    // Archivos de datos de ubicaciones
    public static final String CITIES_FILE = "cities.dat";
    public static final String CONNECTIONS_FILE = "connections.dat";
    
    // Configuración del sistema
    public static final String SYSTEM_CONFIG_FILE = "system_config.dat";
    
    // Sufijo para archivos de respaldo
    public static final String BACKUP_SUFFIX = ".backup";
    
    /**
     * Constructor privado para evitar instanciación
     * 
     * Esta clase es una clase de utilidad que solo contiene constantes.
     * No debe ser instanciada, por lo que el constructor es privado.
     */
    private FileConstants() {
        // Clase de utilidad - prevenir instanciación
    }
}