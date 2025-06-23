/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Services;

import Interfaces.DataManager;
import User.User;
import Vehicle.Vehicle;
import java.util.List;

/**
 *
 * @author JE
 */
public class VehicleService {
    
    private final DataManager dataManager;
    private final AuthenticationService authService;
    
    public VehicleService(DataManager dataManager, AuthenticationService authService) {
        this.dataManager = dataManager;
        this.authService = authService;
    }
    
    /**
     * Adds a vehicle to the current user
     * @param vehicle the vehicle to add
     * @return true if added successfully
     */
    public boolean addVehicle(Vehicle vehicle) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null || vehicle == null) {
            return false;
        }
        
        currentUser.addVehicle(vehicle);
        return authService.updateCurrentUser(currentUser);
    }
    
    /**
     * Removes a vehicle from the current user
     * @param vehicle the vehicle to remove
     * @return true if removed successfully
     */
    public boolean removeVehicle(Vehicle vehicle) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null || vehicle == null) {
            return false;
        }
        
        boolean removed = currentUser.removeVehicle(vehicle);
        if (removed) {
            return authService.updateCurrentUser(currentUser);
        }
        
        return false;
    }
    
    /**
     * Updates a vehicle for the current user
     * @param vehicleId the ID of the vehicle to update
     * @param updatedVehicle the updated vehicle information
     * @return true if updated successfully
     */
    public boolean updateVehicle(String vehicleId, Vehicle updatedVehicle) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null || vehicleId == null || updatedVehicle == null) {
            return false;
        }
        
        List<Vehicle> vehicles = currentUser.getVehicles();
        for (int i = 0; i < vehicles.size(); i++) {
            if (vehicleId.equals(vehicles.get(i).getId())) {
                vehicles.set(i, updatedVehicle);
                return authService.updateCurrentUser(currentUser);
            }
        }
        
        return false;
    }
    
    /**
     * Sets a vehicle as the favorite for the current user
     * @param vehicle the vehicle to set as favorite
     * @return true if set successfully
     */
    public boolean setFavoriteVehicle(Vehicle vehicle) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            return false;
        }
        
        // Check if vehicle belongs to user
        if (vehicle != null && !currentUser.getVehicles().contains(vehicle)) {
            return false;
        }
        
        currentUser.setFavoriteVehicle(vehicle);
        return authService.updateCurrentUser(currentUser);
    }
    
    /**
     * Gets all vehicles for the current user
     * @return list of vehicles, or empty list if no user
     */
    public List<Vehicle> getCurrentUserVehicles() {
        User currentUser = authService.getCurrentUser();
        return currentUser != null ? currentUser.getVehicles() : List.of();
    }
    
    /**
     * Gets the favorite vehicle for the current user
     * @return the favorite vehicle, or null if none set
     */
    public Vehicle getCurrentUserFavoriteVehicle() {
        User currentUser = authService.getCurrentUser();
        return currentUser != null ? currentUser.getFavoriteVehicle() : null;
    }
    
    /**
     * Finds a vehicle by ID for the current user
     * @param vehicleId the vehicle ID
     * @return the vehicle, or null if not found
     */
    public Vehicle findVehicleById(String vehicleId) {
        List<Vehicle> vehicles = getCurrentUserVehicles();
        return vehicles.stream()
            .filter(v -> vehicleId.equals(v.getId()))
            .findFirst()
            .orElse(null);
    }
}
