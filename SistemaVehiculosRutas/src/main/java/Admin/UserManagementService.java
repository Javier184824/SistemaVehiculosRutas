/*
 * Nombre del Archivo: UserManagementService.java
 * 
 * Descripcion: Servicio de gestión de usuarios del sistema. Proporciona funcionalidades
 *              completas para administrar usuarios, incluyendo operaciones CRUD,
 *              cambio de roles, búsqueda por diferentes criterios y estadísticas
 *              de usuarios. Maneja la persistencia de datos de usuarios de forma
 *              segura y eficiente.
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
import User.User;
import User.UserRole;

/**
 * Servicio de gestión de usuarios del sistema
 * 
 * Esta clase proporciona funcionalidades completas para la administración de usuarios:
 * - Creación, actualización y eliminación de usuarios
 * - Cambio de roles de usuario (Admin/Regular)
 * - Búsqueda de usuarios por ID y nombre de usuario
 * - Filtrado de usuarios por rol
 * - Estadísticas de usuarios del sistema
 * 
 * Todas las operaciones incluyen validación de datos y manejo de errores.
 */
public class UserManagementService {
    
    private final DataManager dataManager;
    
    /**
     * Constructor del servicio de gestión de usuarios
     * 
     * @param dataManager Gestor de datos para operaciones de persistencia
     */
    public UserManagementService(DataManager dataManager) {
        this.dataManager = dataManager;
    }
    
    /**
     * Obtiene todos los usuarios del sistema
     * 
     * @return Lista de todos los usuarios registrados
     * 
     * Notas:
     * - Retorna una lista vacía si hay errores de carga
     * - Maneja errores de serialización
     */
    public List<User> getAllUsers() {
        try {
            return dataManager.loadList(FileConstants.USERS_FILE, User::new);
        } catch (SerializationException e) {
            System.err.println("Error loading users: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Crea un nuevo usuario en el sistema
     * 
     * @param user El usuario a crear
     * @return true si se creó exitosamente, false en caso contrario
     * 
     * Restricciones:
     * - El usuario no puede ser null
     * - No se permiten nombres de usuario duplicados
     * - Se valida la integridad de los datos antes de guardar
     */
    public boolean createUser(User user) {
        if (user == null) {
            return false;
        }
        
        try {
            List<User> users = getAllUsers();
            
            // Verificar nombres de usuario duplicados
            boolean usernameExists = users.stream()
                .anyMatch(u -> user.getUsername().equals(u.getUsername()));
            
            if (usernameExists) {
                return false;
            }
            
            users.add(user);
            dataManager.saveList(users, FileConstants.USERS_FILE);
            return true;
            
        } catch (SerializationException e) {
            System.err.println("Error creating user: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Actualiza un usuario existente en el sistema
     * 
     * @param userId El ID del usuario a actualizar
     * @param updatedUser La información actualizada del usuario
     * @return true si se actualizó exitosamente, false en caso contrario
     * 
     * Notas:
     * - Busca el usuario por ID
     * - Reemplaza completamente la información existente
     * - Mantiene la integridad de los datos
     */
    public boolean updateUser(String userId, User updatedUser) {
        if (userId == null || updatedUser == null) {
            return false;
        }
        
        try {
            List<User> users = getAllUsers();
            
            for (int i = 0; i < users.size(); i++) {
                if (userId.equals(users.get(i).getId())) {
                    users.set(i, updatedUser);
                    dataManager.saveList(users, FileConstants.USERS_FILE);
                    return true;
                }
            }
            
        } catch (SerializationException e) {
            System.err.println("Error updating user: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Elimina un usuario del sistema
     * 
     * @param userId El ID del usuario a eliminar
     * @return true si se eliminó exitosamente, false en caso contrario
     * 
     * Notas:
     * - Busca y elimina el usuario por ID
     * - Actualiza la persistencia de datos
     * - Maneja errores de serialización
     */
    public boolean deleteUser(String userId) {
        if (userId == null) {
            return false;
        }
        
        try {
            List<User> users = getAllUsers();
            boolean removed = users.removeIf(user -> userId.equals(user.getId()));
            
            if (removed) {
                dataManager.saveList(users, FileConstants.USERS_FILE);
                return true;
            }
            
        } catch (SerializationException e) {
            System.err.println("Error deleting user: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Cambia el rol de un usuario específico
     * 
     * @param userId El ID del usuario cuyo rol se va a cambiar
     * @param newRole El nuevo rol a asignar
     * @return true si se cambió el rol exitosamente, false en caso contrario
     * 
     * Notas:
     * - Busca el usuario por ID
     * - Actualiza solo el rol del usuario
     * - Mantiene el resto de la información del usuario intacta
     */
    public boolean changeUserRole(String userId, UserRole newRole) {
        if (userId == null || newRole == null) {
            return false;
        }
        
        try {
            List<User> users = getAllUsers();
            
            for (User user : users) {
                if (userId.equals(user.getId())) {
                    user.setRole(newRole);
                    dataManager.saveList(users, FileConstants.USERS_FILE);
                    return true;
                }
            }
            
        } catch (SerializationException e) {
            System.err.println("Error changing user role: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Busca un usuario por su ID único
     * 
     * @param userId El ID del usuario a buscar
     * @return El usuario encontrado, o null si no existe
     * 
     * Notas:
     * - Utiliza búsqueda por stream para eficiencia
     * - Retorna null si no se encuentra el usuario
     */
    public User findUserById(String userId) {
        return getAllUsers().stream()
            .filter(user -> userId.equals(user.getId()))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Busca un usuario por su nombre de usuario
     * 
     * @param username El nombre de usuario a buscar
     * @return El usuario encontrado, o null si no existe
     * 
     * Notas:
     * - Utiliza búsqueda por stream para eficiencia
     * - Retorna null si no se encuentra el usuario
     * - Los nombres de usuario son únicos en el sistema
     */
    public User findUserByUsername(String username) {
        return getAllUsers().stream()
            .filter(user -> username.equals(user.getUsername()))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Obtiene todos los usuarios con un rol específico
     * 
     * @param role El rol por el cual filtrar los usuarios
     * @return Lista de usuarios que tienen el rol especificado
     * 
     * Notas:
     * - Utiliza filtrado por stream para eficiencia
     * - Retorna una lista vacía si no hay usuarios con ese rol
     */
    public List<User> getUsersByRole(UserRole role) {
        return getAllUsers().stream()
            .filter(user -> role.equals(user.getRole()))
            .toList();
    }
    
    /**
     * Genera estadísticas de usuarios del sistema
     * 
     * @return Cadena formateada con estadísticas de usuarios
     * 
     * Estadísticas incluidas:
     * - Total de usuarios en el sistema
     * - Número de administradores
     * - Número de usuarios regulares
     */
    public String getUserStatistics() {
        List<User> users = getAllUsers();
        long adminCount = users.stream().filter(u -> u.getRole() == UserRole.ADMIN).count();
        long userCount = users.stream().filter(u -> u.getRole() == UserRole.USER).count();
        
        return String.format("Total Users: %d%nAdmins: %d%nRegular Users: %d", 
                           users.size(), adminCount, userCount);
    }
}
