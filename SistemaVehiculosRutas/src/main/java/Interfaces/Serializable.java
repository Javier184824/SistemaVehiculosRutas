/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interfaces;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author JE
 */
public interface Serializable {
    
    void serialize(DataOutputStream out) throws IOException;
    
    void deserialize(DataInputStream in) throws IOException;
}
