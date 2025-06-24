/*
 * Nombre del Archivo: City.java
 * 
 * Descripcion: Clase que representa una ciudad en el sistema de planificación
 *              de rutas. Implementa las interfaces Node y Serializable para
 *              integración con algoritmos de grafos y persistencia de datos.
 *              Almacena información geográfica de la ciudad y gestiona las
 *              estaciones ubicadas en ella, proporcionando funcionalidades
 *              para agregar y eliminar estaciones de forma segura.
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import Interfaces.Node;
import Interfaces.Serializable;

/**
 * Clase que representa una ciudad en el sistema
 * 
 * Esta clase implementa las interfaces Node y Serializable para proporcionar:
 * - Información geográfica de la ciudad (nombre, latitud, longitud)
 * - Gestión de estaciones ubicadas en la ciudad
 * - Integración con algoritmos de grafos como nodo
 * - Serialización completa para persistencia de datos
 * - Funcionalidades para agregar y eliminar estaciones
 * 
 * Utilizada como nodo en el grafo de ciudades para planificación de rutas.
 */
public class City implements Node, Serializable {
    private String id;
    private String name;
    private double latitude;
    private double longitude;
    private List<Station> stations;
    
    /**
     * Constructor por defecto para deserialización
     * 
     * Notas:
     * - Genera automáticamente un ID único
     * - Inicializa la lista de estaciones vacía
     * - Útil para deserialización
     */
    public City() {
        this.id = UUID.randomUUID().toString();
        this.stations = new ArrayList<>();
    }
    
    /**
     * Constructor con detalles de la ciudad
     * 
     * @param name El nombre de la ciudad
     * @param latitude La coordenada de latitud
     * @param longitude La coordenada de longitud
     * 
     * Notas:
     * - Llama al constructor por defecto para inicialización básica
     * - Establece la información geográfica proporcionada
     */
    public City(String name, double latitude, double longitude) {
        this();
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    // ========== GETTERS Y SETTERS ==========
    
    /**
     * Establece el ID único de la ciudad
     * @param id El nuevo ID de la ciudad
     */
    public void setId(String id) { this.id = id; }
    
    /**
     * Obtiene el nombre de la ciudad
     * @return El nombre de la ciudad
     */
    public String getName() { return name; }
    
    /**
     * Establece el nombre de la ciudad
     * @param name El nuevo nombre de la ciudad
     */
    public void setName(String name) { this.name = name; }
    
    /**
     * Obtiene la latitud de la ciudad
     * @return La coordenada de latitud
     */
    public double getLatitude() { return latitude; }
    
    /**
     * Establece la latitud de la ciudad
     * @param latitude La nueva coordenada de latitud
     */
    public void setLatitude(double latitude) { this.latitude = latitude; }
    
    /**
     * Obtiene la longitud de la ciudad
     * @return La coordenada de longitud
     */
    public double getLongitude() { return longitude; }
    
    /**
     * Establece la longitud de la ciudad
     * @param longitude La nueva coordenada de longitud
     */
    public void setLongitude(double longitude) { this.longitude = longitude; }
    
    /**
     * Obtiene la lista de estaciones de la ciudad
     * @return Lista de estaciones en la ciudad
     */
    public List<Station> getStations() { return stations; }
    
    /**
     * Establece la lista de estaciones de la ciudad
     * @param stations Nueva lista de estaciones
     */
    public void setStations(List<Station> stations) { this.stations = stations; }
    
    // ========== IMPLEMENTACIÓN DE INTERFAZ NODE ==========
    
    /**
     * Obtiene el ID único de la ciudad
     * 
     * @return El ID único de la ciudad
     * 
     * Notas:
     * - Implementación del método de la interfaz Node
     * - Utilizado por algoritmos de grafos para identificación
     */
    @Override
    public String getId() { return id; }
    
    // ========== GESTIÓN DE ESTACIONES ==========
    
    /**
     * Agrega una estación a esta ciudad
     * 
     * @param station La estación a agregar
     * 
     * Notas:
     * - Verifica que la estación no esté ya en la lista
     * - Establece automáticamente la referencia de la ciudad en la estación
     * - Evita duplicados automáticamente
     */
    public void addStation(Station station) {
        if (!stations.contains(station)) {
            stations.add(station);
            station.setCity(this);
        }
    }
    
    /**
     * Elimina una estación de esta ciudad
     * 
     * @param station La estación a eliminar
     * @return true si se eliminó exitosamente, false si no estaba en la lista
     * 
     * Notas:
     * - Elimina la estación de la lista de la ciudad
     * - Quita la referencia de la ciudad en la estación
     * - Mantiene la integridad de las referencias
     */
    public boolean removeStation(Station station) {
        if (stations.remove(station)) {
            station.setCity(null);
            return true;
        }
        return false;
    }
    
    // ========== SERIALIZACIÓN ==========
    
    /**
     * Serializa la ciudad completa a un stream de datos
     * 
     * @param out Stream de salida para escribir los datos serializados
     * @throws IOException Si ocurre un error durante la escritura
     * 
     * Datos serializados:
     * - ID, nombre, latitud y longitud de la ciudad
     * - Número de estaciones
     * - Información completa de cada estación
     */
    @Override
    public void serialize(DataOutputStream out) throws IOException {
        out.writeUTF(id != null ? id : "");
        out.writeUTF(name != null ? name : "");
        out.writeDouble(latitude);
        out.writeDouble(longitude);
        
        out.writeInt(stations.size());
        for (Station station : stations) {
            station.serialize(out);
        }
    }
    
    /**
     * Deserializa la ciudad desde un stream de datos
     * 
     * @param in Stream de entrada para leer los datos serializados
     * @throws IOException Si ocurre un error durante la lectura
     * 
     * Proceso:
     * - Lee la información básica de la ciudad (ID, nombre, coordenadas)
     * - Lee el número de estaciones
     * - Deserializa cada estación y establece la referencia de la ciudad
     * - Reconstruye la lista completa de estaciones
     */
    @Override
    public void deserialize(DataInputStream in) throws IOException {
        this.id = in.readUTF();
        this.name = in.readUTF();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        
        int stationCount = in.readInt();
        this.stations = new ArrayList<>();
        for (int i = 0; i < stationCount; i++) {
            Station station = Station.deserializeFromStream(in);
            station.setCity(this);
            stations.add(station);
        }
    }
    
    // ========== MÉTODOS DE OBJETO ==========
    
    /**
     * Genera una representación en cadena de la ciudad
     * 
     * @return El nombre de la ciudad
     * 
     * Notas:
     * - Utiliza solo el nombre para representación simple
     * - Útil para interfaces de usuario y debugging
     */
    @Override
    public String toString() {
        return name;
    }
    
    /**
     * Compara esta ciudad con otro objeto
     * 
     * @param obj El objeto a comparar
     * @return true si son iguales, false en caso contrario
     * 
     * Criterio de igualdad:
     * - Compara por ID único de la ciudad
     * - Maneja casos de null de forma segura
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        City city = (City) obj;
        return id != null ? id.equals(city.id) : city.id == null;
    }
    
    /**
     * Genera el código hash de la ciudad
     * 
     * @return El código hash basado en el ID de la ciudad
     * 
     * Notas:
     * - Basado en el ID único de la ciudad
     * - Maneja casos de ID null
     */
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}