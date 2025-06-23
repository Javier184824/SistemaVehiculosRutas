/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import Interfaces.Node;
import Interfaces.Serializable;
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
public class City implements Node, Serializable {
    private String id;
    private String name;
    private double latitude;
    private double longitude;
    private List<Station> stations;
    
    /**
     * Default constructor for deserialization
     */
    public City() {
        this.id = UUID.randomUUID().toString();
        this.stations = new ArrayList<>();
    }
    
    /**
     * Constructor with city details
     * @param name the city name
     * @param latitude the latitude coordinate
     * @param longitude the longitude coordinate
     */
    public City(String name, double latitude, double longitude) {
        this();
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    // ========== GETTERS AND SETTERS ==========
    
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    
    public List<Station> getStations() { return stations; }
    public void setStations(List<Station> stations) { this.stations = stations; }
    
    // ========== NODE INTERFACE IMPLEMENTATION ==========
    
    @Override
    public String getId() { return id; }
    
    // ========== STATION MANAGEMENT ==========
    
    /**
     * Adds a station to this city
     * @param station the station to add
     */
    public void addStation(Station station) {
        if (!stations.contains(station)) {
            stations.add(station);
            station.setCity(this);
        }
    }
    
    /**
     * Removes a station from this city
     * @param station the station to remove
     * @return true if removed successfully
     */
    public boolean removeStation(Station station) {
        if (stations.remove(station)) {
            station.setCity(null);
            return true;
        }
        return false;
    }
    
    // ========== SERIALIZATION ==========
    
    @Override
    public void serialize(DataOutputStream out) throws IOException {
        out.writeUTF(id != null ? id : "");
        out.writeUTF(name != null ? name : "");
        out.writeDouble(latitude);
        out.writeDouble(longitude);
        
        out.writeInt(stations.size());
        for (Station station : stations) {
            station.serialize(out);
        }
    }
    
    @Override
    public void deserialize(DataInputStream in) throws IOException {
        this.id = in.readUTF();
        this.name = in.readUTF();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        
        int stationCount = in.readInt();
        this.stations = new ArrayList<>();
        for (int i = 0; i < stationCount; i++) {
            Station station = Station.deserializeFromStream(in);
            station.setCity(this);
            stations.add(station);
        }
    }
    
    // ========== OBJECT METHODS ==========
    
    @Override
    public String toString() {
        return name;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        City city = (City) obj;
        return id != null ? id.equals(city.id) : city.id == null;
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}