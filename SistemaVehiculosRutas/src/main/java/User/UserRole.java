/*
 * Nombre del Archivo: UserRole.java
 * 
 * Descripcion: Enumeración que define los roles de usuario disponibles en el sistema.
 *              Proporciona dos roles principales: Administrador y Usuario Regular,
 *              cada uno con un nombre de visualización apropiado para la interfaz
 *              de usuario. Facilita la gestión de permisos y funcionalidades
 *              específicas por rol.
 * 
 * Nombre de los Integrantes:
 * Javier Lee Liang
 * Paulo César Herrera Arias
 * José Emilio Alvarado Mendez
 * Josué Santiago Hidalgo Sandoval
 */
package User;

/**
 * Enumeración de roles de usuario del sistema
 * 
 * Esta enumeración define los diferentes roles que puede tener un usuario
 * en el sistema, proporcionando:
 * - Roles predefinidos con nombres descriptivos
 * - Métodos para obtener nombres de visualización
 * - Estructura para gestión de permisos por rol
 * 
 * Los roles determinan las funcionalidades disponibles para cada usuario.
 */
public enum UserRole {
    ADMIN("Administrator"),
    USER("Regular User");
    
    private final String displayName;
    
    /**
     * Constructor del rol de usuario
     * 
     * @param displayName El nombre de visualización del rol para la interfaz de usuario
     */
    UserRole(String displayName) {
        this.displayName = displayName;
    }
    
    /**
     * Obtiene el nombre de visualización del rol
     * 
     * @return El nombre descriptivo del rol para mostrar en la interfaz
     * 
     * Notas:
     * - Proporciona nombres amigables para el usuario
     * - Útil para mostrar información en menús y pantallas
     */
    public String getDisplayName() {
        return displayName;
    }
}
