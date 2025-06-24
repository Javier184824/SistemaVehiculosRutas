/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Graph;

import java.util.ArrayList;
import java.util.List;

import Interfaces.Node;
import Models.City;

/**
 *
 * @author JE
 */
public class CityNode implements Node {
    private final City city;
    
    public CityNode(City city) {
        this.city = city;
    }
    
    public City getCity() {
        return city;
    }
    
    @Override
    public String getId() {
        return city.getId();
    }
    
    @Override
    public String getName() {
        return city.getName();
    }
    
    /**
     * This method is implemented by the graph, not the node
     * Returns empty list as nodes don't maintain their own connections
     */
    public List<Node> getConnectedNodes() {
        return new ArrayList<>();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CityNode cityNode = (CityNode) obj;
        return city.equals(cityNode.city);
    }
    
    @Override
    public int hashCode() {
        return city.hashCode();
    }
    
    @Override
    public String toString() {
        return city.toString();
    }
}