/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import Vehicle.ChargerType;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JE
 */
public class ChargingStation extends Station{
    
    private List<ChargerType> availableChargers;
    private boolean isPublic;
    
    public ChargingStation() {
        super();
        this.availableChargers = new ArrayList<>();
        this.isPublic = true;
    }
    
    public ChargingStation(String name, String address, List<ChargerType> availableChargers, boolean isPublic) {
        super(name, address);
        this.availableChargers = new ArrayList<>(availableChargers);
        this.isPublic = isPublic;
    }
    
    public List<ChargerType> getAvailableChargers() { return availableChargers; }
    public void setAvailableChargers(List<ChargerType> availableChargers) { 
        this.availableChargers = new ArrayList<>(availableChargers); 
    }
    
    public boolean isPublic() { return isPublic; }
    public void setPublic(boolean isPublic) { this.isPublic = isPublic; }
    
    public void addChargerType(ChargerType chargerType) {
        if (!availableChargers.contains(chargerType)) {
            availableChargers.add(chargerType);
        }
    }
    
    public boolean removeChargerType(ChargerType chargerType) {
        return availableChargers.remove(chargerType);
    }
    
    public boolean supportsChargerType(ChargerType chargerType) {
        return availableChargers.contains(chargerType);
    }
    
    @Override
    public String getStationType() {
        return "Charging Station" + (isPublic ? " (Public)" : " (Private)");
    }
    
    @Override
    protected void serializeSpecific(DataOutputStream out) throws IOException {
        out.writeInt(availableChargers.size());
        for (ChargerType charger : availableChargers) {
            charger.serialize(out);
        }
        out.writeBoolean(isPublic);
    }
    
    @Override
    protected void deserializeSpecific(DataInputStream in) throws IOException {
        int chargerCount = in.readInt();
        this.availableChargers = new ArrayList<>();
        for (int i = 0; i < chargerCount; i++) {
            ChargerType charger = new ChargerType();
            charger.deserialize(in);
            availableChargers.add(charger);
        }
        this.isPublic = in.readBoolean();
    }
    
    @Override
    public String toString() {
        return super.toString() + " - Chargers: " + availableChargers.size();
    }
}
