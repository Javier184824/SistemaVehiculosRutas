/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interfaces;

/**
 *
 * @author JE
 */
public interface Edge {
    
    Node getFrom();
    
    Node getTo();
    
    double getWeight();
    
    double getDistance();
    
    int getTimeMinutes();
    
    double getCost();
}
