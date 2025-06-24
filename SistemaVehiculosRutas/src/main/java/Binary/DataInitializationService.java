/*
 * Nombre del Archivo: DataInitializationService.java
 * 
 * Descripcion: Servicio de inicialización de datos que crea y configura
 *              datos por defecto para el sistema. Proporciona funcionalidades
 *              para crear usuarios iniciales, tipos de combustible, tipos
 *              de cargadores, ciudades y conexiones por defecto. Utiliza
 *              el DataManager para persistir los datos y maneja errores
 *              de serialización de forma robusta.
 * 
 * Nombre de los Integrantes:
 * Javier Lee Liang
 * Paulo César Herrera Arias
 * José Emilio Alvarado Mendez
 * Josué Santiago Hidalgo Sandoval
 */
package Binary;

import java.util.Arrays;
import java.util.List;

import Interfaces.DataManager;
import Interfaces.SerializationException;
import Models.City;
import Models.Connection;
import User.User;
import User.UserRole;
import Vehicle.ChargerType;
import Vehicle.FuelType;

/**
 * Servicio de inicialización de datos del sistema
 * 
 * Esta clase proporciona funcionalidades para crear y configurar datos
 * por defecto cuando el sistema se ejecuta por primera vez o cuando
 * se requiere un reset completo de los datos.
 * 
 * Características principales:
 * - Creación de usuarios administradores y de prueba
 * - Configuración de tipos de combustible estándar
 * - Configuración de tipos de cargadores eléctricos
 * - Creación de ciudades principales de Costa Rica
 * - Establecimiento de conexiones entre ciudades
 * - Funcionalidad de reset completo de datos
 * 
 * Datos por defecto incluidos:
 * - Usuarios: admin/admin123, user/user123
 * - Combustibles: Regular, Super, Diesel, LP Gas
 * - Cargadores: Schuko, Type 1, Type 2, CHAdeMO, CCS, Tesla
 * - Ciudades: San José, Cartago, Alajuela, Heredia, Puntarenas, Liberia
 * - Conexiones: Rutas principales entre ciudades
 */
public class DataInitializationService {
    private final DataManager dataManager;
    
    /**
     * Constructor del servicio de inicialización de datos
     * 
     * @param dataManager Gestor de datos para persistir la información
     * 
     * Notas:
     * - Requiere un DataManager válido para funcionar
     * - El DataManager se utiliza para todas las operaciones de persistencia
     */
    public DataInitializationService(DataManager dataManager) {
        this.dataManager = dataManager;
    }
    
    /**
     * Inicializa todos los datos por defecto si los archivos no existen
     * 
     * Proceso:
     * - Verifica la existencia de cada archivo de datos
     * - Crea datos por defecto solo para archivos faltantes
     * - Ejecuta la inicialización en orden específico
     * 
     * Orden de inicialización:
     * 1. Usuarios (requeridos para autenticación)
     * 2. Tipos de combustible (requeridos para estaciones)
     * 3. Tipos de cargadores (requeridos para estaciones eléctricas)
     * 4. Ciudades (requeridas para conexiones)
     * 5. Conexiones (requieren ciudades existentes)
     * 
     * Notas:
     * - Solo crea datos si los archivos no existen
     * - No sobrescribe datos existentes
     * - Maneja errores individualmente para cada tipo de dato
     */
    public void initializeDefaultData() {
        initializeDefaultUsers();
        initializeDefaultFuelTypes();
        initializeDefaultChargerTypes();
        initializeDefaultCities();
        initializeDefaultConnections();
    }
    
    /**
     * Crea usuarios por defecto si el archivo de usuarios no existe
     * 
     * Proceso:
     * - Verifica si existe el archivo de usuarios
     * - Crea usuario administrador con credenciales por defecto
     * - Crea usuario de prueba para testing
     * - Guarda la lista de usuarios en el archivo correspondiente
     * 
     * Usuarios creados:
     * - admin/admin123 (rol ADMIN)
     * - user/user123 (rol USER)
     * 
     * Manejo de errores:
     * - Captura SerializationException y muestra mensaje de error
     * - No interrumpe la ejecución del programa
     * - Muestra confirmación en consola si la creación es exitosa
     */
    private void initializeDefaultUsers() {
        if (!dataManager.fileExists(FileConstants.USERS_FILE)) {
            try {
                User admin = new User("admin", "admin123", UserRole.ADMIN);
                User testUser = new User("user", "user123", UserRole.USER);
                
                List<User> defaultUsers = Arrays.asList(admin, testUser);
                dataManager.saveList(defaultUsers, FileConstants.USERS_FILE);
                
                System.out.println("Created default users: admin/admin123, user/user123");
            } catch (SerializationException e) {
                System.err.println("Failed to create default users: " + e.getMessage());
            }
        }
    }
    
