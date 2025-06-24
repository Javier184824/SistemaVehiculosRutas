/*
 * Nombre del Archivo: FuelStation.java
 * 
 * Descripcion: Clase que representa una estación de combustible en el sistema
 *              de planificación de rutas. Extiende la clase Station para
 *              proporcionar funcionalidades específicas para estaciones de
 *              combustible, incluyendo gestión de tipos de combustible
 *              disponibles y compatibilidad con vehículos de combustible.
 *              Implementa serialización para persistencia de datos.
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

import Vehicle.FuelType;

/**
 * Clase que representa una estación de combustible
 * 
 * Esta clase extiende Station para proporcionar funcionalidades específicas
 * para estaciones de combustible:
 * - Gestión de tipos de combustible disponibles
 * - Verificación de compatibilidad con vehículos de combustible
 * - Operaciones de agregar y eliminar tipos de combustible
 * - Serialización específica para tipos de combustible
 * 
 * Utilizada para representar estaciones de gasolina, diesel y otros combustibles.
 */
public class FuelStation extends Station{
    
    private List<FuelType> availableFuels;
    
    /**
     * Constructor por defecto de la estación de combustible
     * 
     * Notas:
     * - Llama al constructor por defecto de la clase padre
     * - Inicializa la lista de combustibles disponibles vacía
     * - Útil para deserialización
     */
    public FuelStation() {
        super();
        this.availableFuels = new ArrayList<>();
    }
    
    /**
     * Constructor completo de la estación de combustible
     * 
     * @param name Nombre de la estación
     * @param address Dirección de la estación
     * @param availableFuels Lista de tipos de combustible disponibles
     * 
     * Notas:
     * - Llama al constructor de la clase padre con nombre y dirección
     * - Crea una copia de la lista de combustibles para evitar modificaciones externas
     */
    public FuelStation(String name, String address, List<FuelType> availableFuels) {
        super(name, address);
        this.availableFuels = new ArrayList<>(availableFuels);
    }
    
    /**
     * Obtiene los tipos de combustible disponibles en la estación
     * @return Lista de tipos de combustible disponibles
     */
    public List<FuelType> getAvailableFuels() { return availableFuels; }
    
    /**
     * Establece los tipos de combustible disponibles en la estación
     * @param availableFuels Nueva lista de tipos de combustible
     * 
     * Notas:
     * - Crea una copia de la lista para evitar modificaciones externas
     * - Reemplaza completamente la lista actual
     */
    public void setAvailableFuels(List<FuelType> availableFuels) { 
        this.availableFuels = new ArrayList<>(availableFuels); 
    }
    
    /**
     * Agrega un tipo de combustible a la estación
     * 
     * @param fuelType El tipo de combustible a agregar
     * 
     * Notas:
     * - Verifica que el tipo no esté ya en la lista
     * - Evita duplicados automáticamente
     */
    public void addFuelType(FuelType fuelType) {
        if (!availableFuels.contains(fuelType)) {
            availableFuels.add(fuelType);
        }
    }
    
    /**
     * Elimina un tipo de combustible de la estación
     * 
     * @param fuelType El tipo de combustible a eliminar
     * @return true si se eliminó exitosamente, false si no estaba en la lista
     */
    public boolean removeFuelType(FuelType fuelType) {
        return availableFuels.remove(fuelType);
    }
    
    /**
     * Verifica si la estación soporta un tipo de combustible específico
     * 
     * @param fuelType El tipo de combustible a verificar
     * @return true si la estación soporta el tipo de combustible, false en caso contrario
     * 
     * Notas:
     * - Utilizado para verificar compatibilidad con vehículos
     * - Útil para filtrado de estaciones en planificación de rutas
     */
    public boolean supportsFuelType(FuelType fuelType) {
        return availableFuels.contains(fuelType);
    }
    
    /**
     * Obtiene el tipo de estación
     * 
     * @return "Fuel Station" como identificador del tipo de estación
     * 
     * Notas:
     * - Sobrescribe el método de la clase padre
     * - Proporciona identificación específica para estaciones de combustible
     */
    @Override
    public String getStationType() {
        return "Fuel Station";
    }
    
    /**
     * Serializa los datos específicos de la estación de combustible
     * 
     * @param out Stream de salida para escribir los datos serializados
     * @throws IOException Si ocurre un error durante la escritura
     * 
     * Datos serializados:
     * - Número de tipos de combustible disponibles
     * - Información de cada tipo de combustible
     */
    @Override
    protected void serializeSpecific(DataOutputStream out) throws IOException {
        out.writeInt(availableFuels.size());
        for (FuelType fuel : availableFuels) {
            fuel.serialize(out);
        }
    }
    
    /**
     * Deserializa los datos específicos de la estación de combustible
     * 
     * @param in Stream de entrada para leer los datos serializados
     * @throws IOException Si ocurre un error durante la lectura
     * 
     * Proceso:
     * - Lee el número de tipos de combustible
     * - Deserializa cada tipo de combustible
     * - Reconstruye la lista de combustibles disponibles
     */
    @Override
    protected void deserializeSpecific(DataInputStream in) throws IOException {
        int fuelCount = in.readInt();
        this.availableFuels = new ArrayList<>();
        for (int i = 0; i < fuelCount; i++) {
            FuelType fuel = new FuelType();
            fuel.deserialize(in);
            availableFuels.add(fuel);
        }
    }
    
    /**
     * Genera una representación en cadena de la estación de combustible
     * 
     * @return Cadena con información de la estación y número de combustibles
     * 
     * Formato:
     * - Información básica de la estación (de la clase padre)
     * - Número de tipos de combustible disponibles
     */
    @Override
    public String toString() {
        return super.toString() + " - Fuels: " + availableFuels.size();
    }
}
