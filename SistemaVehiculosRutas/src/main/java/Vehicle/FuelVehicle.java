/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vehicle;

import Models.FuelStation;
import Models.Station;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author JE
 */
public class FuelVehicle extends Vehicle{
    
    private FuelType fuelType;
    private double tankCapacity; // in liters
    
    public FuelVehicle() {
        super();
    }
    
    public FuelVehicle(String make, String model, int year, String licensePlate, 
                      FuelType fuelType, double tankCapacity) {
        super(make, model, year, licensePlate);
        this.fuelType = fuelType;
        this.tankCapacity = tankCapacity;
    }
    
    public FuelType getFuelType() { return fuelType; }
    public void setFuelType(FuelType fuelType) { this.fuelType = fuelType; }
    
    public double getTankCapacity() { return tankCapacity; }
    public void setTankCapacity(double tankCapacity) { this.tankCapacity = tankCapacity; }
    
    @Override
    public String getEnergyType() {
        return fuelType != null ? fuelType.getName() : "Fuel";
    }
    
    @Override
    public boolean isCompatibleWith(Station station) {
        if (!(station instanceof FuelStation)) {
            return false;
        }
        FuelStation fuelStation = (FuelStation) station;
        return fuelType != null && fuelStation.getAvailableFuels().contains(fuelType);
    }
    
    @Override
    protected void serializeSpecific(DataOutputStream out) throws IOException {
        if (fuelType != null) {
            out.writeBoolean(true);
            fuelType.serialize(out);
        } else {
            out.writeBoolean(false);
        }
        out.writeDouble(tankCapacity);
    }
    
    @Override
    protected void deserializeSpecific(DataInputStream in) throws IOException {
        boolean hasFuelType = in.readBoolean();
        if (hasFuelType) {
            this.fuelType = new FuelType();
            fuelType.deserialize(in);
        }
        this.tankCapacity = in.readDouble();
    }
}
