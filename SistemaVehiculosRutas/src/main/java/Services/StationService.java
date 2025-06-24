/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Services;

import Binary.FileConstants;
import Interfaces.DataManager;
import Interfaces.SerializationException;
import Models.Station;
import Vehicle.ChargerType;
import Vehicle.FuelType;
import Vehicle.Vehicle;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author JE
 */
public class StationService {
    
    private final DataManager dataManager;
    
    public StationService(DataManager dataManager) {
        this.dataManager = dataManager;
    }
    
    /**
     * Gets all available fuel types
     * @return list of fuel types
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
     * Gets all available charger types
     * @return list of charger types
     */
    public List<ChargerType> getAllChargerTypes() {
        try {
            return dataManager.loadList(FileConstants.CHARGER_TYPES_FILE, ChargerType::new);
        } catch (SerializationException e) {
            System.err.println("Error loading charger types: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Finds fuel type by ID
     * @param fuelTypeId the fuel type ID
     * @return the fuel type, or null if not found
     */
    public FuelType findFuelTypeById(String fuelTypeId) {
        return getAllFuelTypes().stream()
            .filter(ft -> fuelTypeId.equals(ft.getId()))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Finds charger type by ID
     * @param chargerTypeId the charger type ID
     * @return the charger type, or null if not found
     */
    public ChargerType findChargerTypeById(String chargerTypeId) {
        return getAllChargerTypes().stream()
            .filter(ct -> chargerTypeId.equals(ct.getId()))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Filters stations by vehicle compatibility
     * @param stations the list of stations to filter
     * @param vehicle the vehicle to check compatibility for
     * @return list of compatible stations
     */
    public List<Station> filterCompatibleStations(List<Station> stations, Vehicle vehicle) {
        if (stations == null || vehicle == null) {
            return new ArrayList<>();
        }
        
        return stations.stream()
            .filter(vehicle::isCompatibleWith)
            .collect(Collectors.toList());
    }
    
    /**
     * Gets station information summary
     * @param station the station
     * @return formatted station information
     */
    public String getStationInfo(Station station) {
        if (station == null) {
            return "No station information";
        }
        
        StringBuilder info = new StringBuilder();
        info.append("Station: ").append(station.getName()).append("\n");
        info.append("Type: ").append(station.getStationType()).append("\n");
        info.append("Address: ").append(station.getAddress()).append("\n");
        
        if (station.getCity() != null) {
            info.append("City: ").append(station.getCity().getName()).append("\n");
        }
        
        return info.toString();
    }
}
