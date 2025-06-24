/*
 * Nombre del Archivo: User.java
 * 
 * Descripcion: Clase que representa un usuario del sistema de planificación de rutas.
 *              Contiene información personal del usuario, credenciales de acceso,
 *              rol en el sistema, vehículos asociados y vehículo favorito. Implementa
 *              serialización para persistencia de datos y proporciona métodos para
 *              gestión de vehículos personales.
 * 
 * Nombre de los Integrantes:
 * Javier Lee Liang
 * Paulo César Herrera Arias
 * José Emilio Alvarado Mendez
 * Josué Santiago Hidalgo Sandoval
 */
package User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import Interfaces.Serializable;
import Vehicle.Vehicle;

/**
 * Clase que representa un usuario del sistema
 * 
 * Esta clase encapsula toda la información relacionada con un usuario:
 * - Información de identificación y autenticación
 * - Rol y permisos en el sistema
 * - Vehículos personales asociados
 * - Vehículo favorito para planificación de rutas
 * 
 * Implementa serialización para persistencia de datos y proporciona
 * métodos para gestión completa de vehículos personales.
 */
public class User implements Serializable{
    
    private String id;
    private String username;
    private String password; // In production, this should be hashed
    private UserRole role;
    private List<Vehicle> vehicles;
    private String favoriteVehicleId; // ID reference to avoid circular serialization
    
    /**
     * Constructor por defecto del usuario
     * 
     * Características:
     * - Genera un ID único automáticamente
     * - Inicializa la lista de vehículos vacía
     * - Establece valores por defecto para otros campos
     */
    public User() {
        this.id = UUID.randomUUID().toString();
        this.vehicles = new ArrayList<>();
    }
    
    /**
     * Constructor completo del usuario
     * 
     * @param username Nombre de usuario para autenticación
     * @param password Contraseña del usuario (debería estar hasheada en producción)
     * @param role Rol del usuario en el sistema
     * 
     * Notas:
     * - Llama al constructor por defecto para inicialización básica
     * - Establece los valores proporcionados para los campos principales
     */
    public User(String username, String password, UserRole role) {
        this();
        this.username = username;
        this.password = password;
        this.role = role;
    }
    
    // ========== GETTERS Y SETTERS ==========
    
    /**
     * Obtiene el ID único del usuario
     * @return El ID único del usuario
     */
    public String getId() { return id; }
    
    /**
     * Establece el ID único del usuario
     * @param id El nuevo ID del usuario
     */
    public void setId(String id) { this.id = id; }
    
    /**
     * Obtiene el nombre de usuario
     * @return El nombre de usuario para autenticación
     */
    public String getUsername() { return username; }
    
    /**
     * Establece el nombre de usuario
     * @param username El nuevo nombre de usuario
     */
    public void setUsername(String username) { this.username = username; }
    
    /**
     * Obtiene la contraseña del usuario
     * @return La contraseña (debería estar hasheada en producción)
     */
    public String getPassword() { return password; }
    
    /**
     * Establece la contraseña del usuario
     * @param password La nueva contraseña
     */
    public void setPassword(String password) { this.password = password; }
    
    /**
     * Obtiene el rol del usuario
     * @return El rol del usuario en el sistema
     */
    public UserRole getRole() { return role; }
    
    /**
     * Establece el rol del usuario
     * @param role El nuevo rol del usuario
     */
    public void setRole(UserRole role) { this.role = role; }
    
    /**
     * Obtiene la lista de vehículos del usuario
     * @return Lista de vehículos personales del usuario
     */
    public List<Vehicle> getVehicles() { return vehicles; }
    
    /**
     * Establece la lista de vehículos del usuario
     * @param vehicles La nueva lista de vehículos
     */
    public void setVehicles(List<Vehicle> vehicles) { this.vehicles = vehicles; }
    
    /**
     * Obtiene el ID del vehículo favorito
     * @return El ID del vehículo favorito, o null si no hay favorito
     */
    public String getFavoriteVehicleId() { return favoriteVehicleId; }
    
    /**
     * Establece el ID del vehículo favorito
     * @param favoriteVehicleId El ID del nuevo vehículo favorito
     */
    public void setFavoriteVehicleId(String favoriteVehicleId) { this.favoriteVehicleId = favoriteVehicleId; }
    
