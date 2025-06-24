/*
 * Nombre del Archivo: Connection.java
 * 
 * Descripcion: Clase que representa una conexión entre dos ciudades en el sistema
 *              de planificación de rutas. Implementa la interfaz Edge para
 *              integración con algoritmos de grafos y proporciona información
 *              sobre distancia, tiempo de viaje y costo entre ciudades.
 *              Incluye funcionalidades de serialización para persistencia
 *              y resolución de referencias de ciudades.
 * 
 * Nombre de los Integrantes:
 * Javier Lee Liang
 * Paulo César Herrera Arias
 * José Emilio Alvarado Mendez
 * Josué Santiago Hidalgo Sandoval
 */
package Models;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import Interfaces.Edge;
import Interfaces.Node;
import Interfaces.Serializable;

/**
 * Clase que representa una conexión entre dos ciudades
 * 
 * Esta clase implementa la interfaz Edge para integración con algoritmos de grafos:
 * - Representa una conexión directa entre dos ciudades
 * - Almacena información de distancia, tiempo y costo
 * - Proporciona funcionalidades de serialización para persistencia
 * - Maneja resolución de referencias de ciudades
 * - Permite creación de conexiones reversas
 * 
 * Utilizada para construir el grafo de ciudades y calcular rutas óptimas.
 */
public class Connection implements Edge, Serializable{
    
    private City fromCity;
    private City toCity;
    private double distance; // in kilometers
    private int timeMinutes;
    private double cost; // in local currency
    
    // Campos para almacenar IDs de ciudades (no transitorios para persistencia)
    private String fromCityId;
    private String toCityId;
    
    /**
     * Constructor por defecto de la conexión
     * 
     * Notas:
     * - Inicializa todos los campos con valores por defecto
     * - Útil para deserialización
     */
    public Connection() {}
    
    /**
     * Constructor completo de la conexión
     * 
     * @param fromCity Ciudad de origen de la conexión
     * @param toCity Ciudad de destino de la conexión
     * @param distance Distancia en kilómetros
     * @param timeMinutes Tiempo de viaje en minutos
     * @param cost Costo del viaje en moneda local
     * 
     * Notas:
     * - Establece automáticamente los IDs de las ciudades para persistencia
     * - Permite crear conexiones con información completa
     */
    public Connection(City fromCity, City toCity, double distance, int timeMinutes, double cost) {
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.distance = distance;
        this.timeMinutes = timeMinutes;
        this.cost = cost;
        // Establecer IDs de ciudades para persistencia
        this.fromCityId = fromCity != null ? fromCity.getId() : null;
        this.toCityId = toCity != null ? toCity.getId() : null;
    }
    
    // ========== IMPLEMENTACIÓN DE INTERFAZ EDGE ==========
    
    /**
     * Obtiene el nodo de origen de la conexión
     * 
     * @return La ciudad de origen como nodo
     */
    @Override
    public Node getFrom() { return fromCity; }
    
    /**
     * Obtiene el nodo de destino de la conexión
     * 
     * @return La ciudad de destino como nodo
     */
    @Override
    public Node getTo() { return toCity; }
    
    /**
     * Obtiene el peso de la conexión para algoritmos de búsqueda de rutas
     * 
     * @return El tiempo en minutos como peso principal
     * 
     * Notas:
     * - Utiliza el tiempo como peso principal para búsqueda de rutas
     * - Permite optimización por tiempo de viaje
     */
    @Override
    public double getWeight() { 
        // Para búsqueda de rutas, usar tiempo como peso principal
        return timeMinutes; 
    }
    
    /**
     * Obtiene la distancia de la conexión
     * 
     * @return La distancia en kilómetros
     */
    @Override
    public double getDistance() { return distance; }
    
    /**
     * Obtiene el tiempo de viaje de la conexión
     * 
     * @return El tiempo en minutos
     */
    @Override
    public int getTimeMinutes() { return timeMinutes; }
    
    /**
     * Obtiene el costo de la conexión
     * 
     * @return El costo en moneda local
     */
    @Override
    public double getCost() { return cost; }
    
    // ========== GETTERS Y SETTERS ==========
    
    /**
     * Obtiene la ciudad de origen
     * @return La ciudad de origen
     */
    public City getFromCity() { return fromCity; }
    
    /**
     * Establece la ciudad de origen
     * @param fromCity La nueva ciudad de origen
     * 
     * Notas:
     * - Actualiza automáticamente el ID de la ciudad para persistencia
     */
    public void setFromCity(City fromCity) { 
        this.fromCity = fromCity; 
        this.fromCityId = fromCity != null ? fromCity.getId() : null;
    }
    
    /**
     * Obtiene la ciudad de destino
     * @return La ciudad de destino
     */
    public City getToCity() { return toCity; }
    
    /**
     * Establece la ciudad de destino
     * @param toCity La nueva ciudad de destino
     * 
     * Notas:
     * - Actualiza automáticamente el ID de la ciudad para persistencia
     */
    public void setToCity(City toCity) { 
        this.toCity = toCity; 
        this.toCityId = toCity != null ? toCity.getId() : null;
    }
    
    /**
     * Establece la distancia de la conexión
     * @param distance La nueva distancia en kilómetros
     */
    public void setDistance(double distance) { this.distance = distance; }
    
