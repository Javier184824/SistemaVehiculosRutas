/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Binary;

import Interfaces.DataManager;
import Interfaces.Serializable;
import Interfaces.SerializableFactory;
import Interfaces.SerializationException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JE
 */

public class BinaryDataManager implements DataManager {
    private final String dataDirectory;
    private static final String DATA_DIR_DEFAULT = "data";
    
    public BinaryDataManager() {
        this(DATA_DIR_DEFAULT);
    }
    
    public BinaryDataManager(String dataDirectory) {
        this.dataDirectory = dataDirectory;
        createDataDirectoryIfNotExists();
    }
    
    /**
     * Creates the data directory if it doesn't exist
     */
    private void createDataDirectoryIfNotExists() {
        try {
            Path path = Paths.get(dataDirectory);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create data directory: " + dataDirectory, e);
        }
    }
    
    /**
     * Gets the full file path
     */
    private String getFilePath(String filename) {
        return dataDirectory + File.separator + filename;
    }
    
    @Override
    public void save(Serializable object, String filename) throws SerializationException {
        if (object == null) {
            throw new SerializationException("Cannot save null object");
        }
        
        String filePath = getFilePath(filename);
        
        try (FileOutputStream fos = new FileOutputStream(filePath);
             DataOutputStream dos = new DataOutputStream(fos)) {
            
            object.serialize(dos);
            dos.flush();
            
        } catch (IOException e) {
            throw new SerializationException("Failed to save object to file: " + filename, e);
        }
    }
    
    @Override
    public void saveList(List<? extends Serializable> objects, String filename) throws SerializationException {
        if (objects == null) {
            throw new SerializationException("Cannot save null list");
        }
        
        String filePath = getFilePath(filename);
        
        try (FileOutputStream fos = new FileOutputStream(filePath);
             DataOutputStream dos = new DataOutputStream(fos)) {
            
            // Write the number of objects first
            dos.writeInt(objects.size());
            
            // Write each object
            for (Serializable object : objects) {
                if (object == null) {
                    throw new SerializationException("Cannot save null object in list");
                }
                object.serialize(dos);
            }
            
            dos.flush();
            
        } catch (IOException e) {
            throw new SerializationException("Failed to save list to file: " + filename, e);
        }
    }
    
    @Override
    public <T extends Serializable> T load(String filename, SerializableFactory<T> factory) throws SerializationException {
        if (factory == null) {
            throw new SerializationException("Factory cannot be null");
        }
        
        String filePath = getFilePath(filename);
        
        if (!fileExists(filename)) {
            throw new SerializationException("File does not exist: " + filename);
        }
        
        try (FileInputStream fis = new FileInputStream(filePath);
             DataInputStream dis = new DataInputStream(fis)) {
            
            T object = factory.create();
            object.deserialize(dis);
            return object;
            
        } catch (IOException e) {
            throw new SerializationException("Failed to load object from file: " + filename, e);
        }
    }
    
    @Override
    public <T extends Serializable> List<T> loadList(String filename, SerializableFactory<T> factory) throws SerializationException {
        if (factory == null) {
            throw new SerializationException("Factory cannot be null");
        }
        
        String filePath = getFilePath(filename);
        
        if (!fileExists(filename)) {
            return new ArrayList<>(); // Return empty list if file doesn't exist
        }
        
        try (FileInputStream fis = new FileInputStream(filePath);
             DataInputStream dis = new DataInputStream(fis)) {
            
            int count = dis.readInt();
            List<T> objects = new ArrayList<>(count);
            
            for (int i = 0; i < count; i++) {
                T object = factory.create();
                object.deserialize(dis);
                objects.add(object);
            }
            
            return objects;
            
        } catch (IOException e) {
            throw new SerializationException("Failed to load list from file: " + filename, e);
        }
    }
    
    @Override
    public boolean fileExists(String filename) {
        String filePath = getFilePath(filename);
        return Files.exists(Paths.get(filePath));
    }
    
    @Override
    public boolean deleteFile(String filename) {
        try {
            String filePath = getFilePath(filename);
            return Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            return false;
        }
    }
    
    /**
     * Gets the size of a file in bytes
     */
    public long getFileSize(String filename) {
        try {
            String filePath = getFilePath(filename);
            return Files.size(Paths.get(filePath));
        } catch (IOException e) {
            return -1;
        }
    }
    
    /**
     * Lists all files in the data directory
     */
    public List<String> listFiles() {
        List<String> files = new ArrayList<>();
        try {
            Files.list(Paths.get(dataDirectory))
                 .filter(Files::isRegularFile)
                 .forEach(path -> files.add(path.getFileName().toString()));
        } catch (IOException e) {
            // Return empty list on error
        }
        return files;
    }
    
    /**
     * Creates a backup of a file
     */
    public boolean backupFile(String filename) {
        if (!fileExists(filename)) {
            return false;
        }
        
        try {
            String originalPath = getFilePath(filename);
            String backupPath = getFilePath(filename + ".backup");
            Files.copy(Paths.get(originalPath), Paths.get(backupPath));
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    /**
     * Restores a file from backup
     */
    public boolean restoreFromBackup(String filename) {
        String backupFilename = filename + ".backup";
        if (!fileExists(backupFilename)) {
            return false;
        }
        
        try {
            String backupPath = getFilePath(backupFilename);
            String originalPath = getFilePath(filename);
            Files.copy(Paths.get(backupPath), Paths.get(originalPath));
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    /**
     * Gets information about the data directory
     */
    public String getDirectoryInfo() {
        List<String> files = listFiles();
        long totalSize = files.stream()
                             .mapToLong(this::getFileSize)
                             .filter(size -> size >= 0)
                             .sum();
        
        return String.format("Data Directory: %s%nFiles: %d%nTotal Size: %d bytes", 
                           dataDirectory, files.size(), totalSize);
    }
    
    @Override
    public String toString() {
        return "BinaryDataManager{dataDirectory='" + dataDirectory + "'}";
    }
}