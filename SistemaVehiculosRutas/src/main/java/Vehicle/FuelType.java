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
public class FuelType implements Serializable{
    
    private String id;
    private String name;
    private String description;
    
    public FuelType() {} // Required for deserialization
    
    public FuelType(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
    
    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    @Override
    public void serialize(DataOutputStream out) throws IOException {
        out.writeUTF(id != null ? id : "");
        out.writeUTF(name != null ? name : "");
        out.writeUTF(description != null ? description : "");
    }
    
    @Override
    public void deserialize(DataInputStream in) throws IOException {
        this.id = in.readUTF();
        this.name = in.readUTF();
        this.description = in.readUTF();
    }
    
    @Override
    public String toString() {
        return name;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        FuelType fuelType = (FuelType) obj;
        return id != null ? id.equals(fuelType.id) : fuelType.id == null;
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