    /**
     * Establece el tiempo de viaje de la conexión
     * @param timeMinutes El nuevo tiempo en minutos
     */
    public void setTimeMinutes(int timeMinutes) { this.timeMinutes = timeMinutes; }
    
    /**
     * Establece el costo de la conexión
     * @param cost El nuevo costo en moneda local
     */
    public void setCost(double cost) { this.cost = cost; }
    
    /**
     * Obtiene el ID de la ciudad de origen
     * @return El ID de la ciudad de origen
     */
    public String getFromCityId() { return fromCityId; }
    
    /**
     * Obtiene el ID de la ciudad de destino
     * @return El ID de la ciudad de destino
     */
    public String getToCityId() { return toCityId; }
    
    /**
     * Crea una conexión reversa (destino -> origen)
     * 
     * @return Una nueva conexión con las ciudades invertidas
     * 
     * Notas:
     * - Mantiene la misma distancia, tiempo y costo
     * - Útil para crear conexiones bidireccionales
     * - Preserva la información de la conexión original
     */
    public Connection reverse() {
        return new Connection(toCity, fromCity, distance, timeMinutes, cost);
    }
    
    /**
     * Serializa la conexión a un stream de datos
     * 
     * @param out Stream de salida para escribir los datos serializados
     * @throws IOException Si ocurre un error durante la escritura
     * 
     * Datos serializados:
     * - IDs de ciudades de origen y destino
     * - Distancia en kilómetros
     * - Tiempo en minutos
     * - Costo en moneda local
     */
    @Override
    public void serialize(DataOutputStream out) throws IOException {
        // Serializar IDs de ciudades para persistencia
        out.writeUTF(fromCityId != null ? fromCityId : "");
        out.writeUTF(toCityId != null ? toCityId : "");
        out.writeDouble(distance);
        out.writeInt(timeMinutes);
        out.writeDouble(cost);
    }
    
    /**
     * Deserializa la conexión desde un stream de datos
     * 
     * @param in Stream de entrada para leer los datos serializados
     * @throws IOException Si ocurre un error durante la lectura
     * 
     * Proceso:
     * - Lee los IDs de ciudades desde los datos serializados
     * - Restaura distancia, tiempo y costo
     * - Almacena IDs para resolución posterior
     * - Las referencias de ciudades quedan null hasta ser resueltas
     */
    @Override
    public void deserialize(DataInputStream in) throws IOException {
        // Leer IDs de ciudades desde datos serializados
        String fromCityId = in.readUTF();
        String toCityId = in.readUTF();
        this.distance = in.readDouble();
        this.timeMinutes = in.readInt();
        this.cost = in.readDouble();
        
        // Almacenar IDs de ciudades para resolución posterior
        this.fromCityId = fromCityId.isEmpty() ? null : fromCityId;
        this.toCityId = toCityId.isEmpty() ? null : toCityId;
        
        // Las referencias de ciudades serán null hasta ser resueltas
        this.fromCity = null;
        this.toCity = null;
    }
    
    /**
     * Resuelve las referencias de ciudades después de la deserialización
     * 
     * @param cityResolver Función para resolver ciudades por ID
     * 
     * Notas:
     * - Utiliza la función proporcionada para buscar ciudades por ID
     * - Restaura las referencias de objetos de ciudades
     * - Útil después de cargar conexiones desde archivos
     */
    public void resolveCityReferences(java.util.function.Function<String, City> cityResolver) {
        if (fromCityId != null) {
            this.fromCity = cityResolver.apply(fromCityId);
        }
        if (toCityId != null) {
            this.toCity = cityResolver.apply(toCityId);
        }
    }
    
    /**
     * Genera una representación en cadena de la conexión
     * 
     * @return Cadena con información de la conexión
     * 
     * Formato:
     * - Nombres de ciudades de origen y destino
     * - Distancia, tiempo y costo entre paréntesis
     * - Maneja casos donde las ciudades no están resueltas
     */
    @Override
    public String toString() {
        String fromName;
        if (fromCity != null) {
            fromName = fromCity.getName();
        } else if (fromCityId != null) {
            fromName = fromCityId + " (ID)";
        } else {
            fromName = "Unknown";
        }
        
        String toName;
        if (toCity != null) {
            toName = toCity.getName();
        } else if (toCityId != null) {
            toName = toCityId + " (ID)";
        } else {
            toName = "Unknown";
        }
        
        return fromName + " -> " + toName + 
               " (" + distance + "km, " + timeMinutes + "min, $" + cost + ")";
    }
    
    /**
     * Compara esta conexión con otro objeto
     * 
     * @param obj El objeto a comparar
     * @return true si son iguales, false en caso contrario
     * 
     * Criterio de igualdad:
     * - Compara por ciudades de origen y destino
     * - Maneja casos de null de forma segura
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Connection that = (Connection) obj;
        return fromCity != null ? fromCity.equals(that.fromCity) : that.fromCity == null &&
               toCity != null ? toCity.equals(that.toCity) : that.toCity == null;
    }
    
    /**
     * Genera el código hash de la conexión
     * 
     * @return El código hash basado en las ciudades de origen y destino
     * 
     * Notas:
     * - Basado en las ciudades de origen y destino
     * - Maneja casos de ciudades null
     */
    @Override
    public int hashCode() {
        int result = fromCity != null ? fromCity.hashCode() : 0;
        result = 31 * result + (toCity != null ? toCity.hashCode() : 0);
        return result;
    }
}
