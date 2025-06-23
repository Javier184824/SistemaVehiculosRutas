/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package User;

import Interfaces.Serializable;
import Vehicle.Vehicle;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author JE
 */
public class User implements Serializable{
    
    private String id;
    private String username;
    private String password; // In production, this should be hashed
    private UserRole role;
    private List<Vehicle> vehicles;
    private String favoriteVehicleId; // ID reference to avoid circular serialization
    
    public User() {
        this.id = UUID.randomUUID().toString();
        this.vehicles = new ArrayList<>();
    }
    
    public User(String username, String password, UserRole role) {
        this();
        this.username = username;
        this.password = password;
        this.role = role;
    }
    
    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
    
    public List<Vehicle> getVehicles() { return vehicles; }
    public void setVehicles(List<Vehicle> vehicles) { this.vehicles = vehicles; }
    
    public String getFavoriteVehicleId() { return favoriteVehicleId; }
    public void setFavoriteVehicleId(String favoriteVehicleId) { this.favoriteVehicleId = favoriteVehicleId; }
    
    /**
     * Gets the favorite vehicle object from the list
     */
    public Vehicle getFavoriteVehicle() {
        if (favoriteVehicleId == null) return null;
        return vehicles.stream()
                .filter(v -> favoriteVehicleId.equals(v.getId()))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Sets the favorite vehicle by object reference
     */
    public void setFavoriteVehicle(Vehicle vehicle) {
        this.favoriteVehicleId = vehicle != null ? vehicle.getId() : null;
    }
    
    public void addVehicle(Vehicle vehicle) {
        if (!vehicles.contains(vehicle)) {
            vehicles.add(vehicle);
        }
    }
    
    public boolean removeVehicle(Vehicle vehicle) {
        if (favoriteVehicleId != null && favoriteVehicleId.equals(vehicle.getId())) {
            favoriteVehicleId = null;
        }
        return vehicles.remove(vehicle);
    }
    
    public boolean isAdmin() {
        return role == UserRole.ADMIN;
    }
    
    @Override
    public void serialize(DataOutputStream out) throws IOException {
        out.writeUTF(id != null ? id : "");
        out.writeUTF(username != null ? username : "");
        out.writeUTF(password != null ? password : "");
        out.writeUTF(role != null ? role.name() : UserRole.USER.name());
        out.writeUTF(favoriteVehicleId != null ? favoriteVehicleId : "");
        
        // Serialize vehicles list
        out.writeInt(vehicles.size());
        for (Vehicle vehicle : vehicles) {
            vehicle.serialize(out);
        }
    }
    
    @Override
    public void deserialize(DataInputStream in) throws IOException {
        this.id = in.readUTF();
        this.username = in.readUTF();
        this.password = in.readUTF();
        this.role = UserRole.valueOf(in.readUTF());
        String favVehicleId = in.readUTF();
        this.favoriteVehicleId = favVehicleId.isEmpty() ? null : favVehicleId;
        
        // Deserialize vehicles list
        int vehicleCount = in.readInt();
        this.vehicles = new ArrayList<>();
        for (int i = 0; i < vehicleCount; i++) {
            // This requires Vehicle.deserializeFromStream() static method
            Vehicle vehicle = Vehicle.deserializeFromStream(in);
            vehicles.add(vehicle);
        }
    }
    
    @Override
    public String toString() {
        return username + " (" + role.getDisplayName() + ")";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return id != null ? id.equals(user.id) : user.id == null;
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
