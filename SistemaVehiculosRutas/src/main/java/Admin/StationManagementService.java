/*
 * Nombre del Archivo: StationManagementService.java
 * 
 * Descripcion: Servicio de gestión de estaciones y tipos de energía del sistema.
 *              Proporciona funcionalidades para administrar tipos de combustible,
 *              tipos de cargadores eléctricos y estaciones asociadas a ciudades.
 *              Incluye operaciones CRUD completas para todos los tipos de energía.
 * 
 * Nombre de los Integrantes:
 * Javier Lee Liang
 * Paulo César Herrera Arias
 * José Emilio Alvarado Mendez
 * Josué Santiago Hidalgo Sandoval
 */
package Admin;

import java.util.ArrayList;
import java.util.List;

import Binary.FileConstants;
import Interfaces.DataManager;
import Interfaces.SerializationException;
import Models.City;
import Models.Station;
import Vehicle.ChargerType;
import Vehicle.FuelType;

/**
 * Servicio de gestión de estaciones y tipos de energía
 * 
 * Esta clase proporciona funcionalidades completas para la administración de:
 * - Tipos de combustible (gasolina, diesel, etc.)
 * - Tipos de cargadores eléctricos (rápido, lento, etc.)
 * - Estaciones asociadas a ciudades específicas
 * 
 * Todas las operaciones incluyen validación de datos y manejo de errores.
 */
public class StationManagementService {
    
    private final DataManager dataManager;
    
    /**
     * Constructor del servicio de gestión de estaciones
     * 
     * @param dataManager Gestor de datos para operaciones de persistencia
     */
    public StationManagementService(DataManager dataManager) {
        this.dataManager = dataManager;
    }
    
    // ========== GESTIÓN DE TIPOS DE COMBUSTIBLE ==========
    
    /**
     * Crea un nuevo tipo de combustible en el sistema
     * 
     * @param fuelType El tipo de combustible a crear
     * @return true si se creó exitosamente, false en caso contrario
     * 
     * Restricciones:
     * - El tipo de combustible no puede ser null
     * - No se permiten IDs duplicados
     * - Se valida la integridad de los datos antes de guardar
     */
    public boolean createFuelType(FuelType fuelType) {
        if (fuelType == null) {
            return false;
        }
        
        try {
            List<FuelType> fuelTypes = dataManager.loadList(FileConstants.FUEL_TYPES_FILE, FuelType::new);
            
            // Verificar IDs duplicados
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
     * Actualiza un tipo de combustible existente
     * 
     * @param fuelTypeId El ID del tipo de combustible a actualizar
     * @param updatedFuelType La información actualizada del tipo de combustible
     * @return true si se actualizó exitosamente, false en caso contrario
     * 
     * Notas:
     * - Busca el tipo de combustible por ID
     * - Reemplaza completamente la información existente
     * - Mantiene la integridad de los datos
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
     * Elimina un tipo de combustible del sistema
     * 
     * @param fuelTypeId El ID del tipo de combustible a eliminar
     * @return true si se eliminó exitosamente, false en caso contrario
     * 
     * Notas:
     * - Busca y elimina el tipo de combustible por ID
     * - Actualiza la persistencia de datos
     * - Maneja errores de serialización
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
    
    // ========== GESTIÓN DE TIPOS DE CARGADORES ==========
    
    /**
     * Crea un nuevo tipo de cargador eléctrico en el sistema
     * 
     * @param chargerType El tipo de cargador a crear
     * @return true si se creó exitosamente, false en caso contrario
     * 
     * Restricciones:
     * - El tipo de cargador no puede ser null
     * - No se permiten IDs duplicados
     * - Se valida la integridad de los datos antes de guardar
     */
    public boolean createChargerType(ChargerType chargerType) {
        if (chargerType == null) {
            return false;
        }
        
        try {
            List<ChargerType> chargerTypes = dataManager.loadList(FileConstants.CHARGER_TYPES_FILE, ChargerType::new);
            
            // Verificar IDs duplicados
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
     * Actualiza un tipo de cargador eléctrico existente
     * 
     * @param chargerTypeId El ID del tipo de cargador a actualizar
     * @param updatedChargerType La información actualizada del tipo de cargador
     * @return true si se actualizó exitosamente, false en caso contrario
     * 
     * Notas:
     * - Busca el tipo de cargador por ID
     * - Reemplaza completamente la información existente
     * - Mantiene la integridad de los datos
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
     * Elimina un tipo de cargador eléctrico del sistema
     * 
     * @param chargerTypeId El ID del tipo de cargador a eliminar
     * @return true si se eliminó exitosamente, false en caso contrario
     * 
     * Notas:
     * - Busca y elimina el tipo de cargador por ID
     * - Actualiza la persistencia de datos
     * - Maneja errores de serialización
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
    
    // ========== GESTIÓN DE ESTACIONES ==========
    
    /**
     * Agrega una estación a una ciudad específica
     * 
     * @param cityId El ID de la ciudad donde agregar la estación
     * @param station La estación a agregar
     * @return true si se agregó exitosamente, false en caso contrario
     * 
     * Notas:
     * - Busca la ciudad por ID
     * - Agrega la estación a la lista de estaciones de la ciudad
     * - Actualiza la persistencia de datos
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
     * Elimina una estación de una ciudad específica
     * 
     * @param cityId El ID de la ciudad de donde eliminar la estación
     * @param stationId El ID de la estación a eliminar
     * @return true si se eliminó exitosamente, false en caso contrario
     * 
     * Notas:
     * - Busca la ciudad por ID
     * - Elimina la estación de la lista de estaciones de la ciudad
     * - Actualiza la persistencia de datos
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
     * Obtiene todos los tipos de combustible del sistema
     * 
     * @return Lista de todos los tipos de combustible disponibles
     * 
     * Notas:
     * - Retorna una lista vacía si hay errores de carga
     * - Maneja errores de serialización
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
     * Obtiene todos los tipos de cargadores eléctricos del sistema
     * 
     * @return Lista de todos los tipos de cargadores disponibles
     * 
     * Notas:
     * - Retorna una lista vacía si hay errores de carga
     * - Maneja errores de serialización
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
