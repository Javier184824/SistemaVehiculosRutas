/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Services;

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
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 *
 * @author JE
 */
public class RouteService {
    
    private final DataManager dataManager;
    private AdjacencyMatrixGraph<CityNode> graph;
    
    public RouteService(DataManager dataManager) {
        this.dataManager = dataManager;
        loadGraphFromData();
    }
    
    /**
     * Loads the graph from persisted data
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
     * Builds the graph from cities and connections
     */
    private void buildGraph(List<City> cities, List<Connection> connections) {
        this.graph = new AdjacencyMatrixGraph<>();
        
        // Add all cities as nodes
        for (City city : cities) {
            graph.addNode(new CityNode(city));
        }
        
        // Resolve city references in connections and add edges
        for (Connection connection : connections) {
            // Resolve city references by ID
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
     * Finds a city by ID from a list
     */
    private City findCityById(List<City> cities, String cityId) {
        if (cityId == null) return null;
        return cities.stream()
            .filter(city -> cityId.equals(city.getId()))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Refreshes the graph from current data
     */
    public void refreshGraph() {
        loadGraphFromData();
    }
    
    /**
     * Finds routes between two cities
     * @param fromCityId the origin city ID
     * @param toCityId the destination city ID
     * @return list of possible routes
     */
    public List<Route> findRoutes(String fromCityId, String toCityId) {
        if (graph == null || fromCityId == null || toCityId == null) {
            return new ArrayList<>();
        }
        
        CityNode fromNode = null;
        CityNode toNode = null;
        
        // Find the city nodes
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
        
        // Find shortest path
        List<CityNode> shortestPath = graph.findPath(fromNode, toNode);
        List<Route> routes = new ArrayList<>();
        
        if (!shortestPath.isEmpty()) {
            Route route = createRouteFromPath(shortestPath);
            if (route != null) {
                routes.add(route);
            }
        }
        
        // Optionally find alternative paths
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
     * Creates a Route object from a path of city nodes
     */
    private Route createRouteFromPath(List<CityNode> path) {
        if (path.size() < 2) {
            return null;
        }
        
        List<City> cities = path.stream().map(CityNode::getCity).collect(Collectors.toList());
        
        List<Connection> connections = new ArrayList<>();
        
        // Build connections from consecutive cities in path
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
     * Gets all available cities
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
     * Gets stations along a route that are compatible with a vehicle
     * @param route the route
     * @param vehicle the vehicle
     * @return list of compatible stations in route order
     */
    public List<Station> getCompatibleStationsOnRoute(Route route, Vehicle vehicle) {
        if (route == null || vehicle == null) {
            return new ArrayList<>();
        }
        
        return route.getCompatibleStations(vehicle);
    }
    
    /**
     * Gets all stations in a city
     * @param cityId the city ID
     * @return list of stations in the city
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
     * Gets graph statistics
     * @return graph statistics string
     */
    public String getGraphStatistics() {
        return graph != null ? graph.getStatistics() : "Graph not loaded";
    }
    
    /**
     * Checks if the graph is connected
     * @return true if all cities are reachable from each other
     */
    public boolean isGraphConnected() {
        return graph != null && graph.isConnected();
    }
}