    /**
     * Obtiene el objeto vehículo favorito desde la lista
     * 
     * @return El vehículo favorito, o null si no existe o no está en la lista
     * 
     * Notas:
     * - Busca el vehículo por ID en la lista de vehículos
     * - Utiliza stream para búsqueda eficiente
     * - Retorna null si no se encuentra el vehículo
     */
    public Vehicle getFavoriteVehicle() {
        if (favoriteVehicleId == null) return null;
        return vehicles.stream()
                .filter(v -> favoriteVehicleId.equals(v.getId()))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Establece el vehículo favorito por referencia de objeto
     * 
     * @param vehicle El vehículo a establecer como favorito, o null para quitar favorito
     * 
     * Notas:
     * - Almacena solo el ID del vehículo para evitar serialización circular
     * - Permite establecer null para quitar el favorito
     */
    public void setFavoriteVehicle(Vehicle vehicle) {
        this.favoriteVehicleId = vehicle != null ? vehicle.getId() : null;
    }
    
    /**
     * Agrega un vehículo a la lista personal del usuario
     * 
     * @param vehicle El vehículo a agregar
     * 
     * Notas:
     * - Verifica que el vehículo no esté ya en la lista
     * - Evita duplicados automáticamente
     */
    public void addVehicle(Vehicle vehicle) {
        if (!vehicles.contains(vehicle)) {
            vehicles.add(vehicle);
        }
    }
    
    /**
     * Elimina un vehículo de la lista personal del usuario
     * 
     * @param vehicle El vehículo a eliminar
     * @return true si se eliminó exitosamente, false si no estaba en la lista
     * 
     * Notas:
     * - Si el vehículo eliminado era el favorito, quita la referencia
     * - Actualiza automáticamente el vehículo favorito si es necesario
     */
    public boolean removeVehicle(Vehicle vehicle) {
        if (favoriteVehicleId != null && favoriteVehicleId.equals(vehicle.getId())) {
            favoriteVehicleId = null;
        }
        return vehicles.remove(vehicle);
    }
    
    /**
     * Verifica si el usuario es administrador
     * 
     * @return true si el usuario tiene rol de administrador, false en caso contrario
     */
    public boolean isAdmin() {
        return role == UserRole.ADMIN;
    }
    
    /**
     * Serializa el objeto usuario a un stream de datos
     * 
     * @param out Stream de salida para escribir los datos serializados
     * @throws IOException Si ocurre un error durante la escritura
     * 
     * Datos serializados:
     * - ID del usuario
     * - Nombre de usuario
     * - Contraseña
     * - Rol del usuario
     * - ID del vehículo favorito
     * - Lista completa de vehículos
     */
    @Override
    public void serialize(DataOutputStream out) throws IOException {
        out.writeUTF(id != null ? id : "");
        out.writeUTF(username != null ? username : "");
        out.writeUTF(password != null ? password : "");
        out.writeUTF(role != null ? role.name() : UserRole.USER.name());
        out.writeUTF(favoriteVehicleId != null ? favoriteVehicleId : "");
        
        // Serializar lista de vehículos
        out.writeInt(vehicles.size());
        for (Vehicle vehicle : vehicles) {
            vehicle.serialize(out);
        }
    }
    
    /**
     * Deserializa el objeto usuario desde un stream de datos
     * 
     * @param in Stream de entrada para leer los datos serializados
     * @throws IOException Si ocurre un error durante la lectura
     * 
     * Proceso de deserialización:
     * - Lee los campos básicos del usuario
     * - Restaura el rol del usuario
     * - Recupera el ID del vehículo favorito
     * - Deserializa la lista completa de vehículos
     */
    @Override
    public void deserialize(DataInputStream in) throws IOException {
        this.id = in.readUTF();
        this.username = in.readUTF();
        this.password = in.readUTF();
        this.role = UserRole.valueOf(in.readUTF());
        String favVehicleId = in.readUTF();
        this.favoriteVehicleId = favVehicleId.isEmpty() ? null : favVehicleId;
        
        // Deserializar lista de vehículos
        int vehicleCount = in.readInt();
        this.vehicles = new ArrayList<>();
        for (int i = 0; i < vehicleCount; i++) {
            // Requiere método estático Vehicle.deserializeFromStream()
            Vehicle vehicle = Vehicle.deserializeFromStream(in);
            vehicles.add(vehicle);
        }
    }
    
    /**
     * Genera una representación en cadena del usuario
     * 
     * @return Cadena con el nombre de usuario y su rol
     */
    @Override
    public String toString() {
        return username + " (" + role.getDisplayName() + ")";
    }
    
    /**
     * Compara este usuario con otro objeto
     * 
     * @param obj El objeto a comparar
     * @return true si son iguales, false en caso contrario
     * 
     * Criterio de igualdad:
     * - Compara por ID único del usuario
     * - Maneja casos de null de forma segura
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return id != null ? id.equals(user.id) : user.id == null;
    }
    
    /**
     * Genera el código hash del usuario
     * 
     * @return El código hash basado en el ID del usuario
     * 
     * Notas:
     * - Basado en el ID único del usuario
     * - Maneja casos de ID null
     */
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
