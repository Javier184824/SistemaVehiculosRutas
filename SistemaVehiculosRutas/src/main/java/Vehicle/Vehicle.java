/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vehicle;

import Interfaces.Serializable;
import Models.Station;
import Models.FuelStation;
import Models.ChargingStation;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 *
 * @author JE
 */
public abstract class Vehicle implements Serializable{
    
    protected String id;
    protected String make;
    protected String model;
    protected int year;
    protected String licensePlate;
    
    public Vehicle() {
        this.id = UUID.randomUUID().toString();
    }
    
    public Vehicle(String make, String model, int year, String licensePlate) {
        this();
        this.make = make;
        this.model = model;
        this.year = year;
        this.licensePlate = licensePlate;
    }
    
    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }
    
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    
    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }
    
    /**
     * Gets the energy type used by this vehicle
     * @return energy type string
     */
    public abstract String getEnergyType();
    
    /**
     * Checks if this vehicle can use a specific station
     * @param station the station to check
     * @return true if compatible
     */
    public abstract boolean isCompatibleWith(Station station);
    
    @Override
    public void serialize(DataOutputStream out) throws IOException {
        out.writeUTF(getClass().getSimpleName()); // Write class type
        out.writeUTF(id != null ? id : "");
        out.writeUTF(make != null ? make : "");
        out.writeUTF(model != null ? model : "");
        out.writeInt(year);
        out.writeUTF(licensePlate != null ? licensePlate : "");
        serializeSpecific(out);
    }
    
    @Override
    public void deserialize(DataInputStream in) throws IOException {
        // Class type already read by factory method
        this.id = in.readUTF();
        this.make = in.readUTF();
        this.model = in.readUTF();
        this.year = in.readInt();
        this.licensePlate = in.readUTF();
        deserializeSpecific(in);
    }
    
    /**
     * Serializes vehicle-specific fields
     */
    protected abstract void serializeSpecific(DataOutputStream out) throws IOException;
    
    /**
     * Deserializes vehicle-specific fields
     */
    protected abstract void deserializeSpecific(DataInputStream in) throws IOException;
    
    /**
     * Factory method to deserialize vehicles from stream
     */
    public static Vehicle deserializeFromStream(DataInputStream in) throws IOException {
        String className = in.readUTF();
        Vehicle vehicle;
        
        switch (className) {
            case "FuelVehicle":
                vehicle = new FuelVehicle();
                break;
            case "ElectricVehicle":
                vehicle = new ElectricVehicle();
                break;
            default:
                throw new IOException("Unknown vehicle type: " + className);
        }
        
        vehicle.deserialize(in);
        return vehicle;
    }
    
    @Override
    public String toString() {
        return year + " " + make + " " + model + " (" + getEnergyType() + ")";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vehicle vehicle = (Vehicle) obj;
        return id != null ? id.equals(vehicle.id) : vehicle.id == null;
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
