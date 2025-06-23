/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import Vehicle.FuelType;
import java.util.List;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author JE
 */
public class FuelStation extends Station{
    
    private List<FuelType> availableFuels;
    
    public FuelStation() {
        super();
        this.availableFuels = new ArrayList<>();
    }
    
    public FuelStation(String name, String address, List<FuelType> availableFuels) {
        super(name, address);
        this.availableFuels = new ArrayList<>(availableFuels);
    }
    
    public List<FuelType> getAvailableFuels() { return availableFuels; }
    public void setAvailableFuels(List<FuelType> availableFuels) { 
        this.availableFuels = new ArrayList<>(availableFuels); 
    }
    
    public void addFuelType(FuelType fuelType) {
        if (!availableFuels.contains(fuelType)) {
            availableFuels.add(fuelType);
        }
    }
    
    public boolean removeFuelType(FuelType fuelType) {
        return availableFuels.remove(fuelType);
    }
    
    public boolean supportsFuelType(FuelType fuelType) {
        return availableFuels.contains(fuelType);
    }
    
    @Override
    public String getStationType() {
        return "Fuel Station";
    }
    
    @Override
    protected void serializeSpecific(DataOutputStream out) throws IOException {
        out.writeInt(availableFuels.size());
        for (FuelType fuel : availableFuels) {
            fuel.serialize(out);
        }
    }
    
    @Override
    protected void deserializeSpecific(DataInputStream in) throws IOException {
        int fuelCount = in.readInt();
        this.availableFuels = new ArrayList<>();
        for (int i = 0; i < fuelCount; i++) {
            FuelType fuel = new FuelType();
            fuel.deserialize(in);
            availableFuels.add(fuel);
        }
    }
    
    @Override
    public String toString() {
        return super.toString() + " - Fuels: " + availableFuels.size();
    }
}
