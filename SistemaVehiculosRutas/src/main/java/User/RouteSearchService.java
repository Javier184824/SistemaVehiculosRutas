/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package User;

import Models.City;
import Models.Route;
import Models.Station;
import Services.RouteService;
import Services.VehicleService;
import Vehicle.Vehicle;
import java.util.List;

/**
 *
 * @author JE
 */
public class RouteSearchService {
    
    private final RouteService routeService;
    private final VehicleService vehicleService;
    
    public RouteSearchService(RouteService routeService, VehicleService vehicleService) {
        this.routeService = routeService;
        this.vehicleService = vehicleService;
    }
    
    /**
     * Finds routes between two cities
     * @param fromCityId origin city ID
     * @param toCityId destination city ID
     * @return list of available routes
     */
    public List<Route> findRoutes(String fromCityId, String toCityId) {
        return routeService.findRoutes(fromCityId, toCityId);
    }
    
    /**
     * Gets all available cities for route planning
     * @return list of cities
     */
    public List<City> getAllCities() {
        return routeService.getAllCities();
    }
    
    /**
     * Gets compatible stations on a route for the current user's favorite vehicle
     * @param route the route
     * @return list of compatible stations, or empty list if no favorite vehicle
     */
    public List<Station> getCompatibleStationsOnRoute(Route route) {
        Vehicle favoriteVehicle = vehicleService.getCurrentUserFavoriteVehicle();
        if (favoriteVehicle == null) {
            return List.of();
        }
        
        return routeService.getCompatibleStationsOnRoute(route, favoriteVehicle);
    }
    
    /**
     * Gets compatible stations on a route for a specific vehicle
     * @param route the route
     * @param vehicle the vehicle
     * @return list of compatible stations
     */
    public List<Station> getCompatibleStationsOnRoute(Route route, Vehicle vehicle) {
        return routeService.getCompatibleStationsOnRoute(route, vehicle);
    }
    
    /**
     * Gets all stations in a city
     * @param cityId the city ID
     * @return list of stations
     */
    public List<Station> getStationsInCity(String cityId) {
        return routeService.getStationsInCity(cityId);
    }
    
    /**
     * Gets current user's vehicles for route planning
     * @return list of user's vehicles
     */
    public List<Vehicle> getCurrentUserVehicles() {
        return vehicleService.getCurrentUserVehicles();
    }
    
    /**
     * Gets current user's favorite vehicle
     * @return favorite vehicle or null
     */
    public Vehicle getCurrentUserFavoriteVehicle() {
        return vehicleService.getCurrentUserFavoriteVehicle();
    }
    
    /**
     * Finds a city by name (case-insensitive)
     * @param cityName the city name
     * @return the city or null if not found
     */
    public City findCityByName(String cityName) {
        return getAllCities().stream()
            .filter(city -> city.getName().equalsIgnoreCase(cityName))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Gets detailed route information including stations
     * @param route the route
     * @param vehicle the vehicle (optional, uses favorite if null)
     * @return formatted route details
     */
    public String getDetailedRouteInfo(Route route, Vehicle vehicle) {
        if (route == null) {
            return "No route information available.";
        }
        
        Vehicle selectedVehicle = vehicle != null ? vehicle : getCurrentUserFavoriteVehicle();
        
        StringBuilder info = new StringBuilder();
        info.append("🛣️ Route Details:\n");
        info.append("═".repeat(60)).append("\n");
        info.append(String.format("From: %s → To: %s\n", 
            route.getOrigin().getName(), route.getDestination().getName()));
        info.append(String.format("Distance: %.1f km\n", route.getTotalDistance()));
        info.append(String.format("Time: %s\n", route.getFormattedTotalTime()));
        info.append(String.format("Cost: $%.2f\n", route.getTotalCost()));
        
        // Show route path
        info.append("\n📍 Route Path:\n");
        List<City> cities = route.getCities();
        for (int i = 0; i < cities.size(); i++) {
            info.append(String.format("%d. %s", i + 1, cities.get(i).getName()));
            if (i < cities.size() - 1) {
                info.append(" → ");
            }
            info.append("\n");
        }
        
        // Show compatible stations if vehicle is selected
        if (selectedVehicle != null) {
            List<Station> compatibleStations = getCompatibleStationsOnRoute(route, selectedVehicle);
            
            info.append(String.format("\n⛽ Compatible Stations for %s:\n", selectedVehicle.toString()));
            if (compatibleStations.isEmpty()) {
                info.append("No compatible stations found on this route.\n");
            } else {
                for (Station station : compatibleStations) {
                    info.append(String.format("• %s in %s (%s)\n", 
                        station.getName(), station.getCity().getName(), station.getStationType()));
                }
            }
        }
        
        return info.toString();
    }
}
