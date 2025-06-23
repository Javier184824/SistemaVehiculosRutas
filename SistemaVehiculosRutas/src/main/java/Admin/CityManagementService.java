/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Admin;

import Binary.FileConstants;
import Interfaces.DataManager;
import Interfaces.SerializationException;
import Models.City;
import Models.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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
            List<Connection> connections = dataManager.loadList(FileConstants.CONNECTIONS_FILE, Connection::new);
            
            // Check for duplicate connections
            boolean connectionExists = connections.stream()
                .anyMatch(conn -> 
                    conn.getFromCity().getId().equals(connection.getFromCity().getId()) &&
                    conn.getToCity().getId().equals(connection.getToCity().getId()));
            
            if (connectionExists) {
                return false; // Connection already exists
            }
            
            connections.add(connection);
            dataManager.saveList(connections, FileConstants.CONNECTIONS_FILE);
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
            List<Connection> connections = dataManager.loadList(FileConstants.CONNECTIONS_FILE, Connection::new);
            List<City> cities = getAllCities();
            
            // Resolve city references
            Function<String, City> cityResolver = cityId -> 
                cities.stream().filter(c -> cityId.equals(c.getId())).findFirst().orElse(null);
            
            for (Connection conn : connections) {
                conn.resolveCityReferences(cityResolver);
            }
            
            // Find and update the connection
            for (int i = 0; i < connections.size(); i++) {
                Connection conn = connections.get(i);
                if (fromCityId.equals(conn.getFromCity().getId()) && 
                    toCityId.equals(conn.getToCity().getId())) {
                    connections.set(i, updatedConnection);
                    dataManager.saveList(connections, FileConstants.CONNECTIONS_FILE);
                    return true;
                }
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
            return false;
        }
        
        try {
            List<Connection> connections = dataManager.loadList(FileConstants.CONNECTIONS_FILE, Connection::new);
            
            boolean removed = connections.removeIf(conn -> 
                fromCityId.equals(conn.getFromCityId()) && toCityId.equals(conn.getToCityId()));
            
            if (removed) {
                dataManager.saveList(connections, FileConstants.CONNECTIONS_FILE);
                return true;
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
            List<Connection> connections = dataManager.loadList(FileConstants.CONNECTIONS_FILE, Connection::new);
            List<City> cities = getAllCities();
            
            // Resolve city references
            Function<String, City> cityResolver = cityId -> 
                cities.stream().filter(c -> cityId.equals(c.getId())).findFirst().orElse(null);
            
            for (Connection conn : connections) {
                conn.resolveCityReferences(cityResolver);
            }
            
            return connections;
            
        } catch (SerializationException e) {
            System.err.println("Error loading connections: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Gets connections from a specific city
     * @param cityId the city ID
     * @return list of connections from the city
     */
    public List<Connection> getConnectionsFromCity(String cityId) {
        return getAllConnections().stream()
            .filter(conn -> cityId.equals(conn.getFromCity().getId()))
            .toList();
    }
    
    /**
     * Gets connections to a specific city
     * @param cityId the city ID
     * @return list of connections to the city
     */
    public List<Connection> getConnectionsToCity(String cityId) {
        return getAllConnections().stream()
            .filter(conn -> cityId.equals(conn.getToCity().getId()))
            .toList();
    }
}
