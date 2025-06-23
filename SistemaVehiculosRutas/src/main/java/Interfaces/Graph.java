/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interfaces;

import java.util.List;

/**
 *
 * @author JE
 */
public interface Graph<T extends Node> {
    boolean addNode(T node);
    
    boolean addEdge(Edge edge);
    
    List<T> getNodes();
    
    List<Edge> getEdges();
    
    List<T> findPath(T from, T to);
    
    T getNode(String id);
}
