/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vehicle;

import Interfaces.Serializable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author JE
 */
public class ChargerType implements Serializable{
    
    private String id;
    private String name;
    private String standard; // e.g., "SAE J1772", "CHAdeMO", "Tesla"
    private int maxPowerKW;
    
    public ChargerType() {} // Required for deserialization
    
    public ChargerType(String id, String name, String standard, int maxPowerKW) {
        this.id = id;
        this.name = name;
        this.standard = standard;
        this.maxPowerKW = maxPowerKW;
    }
    
    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getStandard() { return standard; }
    public void setStandard(String standard) { this.standard = standard; }
    
    public int getMaxPowerKW() { return maxPowerKW; }
    public void setMaxPowerKW(int maxPowerKW) { this.maxPowerKW = maxPowerKW; }
    
    @Override
    public void serialize(DataOutputStream out) throws IOException {
        out.writeUTF(id != null ? id : "");
        out.writeUTF(name != null ? name : "");
        out.writeUTF(standard != null ? standard : "");
        out.writeInt(maxPowerKW);
    }
    
    @Override
    public void deserialize(DataInputStream in) throws IOException {
        this.id = in.readUTF();
        this.name = in.readUTF();
        this.standard = in.readUTF();
        this.maxPowerKW = in.readInt();
    }
    
    @Override
    public String toString() {
        return name + " (" + standard + ", " + maxPowerKW + "kW)";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ChargerType that = (ChargerType) obj;
        return id != null ? id.equals(that.id) : that.id == null;
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
