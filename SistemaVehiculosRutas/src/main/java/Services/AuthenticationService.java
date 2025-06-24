/*
 * Nombre del Archivo: AuthenticationService.java
 * 
 * Descripcion: Servicio de autenticación y gestión de sesiones del sistema.
 *              Proporciona funcionalidades completas para autenticación de
 *              usuarios, registro de nuevos usuarios, gestión de sesiones
 *              activas, cambio de contraseñas y verificación de roles.
 *              Integra con el sistema de persistencia para gestión segura
 *              de credenciales y sesiones de usuario.
 * 
 * Nombre de los Integrantes:
 * Javier Lee Liang
 * Paulo César Herrera Arias
 * José Emilio Alvarado Mendez
 * Josué Santiago Hidalgo Sandoval
 */
package Services;

import java.util.List;
import java.util.Optional;

import Binary.FileConstants;
import Interfaces.DataManager;
import Interfaces.SerializationException;
import User.User;
import User.UserRole;

/**
 * Servicio de autenticación y gestión de sesiones
 * 
 * Esta clase proporciona funcionalidades completas para la gestión de usuarios:
 * - Autenticación con nombre de usuario y contraseña
 * - Registro de nuevos usuarios
 * - Gestión de sesiones activas
 * - Verificación de roles y permisos
 * - Cambio de contraseñas
 * - Actualización de información de usuario
 * 
 * Mantiene la seguridad y integridad de las sesiones de usuario.
 */
public class AuthenticationService {
    
    private final DataManager dataManager;
    private Session currentSession;
    
    /**
     * Constructor del servicio de autenticación
     * 
     * @param dataManager Gestor de datos para operaciones de persistencia
     * 
     * Notas:
     * - Establece la dependencia del gestor de datos
     * - Inicializa sin sesión activa
     */
    public AuthenticationService(DataManager dataManager) {
        this.dataManager = dataManager;
    }
    
