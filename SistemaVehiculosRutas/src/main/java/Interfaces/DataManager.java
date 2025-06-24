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
public interface DataManager {

    void save(Serializable object, String filename) throws SerializationException;
    
    void saveList(List<? extends Serializable> objects, String filename) throws SerializationException;
    
    <T extends Serializable> T load(String filename, SerializableFactory<T> factory) throws SerializationException;
    
    <T extends Serializable> List<T> loadList(String filename, SerializableFactory<T> factory) throws SerializationException;
    
    boolean fileExists(String filename);
    
    boolean deleteFile(String filename);
}
