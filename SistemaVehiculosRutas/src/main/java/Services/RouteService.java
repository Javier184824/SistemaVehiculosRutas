/*
 * Nombre del Archivo: RouteService.java
 * 
 * Descripcion: Servicio principal de planificación y búsqueda de rutas del sistema.
 *              Gestiona un grafo de ciudades y conexiones para encontrar rutas
 *              óptimas entre ciudades, calcular distancias, tiempos y costos.
 *              Proporciona funcionalidades para análisis de rutas, búsqueda de
 *              estaciones compatibles y estadísticas del grafo de ciudades.
 * 
 * Nombre de los Integrantes:
 * Javier Lee Liang
 * Paulo César Herrera Arias
 * José Emilio Alvarado Mendez
 * Josué Santiago Hidalgo Sandoval
 */
package Services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import Binary.FileConstants;
import Graph.AdjacencyMatrixGraph;
import Graph.CityNode;
import Graph.ConnectionEdge;
import Interfaces.DataManager;
import Interfaces.SerializationException;
import Models.City;
import Models.Connection;
import Models.Route;
import Models.Station;
import Vehicle.Vehicle;

/**
 * Servicio principal de planificación y búsqueda de rutas
 * 
 * Esta clase proporciona funcionalidades completas para la planificación de rutas:
 * - Gestión de un grafo de ciudades y conexiones
 * - Búsqueda de rutas óptimas entre ciudades
 * - Cálculo de distancias, tiempos y costos de rutas
 * - Análisis de estaciones compatibles en rutas
 * - Estadísticas y análisis del grafo de ciudades
 * 
 * Utiliza algoritmos de grafos para encontrar las mejores rutas disponibles.
 */
public class RouteService {
    
    private final DataManager dataManager;
    private AdjacencyMatrixGraph<CityNode> graph;
    
    /**
     * Constructor del servicio de rutas
     * 
     * @param dataManager Gestor de datos para operaciones de persistencia
     * 
     * Notas:
     * - Establece la dependencia del gestor de datos
     * - Carga automáticamente el grafo desde los datos persistentes
     * - Inicializa el grafo de ciudades y conexiones
     */
    public RouteService(DataManager dataManager) {
        this.dataManager = dataManager;
        loadGraphFromData();
    }
    
    /**
     * Carga el grafo desde los datos persistentes
     * 
     * Proceso:
     * - Carga ciudades y conexiones desde archivos
     * - Construye el grafo con los datos cargados
     * - Maneja errores de serialización
     * - Inicializa grafo vacío si hay errores
     */
    private void loadGraphFromData() {
        try {
            List<City> cities = dataManager.loadList(FileConstants.CITIES_FILE, City::new);
            List<Connection> connections = dataManager.loadList(FileConstants.CONNECTIONS_FILE, Connection::new);
            
            buildGraph(cities, connections);
            
        } catch (SerializationException e) {
            System.err.println("Error loading graph data: " + e.getMessage());
            this.graph = new AdjacencyMatrixGraph<>();
        }
    }
    
    /**
     * Construye el grafo a partir de ciudades y conexiones
     * 
     * @param cities Lista de ciudades para agregar como nodos
     * @param connections Lista de conexiones para agregar como aristas
     * 
     * Proceso:
     * - Crea un nuevo grafo de matriz de adyacencia
     * - Agrega todas las ciudades como nodos
     * - Resuelve referencias de ciudades en conexiones
     * - Agrega conexiones como aristas del grafo
     */
    private void buildGraph(List<City> cities, List<Connection> connections) {
        this.graph = new AdjacencyMatrixGraph<>();
        
        // Agregar todas las ciudades como nodos
        for (City city : cities) {
            graph.addNode(new CityNode(city));
        }
        
        // Resolver referencias de ciudades en conexiones y agregar aristas
        for (Connection connection : connections) {
            // Resolver referencias de ciudades por ID
            City fromCity = findCityById(cities, connection.getFromCityId());
            City toCity = findCityById(cities, connection.getToCityId());
            
            if (fromCity != null && toCity != null) {
                connection.setFromCity(fromCity);
                connection.setToCity(toCity);
                graph.addEdge(new ConnectionEdge(connection));
            }
        }
    }
    
