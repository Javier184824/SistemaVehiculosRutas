/*
 * Nombre del Archivo: Station.java
 * 
 * Descripcion: Clase abstracta que representa una estación en el sistema de
 *              planificación de rutas. Proporciona la estructura base para
 *              diferentes tipos de estaciones (combustible, eléctricas) con
 *              información común como ID, nombre, dirección y ciudad. Implementa
 *              serialización polimórfica y métodos de fábrica para deserialización
 *              de diferentes tipos de estaciones.
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
import java.util.UUID;

import Interfaces.Serializable;

/**
 * Clase abstracta que representa una estación en el sistema
 * 
 * Esta clase proporciona la estructura base para todos los tipos de estaciones:
 * - Información básica: ID, nombre, dirección y ciudad
 * - Serialización polimórfica para diferentes tipos de estaciones
 * - Método de fábrica para deserialización automática
 * - Métodos abstractos para funcionalidades específicas por tipo
 * 
 * Las clases hijas (FuelStation, ChargingStation) implementan funcionalidades
 * específicas para cada tipo de estación.
 */
public abstract class Station implements Serializable {
    protected String id;
    protected String name;
    protected String address;
    protected City city;
    
    /**
     * Constructor por defecto de la estación
     * 
     * Notas:
     * - Genera automáticamente un ID único
     * - Inicializa otros campos con valores por defecto
     * - Útil para deserialización
     */
    public Station() {
        this.id = UUID.randomUUID().toString();
    }
    
    /**
     * Constructor con información básica de la estación
     * 
     * @param name El nombre de la estación
     * @param address La dirección de la estación
     * 
     * Notas:
     * - Llama al constructor por defecto para generar ID
     * - Establece nombre y dirección proporcionados
     */
    public Station(String name, String address) {
        this();
        this.name = name;
        this.address = address;
    }
    
    // ========== GETTERS Y SETTERS ==========
    
    /**
     * Obtiene el ID único de la estación
     * @return El ID único de la estación
     */
    public String getId() { return id; }
    
    /**
     * Establece el ID único de la estación
     * @param id El nuevo ID de la estación
     */
    public void setId(String id) { this.id = id; }
    
    /**
     * Obtiene el nombre de la estación
     * @return El nombre de la estación
     */
    public String getName() { return name; }
    
    /**
     * Establece el nombre de la estación
     * @param name El nuevo nombre de la estación
     */
    public void setName(String name) { this.name = name; }
    
    /**
     * Obtiene la dirección de la estación
     * @return La dirección de la estación
     */
    public String getAddress() { return address; }
    
    /**
     * Establece la dirección de la estación
     * @param address La nueva dirección de la estación
     */
    public void setAddress(String address) { this.address = address; }
    
    /**
     * Obtiene la ciudad donde se encuentra la estación
     * @return La ciudad de la estación
     */
    public City getCity() { return city; }
    
    /**
     * Establece la ciudad donde se encuentra la estación
     * @param city La nueva ciudad de la estación
     */
    public void setCity(City city) { this.city = city; }
    
    // ========== MÉTODOS ABSTRACTOS ==========
    
    /**
     * Obtiene el tipo de estación
     * 
     * @return Cadena que identifica el tipo de estación
     * 
     * Notas:
     * - Debe ser implementado por las clases hijas
     * - Proporciona identificación específica del tipo de estación
     * - Útil para diferenciar entre estaciones de combustible y eléctricas
     */
    public abstract String getStationType();
    
    /**
     * Serializa campos específicos de la estación
     * 
     * @param out Stream de salida para escribir los datos serializados
     * @throws IOException Si ocurre un error durante la escritura
     * 
     * Notas:
     * - Debe ser implementado por las clases hijas
     * - Serializa información específica del tipo de estación
     * - Llamado automáticamente por el método serialize principal
     */
    protected abstract void serializeSpecific(DataOutputStream out) throws IOException;
    
    /**
     * Deserializa campos específicos de la estación
     * 
     * @param in Stream de entrada para leer los datos serializados
     * @throws IOException Si ocurre un error durante la lectura
     * 
     * Notas:
     * - Debe ser implementado por las clases hijas
     * - Deserializa información específica del tipo de estación
     * - Llamado automáticamente por el método deserialize principal
     */
    protected abstract void deserializeSpecific(DataInputStream in) throws IOException;
    
    // ========== SERIALIZACIÓN ==========
    
    /**
     * Serializa la estación completa a un stream de datos
     * 
     * @param out Stream de salida para escribir los datos serializados
     * @throws IOException Si ocurre un error durante la escritura
     * 
     * Datos serializados:
     * - Nombre de la clase para identificación del tipo
     * - ID, nombre y dirección de la estación
     * - Datos específicos del tipo de estación
     */
    @Override
    public void serialize(DataOutputStream out) throws IOException {
        out.writeUTF(getClass().getSimpleName()); // Escribir tipo de clase
        out.writeUTF(id != null ? id : "");
        out.writeUTF(name != null ? name : "");
        out.writeUTF(address != null ? address : "");
        serializeSpecific(out);
    }
    
    /**
     * Deserializa la estación desde un stream de datos
     * 
     * @param in Stream de entrada para leer los datos serializados
     * @throws IOException Si ocurre un error durante la lectura
     * 
     * Proceso:
     * - Lee ID, nombre y dirección de la estación
     * - Llama al método específico para deserializar datos adicionales
     * 
     * Notas:
     * - El tipo de clase ya fue leído por el método de fábrica
     */
    @Override
    public void deserialize(DataInputStream in) throws IOException {
        // El tipo de clase ya fue leído por el método de fábrica
        this.id = in.readUTF();
        this.name = in.readUTF();
        this.address = in.readUTF();
        deserializeSpecific(in);
    }
    
    /**
     * Método de fábrica para deserializar estaciones desde un stream
     * 
     * @param in Stream de entrada para leer los datos serializados
     * @return La estación deserializada
     * @throws IOException Si ocurre un error durante la deserialización
     * 
     * Proceso:
     * - Lee el nombre de la clase para identificar el tipo
     * - Crea la instancia apropiada según el tipo
     * - Deserializa los datos específicos de la estación
     * 
     * Tipos soportados:
     * - FuelStation: Estaciones de combustible
     * - ChargingStation: Estaciones de carga eléctrica
     */
    public static Station deserializeFromStream(DataInputStream in) throws IOException {
        String className = in.readUTF();
        Station station;
        
        switch (className) {
            case "FuelStation":
                station = new FuelStation();
                break;
            case "ChargingStation":
                station = new ChargingStation();
                break;
            default:
                throw new IOException("Unknown station type: " + className);
        }
        
        station.deserialize(in);
        return station;
    }
    
    // ========== MÉTODOS DE OBJETO ==========
    
    /**
     * Genera una representación en cadena de la estación
     * 
     * @return Cadena con nombre y tipo de la estación
     * 
     * Formato:
     * - Nombre de la estación
     * - Tipo de estación entre paréntesis
     */
    @Override
    public String toString() {
        return name + " (" + getStationType() + ")";
    }
    
    /**
     * Compara esta estación con otro objeto
     * 
     * @param obj El objeto a comparar
     * @return true si son iguales, false en caso contrario
     * 
     * Criterio de igualdad:
     * - Compara por ID único de la estación
     * - Maneja casos de null de forma segura
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Station station = (Station) obj;
        return id != null ? id.equals(station.id) : station.id == null;
    }
    
    /**
     * Genera el código hash de la estación
     * 
     * @return El código hash basado en el ID de la estación
     * 
     * Notas:
     * - Basado en el ID único de la estación
     * - Maneja casos de ID null
     */
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}