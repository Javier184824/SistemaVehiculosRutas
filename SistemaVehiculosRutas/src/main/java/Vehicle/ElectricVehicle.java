/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vehicle;

import Models.ChargingStation;
import Models.Station;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JE
 */
public class ElectricVehicle extends Vehicle{
    
    private List<ChargerType> supportedChargers;
    private double batteryCapacity; // in kWh
    
    public ElectricVehicle() {
        super();
        this.supportedChargers = new ArrayList<>();
    }
    
    public ElectricVehicle(String make, String model, int year, String licensePlate,
                          List<ChargerType> supportedChargers, double batteryCapacity) {
        super(make, model, year, licensePlate);
        this.supportedChargers = new ArrayList<>(supportedChargers);
        this.batteryCapacity = batteryCapacity;
    }
    
    public List<ChargerType> getSupportedChargers() { return supportedChargers; }
    public void setSupportedChargers(List<ChargerType> supportedChargers) { 
        this.supportedChargers = new ArrayList<>(supportedChargers); 
    }
    
    public double getBatteryCapacity() { return batteryCapacity; }
    public void setBatteryCapacity(double batteryCapacity) { this.batteryCapacity = batteryCapacity; }
    
    public void addSupportedCharger(ChargerType charger) {
        if (!supportedChargers.contains(charger)) {
            supportedChargers.add(charger);
        }
    }
    
    public boolean removeSupportedCharger(ChargerType charger) {
        return supportedChargers.remove(charger);
    }
    
    @Override
    public String getEnergyType() {
        return "Electric";
    }
    
    @Override
    public boolean isCompatibleWith(Station station) {
        if (!(station instanceof ChargingStation)) {
            return false;
        }
        ChargingStation chargingStation = (ChargingStation) station;
        
        // Check if any supported charger is available at the station
        for (ChargerType supportedCharger : supportedChargers) {
            if (chargingStation.getAvailableChargers().contains(supportedCharger)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    protected void serializeSpecific(DataOutputStream out) throws IOException {
        out.writeInt(supportedChargers.size());
        for (ChargerType charger : supportedChargers) {
            charger.serialize(out);
        }
        out.writeDouble(batteryCapacity);
    }
    
    @Override
    protected void deserializeSpecific(DataInputStream in) throws IOException {
        int chargerCount = in.readInt();
        this.supportedChargers = new ArrayList<>();
        for (int i = 0; i < chargerCount; i++) {
            ChargerType charger = new ChargerType();
            charger.deserialize(in);
            supportedChargers.add(charger);
        }
        this.batteryCapacity = in.readDouble();
    }
}
