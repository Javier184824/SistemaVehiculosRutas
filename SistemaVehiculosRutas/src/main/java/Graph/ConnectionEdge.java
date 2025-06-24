/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Graph;

import Interfaces.Edge;
import Interfaces.Node;
import Models.Connection;

/**
 *
 * @author JE
 */
public class ConnectionEdge implements Edge {
    private final Connection connection;
    
    public ConnectionEdge(Connection connection) {
        this.connection = connection;
    }
    
    public Connection getConnection() {
        return connection;
    }
    
    @Override
    public Node getFrom() {
        return new CityNode(connection.getFromCity());
    }
    
    @Override
    public Node getTo() {
        return new CityNode(connection.getToCity());
    }
    
    @Override
    public double getWeight() {
        return connection.getWeight();
    }
    
    @Override
    public double getDistance() {
        return connection.getDistance();
    }
    
    @Override
    public int getTimeMinutes() {
        return connection.getTimeMinutes();
    }
    
    @Override
    public double getCost() {
        return connection.getCost();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ConnectionEdge that = (ConnectionEdge) obj;
        return connection.equals(that.connection);
    }
    
    @Override
    public int hashCode() {
        return connection.hashCode();
    }
    
    @Override
    public String toString() {
        return connection.toString();
    }
}