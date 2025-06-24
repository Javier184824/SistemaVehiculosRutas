/*
 * Nombre del Archivo: Route.java
 * 
 * Descripcion: Clase que representa una ruta completa entre ciudades en el sistema
 *              de planificación de rutas. Almacena información sobre las ciudades
 *              que componen la ruta, las conexiones entre ellas, y calcula
 *              automáticamente distancias totales, tiempos y costos. Proporciona
 *              funcionalidades para encontrar estaciones compatibles con vehículos
 *              específicos a lo largo de la ruta.
 * 
 * Nombre de los Integrantes:
 * Javier Lee Liang
 * Paulo César Herrera Arias
 * José Emilio Alvarado Mendez
 * Josué Santiago Hidalgo Sandoval
 */
package Models;

import java.util.ArrayList;
import java.util.List;

import Vehicle.Vehicle;

/**
 * Clase que representa una ruta completa entre ciudades
 * 
 * Esta clase encapsula toda la información relacionada con una ruta:
 * - Lista de ciudades que componen la ruta
 * - Conexiones entre ciudades consecutivas
 * - Cálculos automáticos de distancia, tiempo y costo total
 * - Funcionalidades para análisis de estaciones compatibles
 * - Formateo de información para presentación al usuario
 * 
 * Utilizada para representar rutas completas calculadas por el sistema.
 */
public class Route {
    
    private List<City> cities;
    private List<Connection> connections;
    private double totalDistance;
    private int totalTimeMinutes;
    private double totalCost;
    
    /**
     * Constructor por defecto de la ruta
     * 
     * Notas:
     * - Inicializa listas vacías de ciudades y conexiones
     * - Los totales se calculan automáticamente cuando se agregan datos
     */
    public Route() {
        this.cities = new ArrayList<>();
        this.connections = new ArrayList<>();
    }
    
    /**
     * Constructor completo de la ruta
     * 
     * @param cities Lista de ciudades que componen la ruta
     * @param connections Lista de conexiones entre ciudades consecutivas
     * 
     * Notas:
     * - Crea copias de las listas para evitar modificaciones externas
     * - Calcula automáticamente los totales de distancia, tiempo y costo
     */
    public Route(List<City> cities, List<Connection> connections) {
        this.cities = new ArrayList<>(cities);
        this.connections = new ArrayList<>(connections);
        calculateTotals();
    }
    
    /**
     * Obtiene la lista de ciudades de la ruta
     * @return Lista de ciudades que componen la ruta
     */
    public List<City> getCities() { return cities; }
    
    /**
     * Establece la lista de ciudades de la ruta
     * @param cities Nueva lista de ciudades
     * 
     * Notas:
     * - Crea una copia de la lista para evitar modificaciones externas
     * - Recalcula automáticamente los totales
     */
    public void setCities(List<City> cities) { 
        this.cities = new ArrayList<>(cities); 
        calculateTotals();
    }
    
    /**
     * Obtiene la lista de conexiones de la ruta
     * @return Lista de conexiones entre ciudades consecutivas
     */
    public List<Connection> getConnections() { return connections; }
    
    /**
     * Establece la lista de conexiones de la ruta
     * @param connections Nueva lista de conexiones
     * 
     * Notas:
     * - Crea una copia de la lista para evitar modificaciones externas
     * - Recalcula automáticamente los totales
     */
    public void setConnections(List<Connection> connections) { 
        this.connections = new ArrayList<>(connections); 
        calculateTotals();
    }
    
    /**
     * Obtiene la distancia total de la ruta
     * @return Distancia total en kilómetros
     */
    public double getTotalDistance() { return totalDistance; }
    
    /**
     * Obtiene el tiempo total de la ruta
     * @return Tiempo total en minutos
     */
    public int getTotalTimeMinutes() { return totalTimeMinutes; }
    