    /**
     * Crea tipos de combustible por defecto si el archivo no existe
     * 
     * Proceso:
     * - Verifica si existe el archivo de tipos de combustible
     * - Crea tipos estándar de combustible con descripciones detalladas
     * - Guarda la lista en el archivo correspondiente
     * 
     * Tipos de combustible creados:
     * - Regular: Gasolina regular (Octano 87)
     * - Super: Gasolina súper (Octano 91+)
     * - Diesel: Combustible diesel (bajo azufre)
     * - LP Gas: Gas licuado de petróleo (propano)
     * 
     * Manejo de errores:
     * - Captura SerializationException y muestra mensaje de error
     * - No interrumpe la ejecución del programa
     * - Muestra confirmación en consola si la creación es exitosa
     */
    private void initializeDefaultFuelTypes() {
        if (!dataManager.fileExists(FileConstants.FUEL_TYPES_FILE)) {
            try {
                List<FuelType> defaultFuels = Arrays.asList(
                    new FuelType("regular", "Regular Gasoline", "Octane 87 - Regular unleaded gasoline"),
                    new FuelType("super", "Super Gasoline", "Octane 91+ - Premium unleaded gasoline"),
                    new FuelType("diesel", "Diesel", "Ultra-low sulfur diesel fuel"),
                    new FuelType("lp", "LP Gas", "Liquefied petroleum gas (propane)")
                );
                
                dataManager.saveList(defaultFuels, FileConstants.FUEL_TYPES_FILE);
                System.out.println("Created default fuel types");
            } catch (SerializationException e) {
                System.err.println("Failed to create default fuel types: " + e.getMessage());
            }
        }
    }
    
    /**
     * Crea tipos de cargadores eléctricos por defecto si el archivo no existe
     * 
     * Proceso:
     * - Verifica si existe el archivo de tipos de cargadores
     * - Crea tipos estándar de cargadores con especificaciones técnicas
     * - Guarda la lista en el archivo correspondiente
     * 
     * Tipos de cargadores creados:
     * - Schuko: Enchufe estándar europeo (3 kW)
     * - Type 1 (J1772): Estándar SAE J1772 (7 kW)
     * - Type 2 (Mennekes): Estándar IEC 62196-2 (22 kW)
     * - CHAdeMO: Carga rápida CHAdeMO (50 kW)
     * - CCS: Sistema de carga combinada (150 kW)
     * - Tesla Supercharger: Cargador propietario Tesla (250 kW)
     * 
     * Manejo de errores:
     * - Captura SerializationException y muestra mensaje de error
     * - No interrumpe la ejecución del programa
     * - Muestra confirmación en consola si la creación es exitosa
     */
    private void initializeDefaultChargerTypes() {
        if (!dataManager.fileExists(FileConstants.CHARGER_TYPES_FILE)) {
            try {
                List<ChargerType> defaultChargers = Arrays.asList(
                    new ChargerType("schuko", "Schuko", "Standard EU plug", 3),
                    new ChargerType("type1", "Type 1 (J1772)", "SAE J1772", 7),
                    new ChargerType("type2", "Type 2 (Mennekes)", "IEC 62196-2", 22),
                    new ChargerType("chademo", "CHAdeMO", "CHAdeMO fast charging", 50),
                    new ChargerType("ccs", "CCS", "Combined Charging System", 150),
                    new ChargerType("tesla", "Tesla Supercharger", "Tesla proprietary", 250)
                );
                
                dataManager.saveList(defaultChargers, FileConstants.CHARGER_TYPES_FILE);
                System.out.println("Created default charger types");
            } catch (SerializationException e) {
                System.err.println("Failed to create default charger types: " + e.getMessage());
            }
        }
    }
    
    /**
     * Crea ciudades por defecto si el archivo no existe
     * 
     * Proceso:
     * - Verifica si existe el archivo de ciudades
     * - Crea ciudades principales de Costa Rica con coordenadas GPS
     * - Guarda la lista en el archivo correspondiente
     * 
     * Ciudades creadas:
     * - San José: Capital de Costa Rica (9.9281, -84.0907)
     * - Cartago: Antigua capital (9.8644, -83.9194)
     * - Alajuela: Ciudad del aeropuerto (10.0162, -84.2117)
     * - Heredia: Ciudad universitaria (9.9994, -84.1167)
     * - Puntarenas: Puerto del Pacífico (9.9763, -84.8403)
     * - Liberia: Capital de Guanacaste (10.6346, -85.4370)
     * 
     * Notas:
     * - Las coordenadas están en formato latitud/longitud
     * - Las ciudades se crean en orden específico para las conexiones
     * 
     * Manejo de errores:
     * - Captura SerializationException y muestra mensaje de error
     * - No interrumpe la ejecución del programa
     * - Muestra confirmación en consola si la creación es exitosa
     */
    private void initializeDefaultCities() {
        if (!dataManager.fileExists(FileConstants.CITIES_FILE)) {
            try {
                List<City> defaultCities = Arrays.asList(
                    new City("San José", 9.9281, -84.0907),
                    new City("Cartago", 9.8644, -83.9194),
                    new City("Alajuela", 10.0162, -84.2117),
                    new City("Heredia", 9.9994, -84.1167),
                    new City("Puntarenas", 9.9763, -84.8403),
                    new City("Liberia", 10.6346, -85.4370)
                );
                
                dataManager.saveList(defaultCities, FileConstants.CITIES_FILE);
                System.out.println("Created default cities");
            } catch (SerializationException e) {
                System.err.println("Failed to create default cities: " + e.getMessage());
            }
        }
    }
    
