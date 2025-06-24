/*
 * Nombre del Archivo: StationService.java
 * 
 * Descripcion: Servicio de gestión de estaciones y tipos de energía del sistema.
 *              Proporciona funcionalidades para acceder a tipos de combustible
 *              y cargadores eléctricos, buscar estaciones compatibles con
 *              vehículos específicos, y generar información detallada de
 *              estaciones. Facilita la gestión de infraestructura de energía
 *              para vehículos.
 * 
 * Nombre de los Integrantes:
 * Javier Lee Liang
 * Paulo César Herrera Arias
 * José Emilio Alvarado Mendez
 * Josué Santiago Hidalgo Sandoval
 */
package Services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import Binary.FileConstants;
import Interfaces.DataManager;
import Interfaces.SerializationException;
import Models.Station;
import Vehicle.ChargerType;
import Vehicle.FuelType;
import Vehicle.Vehicle;

/**
 * Servicio de gestión de estaciones y tipos de energía
 * 
 * Esta clase proporciona funcionalidades para la gestión de estaciones:
 * - Acceso a tipos de combustible y cargadores eléctricos
 * - Búsqueda de tipos de energía por ID
 * - Filtrado de estaciones por compatibilidad con vehículos
 * - Generación de información detallada de estaciones
 * 
 * Actúa como intermediario entre el gestor de datos y las funcionalidades
 * relacionadas con estaciones y energía.
 */
public class StationService {
    
    private final DataManager dataManager;
    
    /**
     * Constructor del servicio de estaciones
     * 
     * @param dataManager Gestor de datos para operaciones de persistencia
     * 
     * Notas:
     * - Establece la dependencia del gestor de datos
     * - Permite acceso a archivos de tipos de energía
     */
    public StationService(DataManager dataManager) {
        this.dataManager = dataManager;
    }
    
    /**
     * Obtiene todos los tipos de combustible disponibles
     * 
     * @return Lista de todos los tipos de combustible del sistema
     * 
     * Notas:
     * - Carga desde el archivo de tipos de combustible
     * - Retorna lista vacía si hay errores de carga
     * - Maneja errores de serialización automáticamente
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
     * Obtiene todos los tipos de cargadores eléctricos disponibles
     * 
     * @return Lista de todos los tipos de cargadores del sistema
     * 
     * Notas:
     * - Carga desde el archivo de tipos de cargadores
     * - Retorna lista vacía si hay errores de carga
     * - Maneja errores de serialización automáticamente
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
     * Busca un tipo de combustible por su ID
     * 
     * @param fuelTypeId El ID del tipo de combustible a buscar
     * @return El tipo de combustible encontrado, o null si no existe
     * 
     * Notas:
     * - Utiliza stream para búsqueda eficiente
     * - Comparación exacta por ID
     * - Retorna null si no se encuentra el tipo
     */
    public FuelType findFuelTypeById(String fuelTypeId) {
        return getAllFuelTypes().stream()
            .filter(ft -> fuelTypeId.equals(ft.getId()))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Busca un tipo de cargador eléctrico por su ID
     * 
     * @param chargerTypeId El ID del tipo de cargador a buscar
     * @return El tipo de cargador encontrado, o null si no existe
     * 
     * Notas:
     * - Utiliza stream para búsqueda eficiente
     * - Comparación exacta por ID
     * - Retorna null si no se encuentra el tipo
     */
    public ChargerType findChargerTypeById(String chargerTypeId) {
        return getAllChargerTypes().stream()
            .filter(ct -> chargerTypeId.equals(ct.getId()))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Filtra estaciones por compatibilidad con un vehículo específico
     * 
     * @param stations La lista de estaciones a filtrar
     * @param vehicle El vehículo para verificar compatibilidad
     * @return Lista de estaciones compatibles con el vehículo
     * 
     * Notas:
     * - Retorna lista vacía si stations o vehicle son null
     * - Utiliza el método isCompatibleWith del vehículo
     * - Aplica filtrado mediante stream para eficiencia
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
     * Genera información detallada de una estación
     * 
     * @param station La estación para obtener información
     * @return Información formateada de la estación
     * 
     * Información incluida:
     * - Nombre de la estación
     * - Tipo de estación (combustible/eléctrica)
     * - Dirección de la estación
     * - Ciudad donde se encuentra
     * 
     * Notas:
     * - Retorna mensaje informativo si la estación es null
     * - Incluye información de la ciudad si está disponible
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
