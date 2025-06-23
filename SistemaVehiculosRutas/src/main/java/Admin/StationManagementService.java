/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Admin;

import Binary.FileConstants;
import Interfaces.DataManager;
import Interfaces.SerializationException;
import Models.City;
import Models.Station;
import Vehicle.ChargerType;
import Vehicle.FuelType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JE
 */
public class StationManagementService {
    
    private final DataManager dataManager;
    
    public StationManagementService(DataManager dataManager) {
        this.dataManager = dataManager;
    }
    
    // ========== FUEL TYPE MANAGEMENT ==========
    
    /**
     * Creates a new fuel type
     * @param fuelType the fuel type to create
     * @return true if created successfully
     */
    public boolean createFuelType(FuelType fuelType) {
        if (fuelType == null) {
            return false;
        }
        
        try {
            List<FuelType> fuelTypes = dataManager.loadList(FileConstants.FUEL_TYPES_FILE, FuelType::new);
            
            // Check for duplicate IDs
            boolean idExists = fuelTypes.stream()
                .anyMatch(ft -> fuelType.getId().equals(ft.getId()));
            
            if (idExists) {
                return false;
            }
            
            fuelTypes.add(fuelType);
            dataManager.saveList(fuelTypes, FileConstants.FUEL_TYPES_FILE);
            return true;
            
        } catch (SerializationException e) {
            System.err.println("Error creating fuel type: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Updates an existing fuel type
     * @param fuelTypeId the ID of the fuel type to update
     * @param updatedFuelType the updated fuel type information
     * @return true if updated successfully
     */
    public boolean updateFuelType(String fuelTypeId, FuelType updatedFuelType) {
        if (fuelTypeId == null || updatedFuelType == null) {
            return false;
        }
        
        try {
            List<FuelType> fuelTypes = dataManager.loadList(FileConstants.FUEL_TYPES_FILE, FuelType::new);
            
            for (int i = 0; i < fuelTypes.size(); i++) {
                if (fuelTypeId.equals(fuelTypes.get(i).getId())) {
                    fuelTypes.set(i, updatedFuelType);
                    dataManager.saveList(fuelTypes, FileConstants.FUEL_TYPES_FILE);
                    return true;
                }
            }
            
        } catch (SerializationException e) {
            System.err.println("Error updating fuel type: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Deletes a fuel type
     * @param fuelTypeId the ID of the fuel type to delete
     * @return true if deleted successfully
     */
    public boolean deleteFuelType(String fuelTypeId) {
        if (fuelTypeId == null) {
            return false;
        }
        
        try {
            List<FuelType> fuelTypes = dataManager.loadList(FileConstants.FUEL_TYPES_FILE, FuelType::new);
            boolean removed = fuelTypes.removeIf(ft -> fuelTypeId.equals(ft.getId()));
            
            if (removed) {
                dataManager.saveList(fuelTypes, FileConstants.FUEL_TYPES_FILE);
                return true;
            }
            
        } catch (SerializationException e) {
            System.err.println("Error deleting fuel type: " + e.getMessage());
        }
        
        return false;
    }
    
    // ========== CHARGER TYPE MANAGEMENT ==========
    
    /**
     * Creates a new charger type
     * @param chargerType the charger type to create
     * @return true if created successfully
     */
    public boolean createChargerType(ChargerType chargerType) {
        if (chargerType == null) {
            return false;
        }
        
        try {
            List<ChargerType> chargerTypes = dataManager.loadList(FileConstants.CHARGER_TYPES_FILE, ChargerType::new);
            
            // Check for duplicate IDs
            boolean idExists = chargerTypes.stream()
                .anyMatch(ct -> chargerType.getId().equals(ct.getId()));
            
            if (idExists) {
                return false;
            }
            
            chargerTypes.add(chargerType);
            dataManager.saveList(chargerTypes, FileConstants.CHARGER_TYPES_FILE);
            return true;
            
        } catch (SerializationException e) {
            System.err.println("Error creating charger type: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Updates an existing charger type
     * @param chargerTypeId the ID of the charger type to update
     * @param updatedChargerType the updated charger type information
     * @return true if updated successfully
     */
    public boolean updateChargerType(String chargerTypeId, ChargerType updatedChargerType) {
        if (chargerTypeId == null || updatedChargerType == null) {
            return false;
        }
        
        try {
            List<ChargerType> chargerTypes = dataManager.loadList(FileConstants.CHARGER_TYPES_FILE, ChargerType::new);
            
            for (int i = 0; i < chargerTypes.size(); i++) {
                if (chargerTypeId.equals(chargerTypes.get(i).getId())) {
                    chargerTypes.set(i, updatedChargerType);
                    dataManager.saveList(chargerTypes, FileConstants.CHARGER_TYPES_FILE);
                    return true;
                }
            }
            
        } catch (SerializationException e) {
            System.err.println("Error updating charger type: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Deletes a charger type
     * @param chargerTypeId the ID of the charger type to delete
     * @return true if deleted successfully
     */
    public boolean deleteChargerType(String chargerTypeId) {
        if (chargerTypeId == null) {
            return false;
        }
        
        try {
            List<ChargerType> chargerTypes = dataManager.loadList(FileConstants.CHARGER_TYPES_FILE, ChargerType::new);
            boolean removed = chargerTypes.removeIf(ct -> chargerTypeId.equals(ct.getId()));
            
            if (removed) {
                dataManager.saveList(chargerTypes, FileConstants.CHARGER_TYPES_FILE);
                return true;
            }
            
        } catch (SerializationException e) {
            System.err.println("Error deleting charger type: " + e.getMessage());
        }
        
        return false;
    }
    
    // ========== STATION MANAGEMENT ==========
    
    /**
     * Adds a station to a city
     * @param cityId the city ID
     * @param station the station to add
     * @return true if added successfully
     */
    public boolean addStationToCity(String cityId, Station station) {
        if (cityId == null || station == null) {
            return false;
        }
        
        try {
            List<City> cities = dataManager.loadList(FileConstants.CITIES_FILE, City::new);
            
            for (City city : cities) {
                if (cityId.equals(city.getId())) {
                    city.addStation(station);
                    dataManager.saveList(cities, FileConstants.CITIES_FILE);
                    return true;
                }
            }
            
        } catch (SerializationException e) {
            System.err.println("Error adding station to city: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Removes a station from a city
     * @param cityId the city ID
     * @param stationId the station ID
     * @return true if removed successfully
     */
    public boolean removeStationFromCity(String cityId, String stationId) {
        if (cityId == null || stationId == null) {
            return false;
        }
        
        try {
            List<City> cities = dataManager.loadList(FileConstants.CITIES_FILE, City::new);
            
            for (City city : cities) {
                if (cityId.equals(city.getId())) {
                    boolean removed = city.getStations().removeIf(station -> stationId.equals(station.getId()));
                    if (removed) {
                        dataManager.saveList(cities, FileConstants.CITIES_FILE);
                        return true;
                    }
                }
            }
            
        } catch (SerializationException e) {
            System.err.println("Error removing station from city: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Gets all fuel types
     * @return list of all fuel types
     */
    public List<FuelType> getAllFuelTypes() {
        try {
            return dataManager.loadList(FileConstants.FUEL_TYPES_FILE, FuelType::new);
        } catch (SerializationException e) {
            System.err.println("Error loading fuel types: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Gets all charger types
     * @return list of all charger types
     */
    public List<ChargerType> getAllChargerTypes() {
        try {
            return dataManager.loadList(FileConstants.CHARGER_TYPES_FILE, ChargerType::new);
        } catch (SerializationException e) {
            System.err.println("Error loading charger types: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