    /**
     * Crea conexiones por defecto entre ciudades si el archivo no existe
     * 
     * Proceso:
     * - Verifica si existe el archivo de conexiones
     * - Carga las ciudades existentes para crear conexiones
     * - Crea conexiones bidireccionales entre ciudades principales
     * - Guarda la lista en el archivo correspondiente
     * 
     * Conexiones creadas:
     * - San José ↔ Cartago: 22 km, 35 min, $1200
     * - San José ↔ Alajuela: 20 km, 30 min, $1100
     * - San José ↔ Heredia: 12 km, 25 min, $800
     * - San José ↔ Puntarenas: 117 km, 90 min, $3500
     * - Alajuela ↔ Liberia: 215 km, 180 min, $6500
     * 
     * Notas:
     * - Las conexiones son bidireccionales (ida y vuelta)
     * - Requiere que existan al menos 6 ciudades
     * - Los datos incluyen distancia, tiempo y costo
     * 
     * Manejo de errores:
     * - Captura SerializationException y muestra mensaje de error
     * - No interrumpe la ejecución del programa
     * - Muestra confirmación en consola si la creación es exitosa
     */
    private void initializeDefaultConnections() {
        if (!dataManager.fileExists(FileConstants.CONNECTIONS_FILE)) {
            try {
                // Carga ciudades para crear conexiones
                List<City> cities = dataManager.loadList(FileConstants.CITIES_FILE, City::new);
                
                if (cities.size() >= 6) {
                    City sanJose = cities.get(0);
                    City cartago = cities.get(1);
                    City alajuela = cities.get(2);
                    City heredia = cities.get(3);
                    City puntarenas = cities.get(4);
                    City liberia = cities.get(5);
                    
                    List<Connection> defaultConnections = Arrays.asList(
                        new Connection(sanJose, cartago, 22, 35, 1200),
                        new Connection(cartago, sanJose, 22, 35, 1200),
                        new Connection(sanJose, alajuela, 20, 30, 1100),
                        new Connection(alajuela, sanJose, 20, 30, 1100),
                        new Connection(sanJose, heredia, 12, 25, 800),
                        new Connection(heredia, sanJose, 12, 25, 800),
                        new Connection(sanJose, puntarenas, 117, 90, 3500),
                        new Connection(puntarenas, sanJose, 117, 90, 3500),
                        new Connection(alajuela, liberia, 215, 180, 6500),
                        new Connection(liberia, alajuela, 215, 180, 6500)
                    );
                    
                    dataManager.saveList(defaultConnections, FileConstants.CONNECTIONS_FILE);
                    System.out.println("Created default city connections");
                }
            } catch (SerializationException e) {
                System.err.println("Failed to create default connections: " + e.getMessage());
            }
        }
    }
    
    /**
     * Resetea todos los datos a los valores por defecto
     * 
     * Proceso:
     * - Elimina todos los archivos de datos existentes
     * - Recrea todos los datos con valores por defecto
     * - Ejecuta la inicialización completa
     * 
     * Archivos eliminados:
     * - users.dat
     * - fuel_types.dat
     * - charger_types.dat
     * - cities.dat
     * - connections.dat
     * 
     * Notas:
     * - Útil para testing o reset completo del sistema
     * - Elimina TODOS los datos existentes
     * - Recrea datos por defecto automáticamente
     * - Muestra confirmación en consola
     * 
     * Advertencia:
     * - Esta operación es destructiva y elimina todos los datos
     * - Solo debe usarse cuando se requiere un reset completo
     */
    public void resetToDefaults() {
        // Elimina archivos existentes
        dataManager.deleteFile(FileConstants.USERS_FILE);
        dataManager.deleteFile(FileConstants.FUEL_TYPES_FILE);
        dataManager.deleteFile(FileConstants.CHARGER_TYPES_FILE);
        dataManager.deleteFile(FileConstants.CITIES_FILE);
        dataManager.deleteFile(FileConstants.CONNECTIONS_FILE);
        
        // Recrea con valores por defecto
        initializeDefaultData();
        
        System.out.println("All data reset to defaults");
    }
}