/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import Binary.FileConstants;
import Interfaces.DataManager;
import Interfaces.SerializationException;
import Models.City;
import Models.Connection;

/**
 *
 * @author JE
 */
public class CityManagementService {
    
    private final DataManager dataManager;
    
    public CityManagementService(DataManager dataManager) {
        this.dataManager = dataManager;
    }
    
    // ========== CITY MANAGEMENT ==========
    
    /**
     * Creates a new city
     * @param city the city to create
     * @return true if created successfully
     */
    public boolean createCity(City city) {
        if (city == null) {
            return false;
        }
        
        try {
            List<City> cities = dataManager.loadList(FileConstants.CITIES_FILE, City::new);
            
            // Check for duplicate names
            boolean nameExists = cities.stream()
                .anyMatch(c -> city.getName().equalsIgnoreCase(c.getName()));
            
            if (nameExists) {
                return false; // City name already exists
            }
            
            cities.add(city);
            dataManager.saveList(cities, FileConstants.CITIES_FILE);
            return true;
            
        } catch (SerializationException e) {
            System.err.println("Error creating city: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Updates an existing city
     * @param cityId the ID of the city to update
     * @param updatedCity the updated city information
     * @return true if updated successfully
     */
    public boolean updateCity(String cityId, City updatedCity) {
        if (cityId == null || updatedCity == null) {
            return false;
        }
        
        try {
            List<City> cities = dataManager.loadList(FileConstants.CITIES_FILE, City::new);
            
            for (int i = 0; i < cities.size(); i++) {
                if (cityId.equals(cities.get(i).getId())) {
                    cities.set(i, updatedCity);
                    dataManager.saveList(cities, FileConstants.CITIES_FILE);
                    return true;
                }
            }
            
        } catch (SerializationException e) {
            System.err.println("Error updating city: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Deletes a city (only if no connections exist)
     * @param cityId the ID of the city to delete
     * @return true if deleted successfully
     */
    public boolean deleteCity(String cityId) {
        if (cityId == null) {
            return false;
        }
        
        try {
            // Check for existing connections
            List<Connection> connections = dataManager.loadList(FileConstants.CONNECTIONS_FILE, Connection::new);
            boolean hasConnections = connections.stream()
                .anyMatch(conn -> cityId.equals(conn.getFromCityId()) || cityId.equals(conn.getToCityId()));
            
            if (hasConnections) {
                return false; // Cannot delete city with existing connections
            }
            
            // Delete the city
            List<City> cities = dataManager.loadList(FileConstants.CITIES_FILE, City::new);
            boolean removed = cities.removeIf(city -> cityId.equals(city.getId()));
            
            if (removed) {
                dataManager.saveList(cities, FileConstants.CITIES_FILE);
                return true;
            }
            
        } catch (SerializationException e) {
            System.err.println("Error deleting city: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Gets all cities
     * @return list of all cities
     */
    public List<City> getAllCities() {
        try {
            return dataManager.loadList(FileConstants.CITIES_FILE, City::new);
        } catch (SerializationException e) {
            System.err.println("Error loading cities: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Finds a city by ID
     * @param cityId the city ID
     * @return the city, or null if not found
     */
    public City findCityById(String cityId) {
        return getAllCities().stream()
            .filter(city -> cityId.equals(city.getId()))
            .findFirst()
            .orElse(null);
    }
    
    // ========== CONNECTION MANAGEMENT ==========
      /**
     * Creates a new connection between cities
     * @param connection the connection to create
     * @return true if created successfully
     */
    public boolean createConnection(Connection connection) {
        if (connection == null || connection.getFromCity() == null || connection.getToCity() == null) {
            return false;
        }
        
        try {
            List<Connection> connections;
            
            // Check if connections file exists, if not create empty list
            if (dataManager.fileExists(FileConstants.CONNECTIONS_FILE)) {
                try {
                    connections = dataManager.loadList(FileConstants.CONNECTIONS_FILE, Connection::new);
                    System.out.println("Debug: Loaded " + connections.size() + " existing connections");
                } catch (SerializationException e) {
                    // File exists but is corrupted, delete it and create new
                    System.out.println("Debug: Connections file is corrupted, deleting and creating new");
                    dataManager.deleteFile(FileConstants.CONNECTIONS_FILE);
                    connections = new ArrayList<>();
                }
            } else {
                connections = new ArrayList<>();
                System.out.println("Debug: Connections file not found, creating new list");
            }
            
            // Check for duplicate connections using city IDs (since city references may not be resolved)
            String fromCityId = connection.getFromCity().getId();
            String toCityId = connection.getToCity().getId();
            
            System.out.println("Debug: Attempting to create connection from " + fromCityId + " to " + toCityId);
            
            boolean connectionExists = connections.stream()
                .anyMatch(conn -> {
                    // Use getFromCityId() and getToCityId() for unresolved connections
                    String connFromId = conn.getFromCity() != null ? conn.getFromCity().getId() : conn.getFromCityId();
                    String connToId = conn.getToCity() != null ? conn.getToCity().getId() : conn.getToCityId();
                    boolean matches = fromCityId.equals(connFromId) && toCityId.equals(connToId);
                    if (matches) {
                        System.out.println("Debug: Found existing connection: " + connFromId + " -> " + connToId);
                    }
                    return matches;
                });
            
            if (connectionExists) {
                System.out.println("Debug: Connection already exists, returning false");
                return false; // Connection already exists
            }
            
            System.out.println("Debug: Adding new connection to list");
            connections.add(connection);
            dataManager.saveList(connections, FileConstants.CONNECTIONS_FILE);
            System.out.println("Debug: Connection saved successfully");
            return true;
            
        } catch (SerializationException e) {
            System.err.println("Error creating connection: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Updates an existing connection
     * @param fromCityId the from city ID
     * @param toCityId the to city ID
     * @param updatedConnection the updated connection information
     * @return true if updated successfully
     */
    public boolean updateConnection(String fromCityId, String toCityId, Connection updatedConnection) {
        if (fromCityId == null || toCityId == null || updatedConnection == null) {
            return false;
        }
        
        try {
            List<Connection> connections;
            List<City> cities = getAllCities();
            
            // Check if connections file exists, if not return false
            if (dataManager.fileExists(FileConstants.CONNECTIONS_FILE)) {
                connections = dataManager.loadList(FileConstants.CONNECTIONS_FILE, Connection::new);
                
                // Resolve city references
                Function<String, City> cityResolver = cityId -> 
                    cities.stream().filter(c -> cityId.equals(c.getId())).findFirst().orElse(null);
                
                for (Connection conn : connections) {
                    conn.resolveCityReferences(cityResolver);
                }
                
                // Find and update the connection
                for (int i = 0; i < connections.size(); i++) {
                    Connection conn = connections.get(i);
                    if (fromCityId.equals(conn.getFromCity() != null ? conn.getFromCity().getId() : conn.getFromCityId()) && 
                        toCityId.equals(conn.getToCity() != null ? conn.getToCity().getId() : conn.getToCityId())) {
                        connections.set(i, updatedConnection);
                        dataManager.saveList(connections, FileConstants.CONNECTIONS_FILE);
                        return true;
                    }
                }
            } else {
                System.out.println("Debug: Connections file not found, nothing to update");
            }
            
        } catch (SerializationException e) {
            System.err.println("Error updating connection: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Deletes a connection
     * @param fromCityId the from city ID
     * @param toCityId the to city ID
     * @return true if deleted successfully
     */
    public boolean deleteConnection(String fromCityId, String toCityId) {
        if (fromCityId == null || toCityId == null) {
            System.out.println("Debug: deleteConnection called with null IDs: fromCityId=" + fromCityId + ", toCityId=" + toCityId);
            return false;
        }
        
        try {
            List<Connection> connections;
            List<City> cities = getAllCities();
            
            // Check if connections file exists, if not return false
            if (dataManager.fileExists(FileConstants.CONNECTIONS_FILE)) {
                connections = dataManager.loadList(FileConstants.CONNECTIONS_FILE, Connection::new);
                
                // First migrate connections to ensure they have proper city IDs
                migrateConnections(connections, cities);
                
                System.out.println("Debug: Attempting to delete connection from " + fromCityId + " to " + toCityId);
                System.out.println("Debug: Found " + connections.size() + " connections to check");
                
                boolean removed = connections.removeIf(conn -> {
                    String connFromId = conn.getFromCityId();
                    String connToId = conn.getToCityId();
                    boolean matches = fromCityId.equals(connFromId) && toCityId.equals(connToId);
                    System.out.println("Debug: Checking connection " + connFromId + " -> " + connToId + " matches: " + matches);
                    return matches;
                });
                
                System.out.println("Debug: Connection removed: " + removed);
                
                if (removed) {
                    dataManager.saveList(connections, FileConstants.CONNECTIONS_FILE);
                    return true;
                }
            } else {
                System.out.println("Debug: Connections file not found, nothing to delete");
            }
            
        } catch (SerializationException e) {
            System.err.println("Error deleting connection: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Gets all connections
     * @return list of all connections
     */
    public List<Connection> getAllConnections() {
        try {
            List<Connection> connections;
            List<City> cities = getAllCities();
            
            // Check if connections file exists, if not return empty list
            if (dataManager.fileExists(FileConstants.CONNECTIONS_FILE)) {
                connections = dataManager.loadList(FileConstants.CONNECTIONS_FILE, Connection::new);
                System.out.println("Debug: Loaded " + connections.size() + " connections and " + cities.size() + " cities");
                
                // Check for corrupted connections and log them
                long corruptedCount = connections.stream()
                    .filter(conn -> conn.getFromCityId() == null || conn.getToCityId() == null)
                    .count();
                
                if (corruptedCount > 0) {
                    System.out.println("Debug: Found " + corruptedCount + " connections with null city IDs");
                    System.out.println("Debug: Use 'Fix Corrupted Connections' option to resolve this issue");
                }
                
                // Migrate connections to ensure they have proper city IDs
                migrateConnections(connections, cities);
            } else {
                connections = new ArrayList<>();
                System.out.println("Debug: Connections file not found, returning empty list");
            }
            
            return connections;
            
        } catch (SerializationException e) {
            // File exists but is corrupted, delete it and return empty list
            System.out.println("Debug: Connections file is corrupted, deleting and returning empty list");
            dataManager.deleteFile(FileConstants.CONNECTIONS_FILE);
            System.err.println("Error loading connections: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Migrates existing connections to ensure they have proper city IDs stored
     */
    private void migrateConnections(List<Connection> connections, List<City> cities) {
        Function<String, City> cityResolver = cityId -> 
            cities.stream().filter(c -> cityId.equals(c.getId())).findFirst().orElse(null);
        
        boolean needsSave = false;
        for (Connection conn : connections) {
            // If city IDs exist but city references are null, resolve them
            if (conn.getFromCityId() != null && conn.getFromCity() == null) {
                City fromCity = cityResolver.apply(conn.getFromCityId());
                if (fromCity != null) {
                    conn.setFromCity(fromCity);
                    needsSave = true;
                }
            }
            if (conn.getToCityId() != null && conn.getToCity() == null) {
                City toCity = cityResolver.apply(conn.getToCityId());
                if (toCity != null) {
                    conn.setToCity(toCity);
                    needsSave = true;
                }
            }
            
            // Also resolve any remaining null references
            conn.resolveCityReferences(cityResolver);
        }
        
        if (needsSave) {
            try {
                dataManager.saveList(connections, FileConstants.CONNECTIONS_FILE);
                System.out.println("Debug: Migrated and saved connections with proper city IDs");
            } catch (SerializationException e) {
                System.err.println("Error saving migrated connections: " + e.getMessage());
            }
        }
    }
    
    /**
     * Gets connections from a specific city
     * @param cityId the city ID
     * @return list of connections from the city
     */
    public List<Connection> getConnectionsFromCity(String cityId) {
        return getAllConnections().stream()
            .filter(conn -> cityId.equals(conn.getFromCity() != null ? conn.getFromCity().getId() : conn.getFromCityId()))
            .toList();
    }
    
    /**
     * Gets connections to a specific city
     * @param cityId the city ID
     * @return list of connections to the city
     */
    public List<Connection> getConnectionsToCity(String cityId) {
        return getAllConnections().stream()
            .filter(conn -> cityId.equals(conn.getToCity() != null ? conn.getToCity().getId() : conn.getToCityId()))
            .toList();
    }
    
    /**
     * Fixes corrupted connections by recreating them with proper city IDs
     * This method should be called if connections have null city IDs
     * @return true if connections were fixed successfully
     */
    public boolean fixCorruptedConnections() {
        try {
            List<City> cities = getAllCities();
            
            if (cities.size() < 6) {
                System.out.println("Debug: Not enough cities to recreate connections");
                return false;
            }
            
            System.out.println("Debug: Recreating default connections...");
            
            // Delete existing connections file
            dataManager.deleteFile(FileConstants.CONNECTIONS_FILE);
            
            // Recreate default connections
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
            System.out.println("Debug: Recreated " + defaultConnections.size() + " connections");
            return true;
            
        } catch (SerializationException e) {
            System.err.println("Error recreating connections: " + e.getMessage());
            return false;
        }
    }
}