    /**
     * Autentica un usuario con nombre de usuario y contraseña
     * 
     * @param username El nombre de usuario
     * @param password La contraseña del usuario
     * @return true si la autenticación fue exitosa, false en caso contrario
     * 
     * Proceso:
     * - Valida que username y password no sean null o vacíos
     * - Carga la lista de usuarios desde el archivo
     * - Busca coincidencia exacta de username y password
     * - Crea una nueva sesión si la autenticación es exitosa
     * 
     * Validaciones:
     * - Username no puede ser null o vacío
     * - Password no puede ser null
     * - Maneja errores de serialización
     */
    public boolean login(String username, String password) {
        if (username == null || password == null || username.trim().isEmpty()) {
            return false;
        }
        
        try {
            List<User> users = dataManager.loadList(FileConstants.USERS_FILE, User::new);
            Optional<User> userOpt = users.stream()
                .filter(user -> username.equals(user.getUsername()) && password.equals(user.getPassword()))
                .findFirst();
            
            if (userOpt.isPresent()) {
                currentSession = new Session(userOpt.get());
                return true;
            }
            
        } catch (SerializationException e) {
            System.err.println("Error loading users during login: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Registra un nuevo usuario en el sistema
     * 
     * @param username El nombre de usuario para el nuevo usuario
     * @param password La contraseña del nuevo usuario
     * @param role El rol del nuevo usuario
     * @return El usuario creado, o null si el registro falló
     * 
     * Proceso:
     * - Valida que username y password no sean null o vacíos
     * - Verifica que el nombre de usuario no exista ya
     * - Crea un nuevo usuario con los datos proporcionados
     * - Guarda la lista actualizada de usuarios
     * 
     * Validaciones:
     * - Username y password no pueden ser null o vacíos
     * - No se permiten nombres de usuario duplicados
     * - Maneja errores de serialización
     */
    public User register(String username, String password, UserRole role) {
        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            return null;
        }
        
        try {
            List<User> users = dataManager.loadList(FileConstants.USERS_FILE, User::new);
            
            // Verificar que el nombre de usuario no exista ya
            boolean usernameExists = users.stream()
                .anyMatch(user -> username.equals(user.getUsername()));
            
            if (usernameExists) {
                return null; // Nombre de usuario ya tomado
            }
            
            // Crear nuevo usuario
            User newUser = new User(username, password, role);
            users.add(newUser);
            
            // Guardar lista actualizada de usuarios
            dataManager.saveList(users, FileConstants.USERS_FILE);
            
            return newUser;
            
        } catch (SerializationException e) {
            System.err.println("Error during user registration: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Cierra la sesión del usuario actual
     * 
     * Notas:
     * - Elimina la referencia a la sesión actual
     * - No requiere confirmación
     * - Útil para cerrar sesión de forma segura
     */
    public void logout() {
        currentSession = null;
    }
    
    /**
     * Obtiene el usuario autenticado actual
     * 
     * @return El usuario actual, o null si no hay sesión activa
     * 
     * Notas:
     * - Retorna null si no hay sesión activa
     * - Proporciona acceso al usuario de la sesión actual
     */
    public User getCurrentUser() {
        return currentSession != null ? currentSession.getUser() : null;
    }
    
    /**
     * Obtiene la sesión actual
     * 
     * @return La sesión actual, o null si no hay sesión activa
     * 
     * Notas:
     * - Proporciona acceso completo a la información de la sesión
     * - Útil para obtener timestamp de login y otros datos de sesión
     */
    public Session getCurrentSession() {
        return currentSession;
    }
    
    /**
     * Verifica si hay un usuario autenticado actualmente
     * 
     * @return true si hay una sesión activa, false en caso contrario
     * 
     * Notas:
     * - Verifica la existencia de una sesión activa
     * - Útil para control de acceso a funcionalidades
     */
    public boolean isAuthenticated() {
        return currentSession != null;
    }
    
    /**
     * Verifica si el usuario actual es administrador
     * 
     * @return true si el usuario actual es administrador, false en caso contrario
     * 
     * Notas:
     * - Verifica que haya una sesión activa
     * - Utiliza el método isAdmin() de la sesión
     * - Útil para control de acceso a funcionalidades administrativas
     */
    public boolean isCurrentUserAdmin() {
        return currentSession != null && currentSession.isAdmin();
    }
    
    /**
     * Actualiza la información del usuario actual
     * 
     * @param updatedUser La información actualizada del usuario
     * @return true si la actualización fue exitosa, false en caso contrario
     * 
     * Proceso:
     * - Verifica que haya una sesión activa
     * - Busca el usuario en la lista de usuarios
     * - Actualiza la información del usuario
     * - Guarda la lista actualizada
     * - Actualiza la sesión actual
     * 
     * Validaciones:
     * - Requiere sesión activa
     * - El usuario actualizado no puede ser null
     * - Maneja errores de serialización
     */
    public boolean updateCurrentUser(User updatedUser) {
        if (currentSession == null || updatedUser == null) {
            return false;
        }
        
        try {
            List<User> users = dataManager.loadList(FileConstants.USERS_FILE, User::new);
            
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getId().equals(currentSession.getUser().getId())) {
                    users.set(i, updatedUser);
                    dataManager.saveList(users, FileConstants.USERS_FILE);
                    
                    // Actualizar sesión actual
                    currentSession = new Session(updatedUser);
                    return true;
                }
            }
            
        } catch (SerializationException e) {
            System.err.println("Error updating user: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Cambia la contraseña del usuario actual
     * 
     * @param oldPassword La contraseña actual
     * @param newPassword La nueva contraseña
     * @return true si el cambio de contraseña fue exitoso, false en caso contrario
     * 
     * Proceso:
     * - Verifica que haya una sesión activa
     * - Valida que la contraseña actual sea correcta
     * - Actualiza la contraseña del usuario
     * - Guarda los cambios en el sistema
     * 
     * Validaciones:
     * - Requiere sesión activa
     * - La contraseña actual debe coincidir
     * - Las contraseñas no pueden ser null
     */
    public boolean changePassword(String oldPassword, String newPassword) {
        if (currentSession == null || oldPassword == null || newPassword == null) {
            return false;
        }
        
        User currentUser = currentSession.getUser();
        if (!oldPassword.equals(currentUser.getPassword())) {
            return false; // La contraseña actual no coincide
        }
        
        currentUser.setPassword(newPassword);
        return updateCurrentUser(currentUser);
    }
}