    /**
     * Busca una ciudad por ID en una lista
     * 
     * @param cities Lista de ciudades donde buscar
     * @param cityId ID de la ciudad a buscar
     * @return La ciudad encontrada, o null si no existe
     * 
     * Notas:
     * - Utiliza stream para búsqueda eficiente
     * - Retorna null si cityId es null
     * - Comparación exacta por ID
     */
    private City findCityById(List<City> cities, String cityId) {
        if (cityId == null) return null;
        return cities.stream()
            .filter(city -> cityId.equals(city.getId()))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Actualiza el grafo desde los datos actuales
     * 
     * Notas:
     * - Recarga ciudades y conexiones desde archivos
     * - Reconstruye el grafo completo
     * - Útil después de cambios en datos de ciudades o conexiones
     */
    public void refreshGraph() {
        loadGraphFromData();
    }
    
    /**
     * Encuentra rutas entre dos ciudades
     * 
     * @param fromCityId ID de la ciudad de origen
     * @param toCityId ID de la ciudad de destino
     * @return Lista de rutas posibles entre las ciudades
     * 
     * Proceso:
     * - Busca los nodos de las ciudades en el grafo
     * - Encuentra la ruta más corta
     * - Busca rutas alternativas adicionales
     * - Convierte caminos de nodos en objetos Route
     * 
     * Validaciones:
     * - Verifica que el grafo esté cargado
     * - Valida que los IDs de ciudad no sean null
     * - Retorna lista vacía si no encuentra las ciudades
     */
    public List<Route> findRoutes(String fromCityId, String toCityId) {
        if (graph == null || fromCityId == null || toCityId == null) {
            return new ArrayList<>();
        }
        
        CityNode fromNode = null;
        CityNode toNode = null;
        
        // Buscar los nodos de las ciudades
        for (CityNode node : graph.getNodes()) {
            if (fromCityId.equals(node.getId())) {
                fromNode = node;
            }
            if (toCityId.equals(node.getId())) {
                toNode = node;
            }
        }
        
        if (fromNode == null || toNode == null) {
            return new ArrayList<>();
        }
        
        // Encontrar ruta más corta
        List<CityNode> shortestPath = graph.findPath(fromNode, toNode);
        List<Route> routes = new ArrayList<>();
        
        if (!shortestPath.isEmpty()) {
            Route route = createRouteFromPath(shortestPath);
            if (route != null) {
                routes.add(route);
            }
        }
        
        // Opcionalmente encontrar rutas alternativas
        List<List<CityNode>> allPaths = graph.findAllPaths(fromNode, toNode);
        for (List<CityNode> path : allPaths) {
            if (!path.equals(shortestPath)) {
                Route alternativeRoute = createRouteFromPath(path);
                if (alternativeRoute != null) {
                    routes.add(alternativeRoute);
                }
            }
        }
        
        return routes;
    }
    
    /**
     * Crea un objeto Route a partir de un camino de nodos de ciudad
     * 
     * @param path Lista de nodos de ciudad que forman el camino
     * @return El objeto Route creado, o null si el camino es inválido
     * 
     * Proceso:
     * - Extrae las ciudades de los nodos
     * - Construye las conexiones entre ciudades consecutivas
     * - Crea el objeto Route con ciudades y conexiones
     * 
     * Validaciones:
     * - Requiere al menos 2 ciudades para formar una ruta
     * - Maneja casos donde no se encuentran conexiones
     */
    private Route createRouteFromPath(List<CityNode> path) {
        if (path.size() < 2) {
            return null;
        }
        
        List<City> cities = path.stream().map(CityNode::getCity).collect(Collectors.toList());
        
        List<Connection> connections = new ArrayList<>();
        
        // Construir conexiones desde ciudades consecutivas en el camino
        for (int i = 0; i < path.size() - 1; i++) {
            CityNode from = path.get(i);
            CityNode to = path.get(i + 1);
            
            ConnectionEdge edge = (ConnectionEdge) graph.getEdge(from, to);
            if (edge != null) {
                connections.add(edge.getConnection());
            }
        }
        
        return new Route(cities, connections);
    }
    
    /**
     * Obtiene todas las ciudades disponibles
     * 
     * @return Lista de todas las ciudades del sistema
     * 
     * Notas:
     * - Carga desde el archivo de ciudades
     * - Retorna lista vacía si hay errores de carga
     * - Maneja errores de serialización automáticamente
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
     * Obtiene estaciones en una ruta que son compatibles con un vehículo
     * 
     * @param route La ruta a analizar
     * @param vehicle El vehículo para verificar compatibilidad
     * @return Lista de estaciones compatibles en orden de la ruta
     * 
     * Notas:
     * - Retorna lista vacía si route o vehicle son null
     * - Utiliza el método getCompatibleStations del objeto Route
     * - Filtra estaciones según compatibilidad con el vehículo
     */
    public List<Station> getCompatibleStationsOnRoute(Route route, Vehicle vehicle) {
        if (route == null || vehicle == null) {
            return new ArrayList<>();
        }
        
        return route.getCompatibleStations(vehicle);
    }
    
    /**
     * Obtiene todas las estaciones en una ciudad específica
     * 
     * @param cityId ID de la ciudad
     * @return Lista de estaciones en la ciudad especificada
     * 
     * Proceso:
     * - Carga todas las ciudades del sistema
     * - Busca la ciudad por ID
     * - Extrae las estaciones de la ciudad encontrada
     * 
     * Notas:
     * - Retorna lista vacía si no encuentra la ciudad
     * - Maneja errores de serialización
     */
    public List<Station> getStationsInCity(String cityId) {
        try {
            List<City> cities = dataManager.loadList(FileConstants.CITIES_FILE, City::new);
            return cities.stream()
                .filter(city -> cityId.equals(city.getId()))
                .findFirst()
                .map(City::getStations)
                .orElse(new ArrayList<>());
        } catch (SerializationException e) {
            System.err.println("Error loading stations for city: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Obtiene estadísticas del grafo
     * 
     * @return Cadena con estadísticas del grafo
     * 
     * Notas:
     * - Proporciona información sobre nodos, aristas y conectividad
     * - Retorna mensaje informativo si el grafo no está cargado
     * - Útil para análisis y debugging del grafo
     */
    public String getGraphStatistics() {
        return graph != null ? graph.getStatistics() : "Graph not loaded";
    }
    
    /**
     * Verifica si el grafo está conectado
     * 
     * @return true si todas las ciudades son alcanzables entre sí
     * 
     * Notas:
     * - Verifica conectividad del grafo completo
     * - Retorna false si el grafo no está cargado
     * - Útil para validar integridad de la red de ciudades
     */
    public boolean isGraphConnected() {
        return graph != null && graph.isConnected();
    }
}
