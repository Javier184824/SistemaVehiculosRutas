/*
 * Nombre del Archivo: CityManagementService.java
 * 
 * Descripcion: Servicio que maneja todas las operaciones relacionadas con ciudades y conexiones
 *              entre ellas. Proporciona funcionalidades para crear, actualizar, eliminar y
 *              consultar ciudades, así como gestionar las conexiones entre ciudades.
 * 
 * Nombre de los Integrantes:
 * Javier Lee Liang
 * Paulo César Herrera Arias
 * José Emilio Alvarado Mendez
 * Josué Santiago Hidalgo Sandoval
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
 * Servicio de gestión de ciudades y conexiones
 * 
 * Esta clase proporciona métodos para administrar ciudades y sus conexiones,
 * incluyendo operaciones CRUD completas y manejo de archivos corruptos.
 */
public class CityManagementService {
    
    private final DataManager dataManager;
    
    /**
     * Constructor del servicio de gestión de ciudades
     * 
     * @param dataManager El gestor de datos para persistencia
     */
    public CityManagementService(DataManager dataManager) {
        this.dataManager = dataManager;
    }
    
    // ========== GESTIÓN DE CIUDADES ==========
    
    /**
     * Crea una nueva ciudad en el sistema
     * 
     * @param city La ciudad a crear
     * @return true si la ciudad se creó exitosamente, false en caso contrario
     * 
     * Restricciones:
     * - La ciudad no puede ser null
     * - No puede existir otra ciudad con el mismo nombre (ignorando mayúsculas/minúsculas)
     */
    public boolean createCity(City city) {
        if (city == null) {
            return false;
        }
        
        try {
            List<City> cities = dataManager.loadList(FileConstants.CITIES_FILE, City::new);
            
            // Verificar nombres duplicados
            boolean nameExists = cities.stream()
                .anyMatch(c -> city.getName().equalsIgnoreCase(c.getName()));
            
            if (nameExists) {
                return false; // Ya existe una ciudad con ese nombre
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
     * Actualiza una ciudad existente en el sistema
     * 
     * @param cityId El ID de la ciudad a actualizar
     * @param updatedCity La información actualizada de la ciudad
     * @return true si la ciudad se actualizó exitosamente, false en caso contrario
     * 
     * Restricciones:
     * - El ID de la ciudad no puede ser null
     * - La ciudad actualizada no puede ser null
     * - La ciudad debe existir en el sistema
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
     * Elimina una ciudad del sistema (solo si no tiene conexiones)
     * 
     * @param cityId El ID de la ciudad a eliminar
     * @return true si la ciudad se eliminó exitosamente, false en caso contrario
     * 
     * Restricciones:
     * - El ID de la ciudad no puede ser null
     * - La ciudad no puede tener conexiones existentes
     * - La ciudad debe existir en el sistema
     */
    public boolean deleteCity(String cityId) {
        if (cityId == null) {
            return false;
        }
        
        try {
            // Verificar conexiones existentes
            List<Connection> connections = dataManager.loadList(FileConstants.CONNECTIONS_FILE, Connection::new);
            boolean hasConnections = connections.stream()
                .anyMatch(conn -> cityId.equals(conn.getFromCityId()) || cityId.equals(conn.getToCityId()));
            
            if (hasConnections) {
                return false; // No se puede eliminar ciudad con conexiones existentes
            }
            
            // Eliminar la ciudad
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
     * Obtiene todas las ciudades del sistema
     * 
     * @return Lista de todas las ciudades
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
     * Busca una ciudad por su ID
     * 
     * @param cityId El ID de la ciudad a buscar
     * @return La ciudad encontrada, o null si no existe
     */
    public City findCityById(String cityId) {
        return getAllCities().stream()
            .filter(city -> cityId.equals(city.getId()))
            .findFirst()
            .orElse(null);
    }
    
    // ========== GESTIÓN DE CONEXIONES ==========
    
    /**
     * Crea una nueva conexión entre ciudades
     * 
     * @param connection La conexión a crear
     * @return true si la conexión se creó exitosamente, false en caso contrario
     * 
     * Restricciones:
     * - La conexión no puede ser null
     * - Las ciudades de origen y destino no pueden ser null
     * - No puede existir una conexión idéntica entre las mismas ciudades
     * - Maneja automáticamente archivos corruptos eliminándolos y creando nuevos
     */
    public boolean createConnection(Connection connection) {
        if (connection == null || connection.getFromCity() == null || connection.getToCity() == null) {
            return false;
        }
        
        try {
            List<Connection> connections;
            
            // Verificar si el archivo de conexiones existe, si no crear lista vacía
            if (dataManager.fileExists(FileConstants.CONNECTIONS_FILE)) {
                try {
                    connections = dataManager.loadList(FileConstants.CONNECTIONS_FILE, Connection::new);
                    // System.out.println("Debug: Loaded " + connections.size() + " existing connections");
                } catch (SerializationException e) {
                    // El archivo existe pero está corrupto, eliminarlo y crear nuevo
                    System.out.println("Debug: Connections file is corrupted, deleting and creating new");
                    dataManager.deleteFile(FileConstants.CONNECTIONS_FILE);
                    connections = new ArrayList<>();
                }
            } else {
                connections = new ArrayList<>();
                // System.out.println("Debug: Connections file not found, creating new list");
            }
            
            // Verificar conexiones duplicadas usando IDs de ciudades
            String fromCityId = connection.getFromCity().getId();
            String toCityId = connection.getToCity().getId();
            
            // System.out.println("Debug: Attempting to create connection from " + fromCityId + " to " + toCityId);
            
            boolean connectionExists = connections.stream()
                .anyMatch(conn -> {
                    // Usar getFromCityId() y getToCityId() para conexiones no resueltas
                    String connFromId = conn.getFromCity() != null ? conn.getFromCity().getId() : conn.getFromCityId();
                    String connToId = conn.getToCity() != null ? conn.getToCity().getId() : conn.getToCityId();
                    boolean matches = fromCityId.equals(connFromId) && toCityId.equals(connToId);
                    // if (matches) {
                    //     System.out.println("Debug: Found existing connection: " + connFromId + " -> " + connToId);
                    // }
                    return matches;
                });
            
            if (connectionExists) {
                // System.out.println("Debug: Connection already exists, returning false");
                return false; // La conexión ya existe
            }
            
            // System.out.println("Debug: Adding new connection to list");
            connections.add(connection);
            dataManager.saveList(connections, FileConstants.CONNECTIONS_FILE);
            // System.out.println("Debug: Connection saved successfully");
            return true;
            
        } catch (SerializationException e) {
            System.err.println("Error creating connection: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Actualiza una conexión existente
     * 
     * @param fromCityId El ID de la ciudad de origen
     * @param toCityId El ID de la ciudad de destino
     * @param updatedConnection La información actualizada de la conexión
     * @return true si la conexión se actualizó exitosamente, false en caso contrario
     * 
     * Restricciones:
     * - Los IDs de las ciudades no pueden ser null
     * - La conexión actualizada no puede ser null
     * - La conexión debe existir en el sistema
     */
    public boolean updateConnection(String fromCityId, String toCityId, Connection updatedConnection) {
        if (fromCityId == null || toCityId == null || updatedConnection == null) {
            return false;
        }
        
        try {
            List<Connection> connections;
            List<City> cities = getAllCities();
            
            // Verificar si el archivo de conexiones existe, si no retornar false
            if (dataManager.fileExists(FileConstants.CONNECTIONS_FILE)) {
                connections = dataManager.loadList(FileConstants.CONNECTIONS_FILE, Connection::new);
                
                // Resolver referencias de ciudades
                Function<String, City> cityResolver = cityId -> 
                    cities.stream().filter(c -> cityId.equals(c.getId())).findFirst().orElse(null);
                
                for (Connection conn : connections) {
                    conn.resolveCityReferences(cityResolver);
                }
                
                // Buscar y actualizar la conexión
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
                // System.out.println("Debug: Connections file not found, nothing to update");
            }
            
        } catch (SerializationException e) {
            System.err.println("Error updating connection: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Elimina una conexión del sistema
     * 
     * @param fromCityId El ID de la ciudad de origen
     * @param toCityId El ID de la ciudad de destino
     * @return true si la conexión se eliminó exitosamente, false en caso contrario
     * 
     * Restricciones:
     * - Los IDs de las ciudades no pueden ser null
     * - La conexión debe existir en el sistema
     */
    public boolean deleteConnection(String fromCityId, String toCityId) {
        if (fromCityId == null || toCityId == null) {
            // System.out.println("Debug: deleteConnection called with null IDs: fromCityId=" + fromCityId + ", toCityId=" + toCityId);
            return false;
        }
        
        try {
            List<Connection> connections;
            List<City> cities = getAllCities();
            
            // Verificar si el archivo de conexiones existe, si no retornar false
            if (dataManager.fileExists(FileConstants.CONNECTIONS_FILE)) {
                connections = dataManager.loadList(FileConstants.CONNECTIONS_FILE, Connection::new);
                
                // Primero migrar conexiones para asegurar que tengan IDs de ciudades apropiados
                migrateConnections(connections, cities);
                
                // System.out.println("Debug: Attempting to delete connection from " + fromCityId + " to " + toCityId);
                // System.out.println("Debug: Found " + connections.size() + " connections to check");
                
                boolean removed = connections.removeIf(conn -> {
                    String connFromId = conn.getFromCityId();
                    String connToId = conn.getToCityId();
                    boolean matches = fromCityId.equals(connFromId) && toCityId.equals(connToId);
                    // System.out.println("Debug: Checking connection " + connFromId + " -> " + connToId + " matches: " + matches);
                    return matches;
                });
                
                // System.out.println("Debug: Connection removed: " + removed);
                
                if (removed) {
                    dataManager.saveList(connections, FileConstants.CONNECTIONS_FILE);
                    return true;
                }
            } else {
                // System.out.println("Debug: Connections file not found, nothing to delete");
            }
            
        } catch (SerializationException e) {
            System.err.println("Error deleting connection: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Obtiene todas las conexiones del sistema
     * 
     * @return Lista de todas las conexiones
     * 
     * Notas:
     * - Maneja automáticamente archivos corruptos eliminándolos y retornando lista vacía
     * - Migra conexiones para asegurar que tengan referencias de ciudades apropiadas
     */
    public List<Connection> getAllConnections() {
        try {
            List<Connection> connections;
            List<City> cities = getAllCities();
            
            // Verificar si el archivo de conexiones existe, si no retornar lista vacía
            if (dataManager.fileExists(FileConstants.CONNECTIONS_FILE)) {
                connections = dataManager.loadList(FileConstants.CONNECTIONS_FILE, Connection::new);
                // System.out.println("Debug: Loaded " + connections.size() + " connections and " + cities.size() + " cities");
                
                // Verificar conexiones corruptas y registrarlas
                long corruptedCount = connections.stream()
                    .filter(conn -> conn.getFromCityId() == null || conn.getToCityId() == null)
                    .count();
                
                if (corruptedCount > 0) {
                    System.out.println("Debug: Found " + corruptedCount + " connections with null city IDs");
                    System.out.println("Debug: Use 'Fix Corrupted Connections' option to resolve this issue");
                }
                
                // Migrar conexiones para asegurar que tengan IDs de ciudades apropiados
                migrateConnections(connections, cities);
            } else {
                connections = new ArrayList<>();
                // System.out.println("Debug: Connections file not found, returning empty list");
            }
            
            return connections;
            
        } catch (SerializationException e) {
            // El archivo existe pero está corrupto, eliminarlo y retornar lista vacía
            System.out.println("Debug: Connections file is corrupted, deleting and returning empty list");
            dataManager.deleteFile(FileConstants.CONNECTIONS_FILE);
            System.err.println("Error loading connections: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Migra conexiones existentes para asegurar que tengan IDs de ciudades apropiados almacenados
     * 
     * @param connections Lista de conexiones a migrar
     * @param cities Lista de ciudades disponibles para resolver referencias
     * 
     * Notas:
     * - Resuelve referencias de ciudades que están null
     * - Guarda automáticamente si se realizan cambios
     */
    private void migrateConnections(List<Connection> connections, List<City> cities) {
        Function<String, City> cityResolver = cityId -> 
            cities.stream().filter(c -> cityId.equals(c.getId())).findFirst().orElse(null);
        
        boolean needsSave = false;
        for (Connection conn : connections) {
            // Si los IDs de ciudades existen pero las referencias de ciudades son null, resolverlas
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
            
            // También resolver cualquier referencia null restante
            conn.resolveCityReferences(cityResolver);
        }
        
        if (needsSave) {
            try {
                dataManager.saveList(connections, FileConstants.CONNECTIONS_FILE);
                // System.out.println("Debug: Migrated and saved connections with proper city IDs");
            } catch (SerializationException e) {
                System.err.println("Error saving migrated connections: " + e.getMessage());
            }
        }
    }
    
    /**
     * Obtiene las conexiones que salen de una ciudad específica
     * 
     * @param cityId El ID de la ciudad
     * @return Lista de conexiones que salen de la ciudad
     */
    public List<Connection> getConnectionsFromCity(String cityId) {
        return getAllConnections().stream()
            .filter(conn -> cityId.equals(conn.getFromCity() != null ? conn.getFromCity().getId() : conn.getFromCityId()))
            .toList();
    }
    
    /**
     * Obtiene las conexiones que llegan a una ciudad específica
     * 
     * @param cityId El ID de la ciudad
     * @return Lista de conexiones que llegan a la ciudad
     */
    public List<Connection> getConnectionsToCity(String cityId) {
        return getAllConnections().stream()
            .filter(conn -> cityId.equals(conn.getToCity() != null ? conn.getToCity().getId() : conn.getToCityId()))
            .toList();
    }
    
    /**
     * Repara conexiones corruptas recreándolas con IDs de ciudades apropiados
     * Este método debe ser llamado si las conexiones tienen IDs de ciudades null
     * 
     * @return true si las conexiones se repararon exitosamente, false en caso contrario
     * 
     * Restricciones:
     * - Debe haber al menos 6 ciudades en el sistema para recrear las conexiones por defecto
     * - Elimina y recrea todas las conexiones existentes
     */
    public boolean fixCorruptedConnections() {
        try {
            List<City> cities = getAllCities();
            
            if (cities.size() < 6) {
                // System.out.println("Debug: Not enough cities to recreate connections");
                return false;
            }
            
            // System.out.println("Debug: Recreating default connections...");
            
            // Eliminar archivo de conexiones existente
            dataManager.deleteFile(FileConstants.CONNECTIONS_FILE);
            
            // Recrear conexiones por defecto
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
            // System.out.println("Debug: Recreated " + defaultConnections.size() + " connections");
            return true;
            
        } catch (SerializationException e) {
            System.err.println("Error recreating connections: " + e.getMessage());
            return false;
        }
    }
}
