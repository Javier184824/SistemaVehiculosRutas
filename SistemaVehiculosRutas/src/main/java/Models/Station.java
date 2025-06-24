/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import Interfaces.Serializable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 *
 * @author JE
 */
public abstract class Station implements Serializable {
    protected String id;
    protected String name;
    protected String address;
    protected City city;
    
    /**
     * Default constructor
     */
    public Station() {
        this.id = UUID.randomUUID().toString();
    }
    
    /**
     * Constructor with basic station information
     * @param name the station name
     * @param address the station address
     */
    public Station(String name, String address) {
        this();
        this.name = name;
        this.address = address;
    }
    
    // ========== GETTERS AND SETTERS ==========
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public City getCity() { return city; }
    public void setCity(City city) { this.city = city; }
    
    // ========== ABSTRACT METHODS ==========
    
    /**
     * Gets the type of station
     * @return station type string
     */
    public abstract String getStationType();
    
    /**
     * Serializes station-specific fields
     * @param out the output stream
     * @throws IOException if serialization fails
     */
    protected abstract void serializeSpecific(DataOutputStream out) throws IOException;
    
    /**
     * Deserializes station-specific fields
     * @param in the input stream
     * @throws IOException if deserialization fails
     */
    protected abstract void deserializeSpecific(DataInputStream in) throws IOException;
    
    // ========== SERIALIZATION ==========
    
    @Override
    public void serialize(DataOutputStream out) throws IOException {
        out.writeUTF(getClass().getSimpleName()); // Write class type
        out.writeUTF(id != null ? id : "");
        out.writeUTF(name != null ? name : "");
        out.writeUTF(address != null ? address : "");
        serializeSpecific(out);
    }
    
    @Override
    public void deserialize(DataInputStream in) throws IOException {
        // Class type already read by factory method
        this.id = in.readUTF();
        this.name = in.readUTF();
        this.address = in.readUTF();
        deserializeSpecific(in);
    }
    
    /**
     * Factory method to deserialize stations from stream
     * @param in the input stream
     * @return the deserialized station
     * @throws IOException if deserialization fails
     */
    public static Station deserializeFromStream(DataInputStream in) throws IOException {
        String className = in.readUTF();
        Station station;
        
        switch (className) {
            case "FuelStation":
                station = new FuelStation();
                break;
            case "ChargingStation":
                station = new ChargingStation();
                break;
            default:
                throw new IOException("Unknown station type: " + className);
        }
        
        station.deserialize(in);
        return station;
    }
    
    // ========== OBJECT METHODS ==========
    
    @Override
    public String toString() {
        return name + " (" + getStationType() + ")";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Station station = (Station) obj;
        return id != null ? id.equals(station.id) : station.id == null;
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}