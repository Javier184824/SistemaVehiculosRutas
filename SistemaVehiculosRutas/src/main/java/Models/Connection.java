/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import Interfaces.Edge;
import Interfaces.Node;
import Interfaces.Serializable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author JE
 */
public class Connection implements Edge, Serializable{
    
    private City fromCity;
    private City toCity;
    private double distance; // in kilometers
    private int timeMinutes;
    private double cost; // in local currency
    
    public Connection() {}
    
    public Connection(City fromCity, City toCity, double distance, int timeMinutes, double cost) {
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.distance = distance;
        this.timeMinutes = timeMinutes;
        this.cost = cost;
    }
    
    // Edge interface implementation
    @Override
    public Node getFrom() { return fromCity; }
    
    @Override
    public Node getTo() { return toCity; }
    
    @Override
    public double getWeight() { 
        // For pathfinding, use time as the primary weight
        return timeMinutes; 
    }
    
    @Override
    public double getDistance() { return distance; }
    
    @Override
    public int getTimeMinutes() { return timeMinutes; }
    
    @Override
    public double getCost() { return cost; }
    
    // Getters and setters
    public City getFromCity() { return fromCity; }
    public void setFromCity(City fromCity) { this.fromCity = fromCity; }
    
    public City getToCity() { return toCity; }
    public void setToCity(City toCity) { this.toCity = toCity; }
    
    public void setDistance(double distance) { this.distance = distance; }
    public void setTimeMinutes(int timeMinutes) { this.timeMinutes = timeMinutes; }
    public void setCost(double cost) { this.cost = cost; }
    
    /**
     * Creates a reverse connection (toCity -> fromCity)
     */
    public Connection reverse() {
        return new Connection(toCity, fromCity, distance, timeMinutes, cost);
    }
    
    @Override
    public void serialize(DataOutputStream out) throws IOException {
        // Serialize cities by ID only to avoid circular references
        out.writeUTF(fromCity != null ? fromCity.getId() : "");
        out.writeUTF(toCity != null ? toCity.getId() : "");
        out.writeDouble(distance);
        out.writeInt(timeMinutes);
        out.writeDouble(cost);
    }
    
    @Override
    public void deserialize(DataInputStream in) throws IOException {
        // Note: Cities must be resolved by ID after deserialization
        String fromCityId = in.readUTF();
        String toCityId = in.readUTF();
        this.distance = in.readDouble();
        this.timeMinutes = in.readInt();
        this.cost = in.readDouble();
        
        // Temporary storage for city IDs - will be resolved later
        this.fromCityId = fromCityId.isEmpty() ? null : fromCityId;
        this.toCityId = toCityId.isEmpty() ? null : toCityId;
    }
    
    // Temporary fields for deserialization
    private transient String fromCityId;
    private transient String toCityId;
    
    public String getFromCityId() { return fromCityId; }
    public String getToCityId() { return toCityId; }
    
    /**
     * Resolves city references after deserialization
     */
    public void resolveCityReferences(java.util.function.Function<String, City> cityResolver) {
        if (fromCityId != null) {
            this.fromCity = cityResolver.apply(fromCityId);
        }
        if (toCityId != null) {
            this.toCity = cityResolver.apply(toCityId);
        }
    }
    
    @Override
    public String toString() {
        return fromCity.getName() + " -> " + toCity.getName() + 
               " (" + distance + "km, " + timeMinutes + "min, $" + cost + ")";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Connection that = (Connection) obj;
        return fromCity != null ? fromCity.equals(that.fromCity) : that.fromCity == null &&
               toCity != null ? toCity.equals(that.toCity) : that.toCity == null;
    }
    
    @Override
    public int hashCode() {
        int result = fromCity != null ? fromCity.hashCode() : 0;
        result = 31 * result + (toCity != null ? toCity.hashCode() : 0);
        return result;
    }
}