    /**
     * Obtiene el costo total de la ruta
     * @return Costo total en moneda local
     */
    public double getTotalCost() { return totalCost; }
    
    /**
     * Obtiene la ciudad de origen de la ruta
     * 
     * @return La primera ciudad de la ruta, o null si la ruta está vacía
     */
    public City getOrigin() {
        return cities.isEmpty() ? null : cities.get(0);
    }
    
    /**
     * Obtiene la ciudad de destino de la ruta
     * 
     * @return La última ciudad de la ruta, o null si la ruta está vacía
     */
    public City getDestination() {
        return cities.isEmpty() ? null : cities.get(cities.size() - 1);
    }
    
    /**
     * Calcula la distancia, tiempo y costo totales de la ruta
     * 
     * Proceso:
     * - Reinicia los totales a cero
     * - Suma los valores de cada conexión en la ruta
     * - Actualiza los campos totalDistance, totalTimeMinutes y totalCost
     * 
     * Notas:
     * - Llamado automáticamente cuando se modifican las conexiones
     * - Asegura que los totales estén siempre actualizados
     */
    private void calculateTotals() {
        totalDistance = 0;
        totalTimeMinutes = 0;
        totalCost = 0;
        
        for (Connection connection : connections) {
            totalDistance += connection.getDistance();
            totalTimeMinutes += connection.getTimeMinutes();
            totalCost += connection.getCost();
        }
    }
    
    /**
     * Obtiene todas las estaciones compatibles a lo largo de la ruta para un vehículo específico
     * 
     * @param vehicle El vehículo para verificar compatibilidad
     * @return Lista de estaciones compatibles con el vehículo
     * 
     * Proceso:
     * - Recorre todas las ciudades de la ruta
     * - Para cada ciudad, verifica todas sus estaciones
     * - Filtra estaciones compatibles con el vehículo especificado
     * 
     * Notas:
     * - Utiliza el método isCompatibleWith del vehículo
     * - Útil para planificación de paradas de recarga/combustible
     */
    public List<Station> getCompatibleStations(Vehicle vehicle) {
        List<Station> compatibleStations = new ArrayList<>();
        
        for (City city : cities) {
            for (Station station : city.getStations()) {
                if (vehicle.isCompatibleWith(station)) {
                    compatibleStations.add(station);
                }
            }
        }
        
        return compatibleStations;
    }
    
    /**
     * Formatea el tiempo total como horas:minutos
     * 
     * @return Cadena formateada del tiempo total (ej: "2:30" para 2 horas 30 minutos)
     * 
     * Notas:
     * - Convierte minutos totales a formato horas:minutos
     * - Útil para presentación al usuario
     */
    public String getFormattedTotalTime() {
        int hours = totalTimeMinutes / 60;
        int minutes = totalTimeMinutes % 60;
        return String.format("%d:%02d", hours, minutes);
    }
    
    /**
     * Genera una representación en cadena de la ruta
     * 
     * @return Cadena con información completa de la ruta
     * 
     * Formato:
     * - Origen -> Destino
     * - Distancia, tiempo y costo entre paréntesis
     * - Ciudades intermedias precedidas por "via" si hay más de 2 ciudades
     * 
     * Ejemplo: "San José -> Cartago (25.5km, 0:45, $12.50) via Alajuela"
     */
    @Override
    public String toString() {
        if (cities.isEmpty()) {
            return "Empty route";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append(getOrigin().getName()).append(" -> ").append(getDestination().getName());
        sb.append(" (").append(String.format("%.1f", totalDistance)).append("km, ");
        sb.append(getFormattedTotalTime()).append(", $").append(String.format("%.2f", totalCost)).append(")");
        
        if (cities.size() > 2) {
            sb.append(" via ");
            for (int i = 1; i < cities.size() - 1; i++) {
                if (i > 1) sb.append(", ");
                sb.append(cities.get(i).getName());
            }
        }
        
        return sb.toString();
    }
}
