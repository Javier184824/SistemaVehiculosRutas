/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Binary;

import Interfaces.DataManager;
import Interfaces.SerializationException;
import Models.City;
import Models.Connection;
import User.User;
import User.UserRole;
import Vehicle.ChargerType;
import Vehicle.FuelType;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author JE
 */
public class DataInitializationService {
    private final DataManager dataManager;
    
    public DataInitializationService(DataManager dataManager) {
        this.dataManager = dataManager;
    }
    
    /**
     * Initializes all default data if files don't exist
     */
    public void initializeDefaultData() {
        initializeDefaultUsers();
        initializeDefaultFuelTypes();
        initializeDefaultChargerTypes();
        initializeDefaultCities();
        initializeDefaultConnections();
    }
    
    /**
     * Creates default admin user if users file doesn't exist
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
     * Creates default fuel types
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
     * Creates default charger types
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
     * Creates default cities
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
     * Creates default connections between cities
     */
    private void initializeDefaultConnections() {
        if (!dataManager.fileExists(FileConstants.CONNECTIONS_FILE)) {
            try {
                // Load cities to create connections
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
     * Resets all data to defaults (used for testing or system reset)
     */
    public void resetToDefaults() {
        // Delete existing files
        dataManager.deleteFile(FileConstants.USERS_FILE);
        dataManager.deleteFile(FileConstants.FUEL_TYPES_FILE);
        dataManager.deleteFile(FileConstants.CHARGER_TYPES_FILE);
        dataManager.deleteFile(FileConstants.CITIES_FILE);
        dataManager.deleteFile(FileConstants.CONNECTIONS_FILE);
        
        // Recreate with defaults
        initializeDefaultData();
        
        System.out.println("All data reset to defaults");
    }
}