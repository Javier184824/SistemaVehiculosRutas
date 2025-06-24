/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interfaces;

/**
 *
 * @author JE
 */
public interface Node {
    String getId();
    
    String getName();
    
    boolean equals(Object other);
    
    int hashCode();
}
