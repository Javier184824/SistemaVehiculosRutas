/*
 * Nombre del Archivo: Session.java
 * 
 * Descripcion: Clase que representa una sesión de usuario activa en el sistema.
 *              Almacena información sobre el usuario autenticado y el momento
 *              de inicio de sesión. Proporciona métodos para verificar el rol
 *              del usuario y acceder a la información de la sesión. Facilita
 *              la gestión de autenticación y autorización en el sistema.
 * 
 * Nombre de los Integrantes:
 * Javier Lee Liang
 * Paulo César Herrera Arias
 * José Emilio Alvarado Mendez
 * Josué Santiago Hidalgo Sandoval
 */
package Services;

import User.User;

/**
 * Clase que representa una sesión de usuario activa
 * 
 * Esta clase encapsula la información de una sesión de usuario autenticado:
 * - Referencia al usuario autenticado
 * - Timestamp del momento de inicio de sesión
 * - Métodos para verificar permisos y roles
 * 
 * Proporciona funcionalidades para gestión de sesiones y control de acceso.
 */
public class Session {
    
    private final User user;
    private final long loginTime;
    
    /**
     * Constructor de una nueva sesión de usuario
     * 
     * @param user El usuario que inicia la sesión
     * 
     * Características:
     * - Establece el usuario de la sesión
     * - Registra automáticamente el timestamp de inicio
     * - Utiliza el tiempo actual del sistema en milisegundos
     */
    public Session(User user) {
        this.user = user;
        this.loginTime = System.currentTimeMillis();
    }
    
    /**
     * Obtiene el usuario asociado a esta sesión
     * 
     * @return El usuario autenticado en esta sesión
     */
    public User getUser() {
        return user;
    }
    
    /**
     * Obtiene el timestamp de inicio de la sesión
     * 
     * @return El tiempo de inicio de sesión en milisegundos desde epoch
     * 
     * Notas:
     * - Útil para calcular duración de sesión
     * - Puede usarse para expiración automática de sesiones
     */
    public long getLoginTime() {
        return loginTime;
    }
    
    /**
     * Verifica si el usuario de esta sesión es administrador
     * 
     * @return true si el usuario es administrador, false en caso contrario
     * 
     * Notas:
     * - Verifica que el usuario no sea null antes de consultar su rol
     * - Utiliza el método isAdmin() del usuario
     * - Útil para control de acceso a funcionalidades administrativas
     */
    public boolean isAdmin() {
        return user != null && user.isAdmin();
    }
    
    /**
     * Genera una representación en cadena de la sesión
     * 
     * @return Cadena con información del usuario y tiempo de inicio
     * 
     * Formato:
     * - Incluye nombre de usuario y timestamp de login
     * - Útil para debugging y logging
     */
    @Override
    public String toString() {
        return "Session{user=" + user.getUsername() + ", loginTime=" + loginTime + "}";
    }
}
