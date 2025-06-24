/*
 * Nombre del Archivo: ChargingStation.java
 * 
 * Descripcion: Clase que representa una estación de carga eléctrica en el sistema
 *              de planificación de rutas. Extiende la clase Station para
 *              proporcionar funcionalidades específicas para estaciones de
 *              carga eléctrica, incluyendo gestión de tipos de cargadores
 *              disponibles, distinción entre estaciones públicas y privadas,
 *              y compatibilidad con vehículos eléctricos.
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

import Vehicle.ChargerType;

/**
 * Clase que representa una estación de carga eléctrica
 * 
 * Esta clase extiende Station para proporcionar funcionalidades específicas
 * para estaciones de carga eléctrica:
 * - Gestión de tipos de cargadores disponibles
 * - Distinción entre estaciones públicas y privadas
 * - Verificación de compatibilidad con vehículos eléctricos
 * - Operaciones de agregar y eliminar tipos de cargadores
 * - Serialización específica para tipos de cargadores
 * 
 * Utilizada para representar estaciones de carga rápida, lenta y otros tipos.
 */
public class ChargingStation extends Station{
    
    private List<ChargerType> availableChargers;
    private boolean isPublic;
    
    /**
     * Constructor por defecto de la estación de carga
     * 
     * Notas:
     * - Llama al constructor por defecto de la clase padre
     * - Inicializa la lista de cargadores disponibles vacía
     * - Establece la estación como pública por defecto
     * - Útil para deserialización
     */
    public ChargingStation() {
        super();
        this.availableChargers = new ArrayList<>();
        this.isPublic = true;
    }
    
    /**
     * Constructor completo de la estación de carga
     * 
     * @param name Nombre de la estación
     * @param address Dirección de la estación
     * @param availableChargers Lista de tipos de cargadores disponibles
     * @param isPublic Indica si la estación es pública o privada
     * 
     * Notas:
     * - Llama al constructor de la clase padre con nombre y dirección
     * - Crea una copia de la lista de cargadores para evitar modificaciones externas
     * - Establece el estado público/privado de la estación
     */
    public ChargingStation(String name, String address, List<ChargerType> availableChargers, boolean isPublic) {
        super(name, address);
        this.availableChargers = new ArrayList<>(availableChargers);
        this.isPublic = isPublic;
    }
    
    /**
     * Obtiene los tipos de cargadores disponibles en la estación
     * @return Lista de tipos de cargadores disponibles
     */
    public List<ChargerType> getAvailableChargers() { return availableChargers; }
    
    /**
     * Establece los tipos de cargadores disponibles en la estación
     * @param availableChargers Nueva lista de tipos de cargadores
     * 
     * Notas:
     * - Crea una copia de la lista para evitar modificaciones externas
     * - Reemplaza completamente la lista actual
     */
    public void setAvailableChargers(List<ChargerType> availableChargers) { 
        this.availableChargers = new ArrayList<>(availableChargers); 
    }
    
    /**
     * Verifica si la estación es pública
     * @return true si la estación es pública, false si es privada
     */
    public boolean isPublic() { return isPublic; }
    
    /**
     * Establece si la estación es pública o privada
     * @param isPublic true para estación pública, false para privada
     */
    public void setPublic(boolean isPublic) { this.isPublic = isPublic; }
    
    /**
     * Agrega un tipo de cargador a la estación
     * 
     * @param chargerType El tipo de cargador a agregar
     * 
     * Notas:
     * - Verifica que el tipo no esté ya en la lista
     * - Evita duplicados automáticamente
     */
    public void addChargerType(ChargerType chargerType) {
        if (!availableChargers.contains(chargerType)) {
            availableChargers.add(chargerType);
        }
    }
    
    /**
     * Elimina un tipo de cargador de la estación
     * 
     * @param chargerType El tipo de cargador a eliminar
     * @return true si se eliminó exitosamente, false si no estaba en la lista
     */
    public boolean removeChargerType(ChargerType chargerType) {
        return availableChargers.remove(chargerType);
    }
    
    /**
     * Verifica si la estación soporta un tipo de cargador específico
     * 
     * @param chargerType El tipo de cargador a verificar
     * @return true si la estación soporta el tipo de cargador, false en caso contrario
     * 
     * Notas:
     * - Utilizado para verificar compatibilidad con vehículos eléctricos
     * - Útil para filtrado de estaciones en planificación de rutas
     */
    public boolean supportsChargerType(ChargerType chargerType) {
        return availableChargers.contains(chargerType);
    }
    
    /**
     * Obtiene el tipo de estación
     * 
     * @return "Charging Station" con indicación de si es pública o privada
     * 
     * Notas:
     * - Sobrescribe el método de la clase padre
     * - Proporciona identificación específica para estaciones de carga
     * - Incluye información sobre el acceso público/privado
     */
    @Override
    public String getStationType() {
        return "Charging Station" + (isPublic ? " (Public)" : " (Private)");
    }
    
    /**
     * Serializa los datos específicos de la estación de carga
     * 
     * @param out Stream de salida para escribir los datos serializados
     * @throws IOException Si ocurre un error durante la escritura
     * 
     * Datos serializados:
     * - Número de tipos de cargadores disponibles
     * - Información de cada tipo de cargador
     * - Estado público/privado de la estación
     */
    @Override
    protected void serializeSpecific(DataOutputStream out) throws IOException {
        out.writeInt(availableChargers.size());
        for (ChargerType charger : availableChargers) {
            charger.serialize(out);
        }
        out.writeBoolean(isPublic);
    }
    
    /**
     * Deserializa los datos específicos de la estación de carga
     * 
     * @param in Stream de entrada para leer los datos serializados
     * @throws IOException Si ocurre un error durante la lectura
     * 
     * Proceso:
     * - Lee el número de tipos de cargadores
     * - Deserializa cada tipo de cargador
     * - Reconstruye la lista de cargadores disponibles
     * - Restaura el estado público/privado de la estación
     */
    @Override
    protected void deserializeSpecific(DataInputStream in) throws IOException {
        int chargerCount = in.readInt();
        this.availableChargers = new ArrayList<>();
        for (int i = 0; i < chargerCount; i++) {
            ChargerType charger = new ChargerType();
            charger.deserialize(in);
            availableChargers.add(charger);
        }
        this.isPublic = in.readBoolean();
    }
    
    /**
     * Genera una representación en cadena de la estación de carga
     * 
     * @return Cadena con información de la estación y número de cargadores
     * 
     * Formato:
     * - Información básica de la estación (de la clase padre)
     * - Número de tipos de cargadores disponibles
     */
    @Override
    public String toString() {
        return super.toString() + " - Chargers: " + availableChargers.size();
    }
}
