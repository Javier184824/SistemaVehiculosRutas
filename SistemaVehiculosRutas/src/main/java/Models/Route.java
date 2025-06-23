/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import Vehicle.Vehicle;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JE
 */
public class Route {
    
    private List<City> cities;
    private List<Connection> connections;
    private double totalDistance;
    private int totalTimeMinutes;
    private double totalCost;
    
    public Route() {
        this.cities = new ArrayList<>();
        this.connections = new ArrayList<>();
    }
    
    public Route(List<City> cities, List<Connection> connections) {
        this.cities = new ArrayList<>(cities);
        this.connections = new ArrayList<>(connections);
        calculateTotals();
    }
    
    public List<City> getCities() { return cities; }
    public void setCities(List<City> cities) { 
        this.cities = new ArrayList<>(cities); 
        calculateTotals();
    }
    
    public List<Connection> getConnections() { return connections; }
    public void setConnections(List<Connection> connections) { 
        this.connections = new ArrayList<>(connections); 
        calculateTotals();
    }
    
    public double getTotalDistance() { return totalDistance; }
    public int getTotalTimeMinutes() { return totalTimeMinutes; }
    public double getTotalCost() { return totalCost; }
    
    public City getOrigin() {
        return cities.isEmpty() ? null : cities.get(0);
    }
    
    public City getDestination() {
        return cities.isEmpty() ? null : cities.get(cities.size() - 1);
    }
    
    /**
     * Calculates total distance, time, and cost for the route
     */
    private void calculateTotals() {
        totalDistance = 0;
        totalTimeMinutes = 0;
        totalCost = 0;
        
        for (Connection connection : connections) {
            totalDistance += connection.getDistance();
            totalTimeMinutes += connection.getTimeMinutes();
            totalCost += connection.getCost();
        }
    }
    
    /**
     * Gets all compatible stations along the route for a specific vehicle
     */
    public List<Station> getCompatibleStations(Vehicle vehicle) {
        List<Station> compatibleStations = new ArrayList<>();
        
        for (City city : cities) {
            for (Station station : city.getStations()) {
                if (vehicle.isCompatibleWith(station)) {
                    compatibleStations.add(station);
                }
            }
        }
        
        return compatibleStations;
    }
    
    /**
     * Formats the total time as hours:minutes
     */
    public String getFormattedTotalTime() {
        int hours = totalTimeMinutes / 60;
        int minutes = totalTimeMinutes % 60;
        return String.format("%d:%02d", hours, minutes);
    }
    
    @Override
    public String toString() {
        if (cities.isEmpty()) {
            return "Empty route";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append(getOrigin().getName()).append(" -> ").append(getDestination().getName());
        sb.append(" (").append(String.format("%.1f", totalDistance)).append("km, ");
        sb.append(getFormattedTotalTime()).append(", $").append(String.format("%.2f", totalCost)).append(")");
        
        if (cities.size() > 2) {
            sb.append(" via ");
            for (int i = 1; i < cities.size() - 1; i++) {
                if (i > 1) sb.append(", ");
                sb.append(cities.get(i).getName());
            }
        }
        
        return sb.toString();
    }
}
