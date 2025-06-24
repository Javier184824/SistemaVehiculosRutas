/*
 * Nombre del Archivo: ConsoleUI.java
 * 
 * Descripcion: Interfaz principal de consola del sistema de planificación de rutas.
 *              Gestiona el flujo principal de la aplicación, incluyendo autenticación,
 *              registro de usuarios, menús principales y navegación entre diferentes
 *              roles de usuario. Proporciona una experiencia de usuario coherente
 *              y amigable a través de consola.
 * 
 * Nombre de los Integrantes:
 * Javier Lee Liang
 * Paulo César Herrera Arias
 * José Emilio Alvarado Mendez
 * Josué Santiago Hidalgo Sandoval
 */
package Console;

import Admin.AdminService;
import Services.AuthenticationService;
import User.UserConsole;
import User.UserRole;
import User.UserService;

/**
 * Interfaz principal de consola del sistema
 * 
 * Esta clase actúa como punto de entrada principal para la interfaz de consola,
 * gestionando:
 * - Autenticación y registro de usuarios
 * - Navegación entre menús según el rol del usuario
 * - Gestión de sesiones de usuario
 * - Cambio de contraseñas
 * - Coordinación entre consolas de administrador y usuario regular
 * 
 * Proporciona una experiencia de usuario fluida y consistente.
 */
public class ConsoleUI {
    
    private final AuthenticationService authService;
    private final AdminService adminService;
    private final UserService userService;
    private final AdminConsole adminConsole;
    private final UserConsole userConsole;
    
    /**
     * Constructor de la interfaz principal de consola
     * 
     * @param authService Servicio de autenticación para gestión de sesiones
     * @param adminService Servicio de administración para funcionalidades de admin
     * @param userService Servicio de usuario para funcionalidades de usuario regular
     * 
     * Notas:
     * - Inicializa automáticamente las consolas específicas por rol
     * - Establece las dependencias necesarias para toda la aplicación
     */
    public ConsoleUI(AuthenticationService authService, AdminService adminService, UserService userService) {
        this.authService = authService;
        this.adminService = adminService;
        this.userService = userService;
        this.adminConsole = new AdminConsole(adminService);
        this.userConsole = new UserConsole(userService);
    }
    
    /**
     * Inicia el bucle principal de la aplicación
     * 
     * Características:
     * - Muestra mensaje de bienvenida con limpieza de consola
     * - Bucle infinito hasta que el usuario decida salir
     * - Alterna entre menú de autenticación y menú principal
     * - Maneja la transición entre estados autenticados y no autenticados
     * - Mensaje de despedida al salir
     */
    public void start() {
        MenuUtil.clearConsole();
        System.out.println("🚗 Welcome to the Route Planning System 🚗");
        System.out.println("Energy Transition Vehicle Route Management");
        
        boolean running = true;
        while (running) {
            if (!authService.isAuthenticated()) {
                running = showLoginMenu();
            } else {
                running = showMainMenu();
            }
        }
        
        System.out.println("\nThank you for using the Route Planning System! 👋");
    }
    
    /**
     * Muestra el menú de autenticación y registro
     * 
     * @return true para continuar la aplicación, false para salir
     * 
     * Opciones disponibles:
     * - Login: Iniciar sesión con credenciales existentes
     * - Register as User: Registrarse como usuario regular
     * - Register as Admin: Registrarse como administrador
     * - Exit: Salir de la aplicación
     */
    private boolean showLoginMenu() {
        int choice = MenuUtil.displayMenu(
            "🔐 Authentication Menu",
            "Login",
            "Register as User",
            "Register as Admin"
        );
        
        switch (choice) {
            case 0 -> {
                return handleLogin();
            }
            case 1 -> {
                return handleRegister(UserRole.USER);
            }
            case 2 -> {
                return handleRegister(UserRole.ADMIN);
            }
            case -1 -> {
                return false; // Exit
            }
            default -> {
                MenuUtil.showError("Invalid option. Please try again.");
                return true;
            }
        }
    }
    
    /**
     * Muestra el menú principal después de la autenticación
     * 
     * @return true para continuar la aplicación, false para salir
     * 
     * Características:
     * - Muestra información personalizada del usuario (nombre y rol)
     * - Opciones dinámicas según el rol del usuario
     * - Acceso a paneles específicos por rol
     * - Gestión de contraseñas y cierre de sesión
     */
    private boolean showMainMenu() {
        String userName = authService.getCurrentUser().getUsername();
        String userRole = authService.getCurrentUser().getRole().getDisplayName();
        
        int choice = MenuUtil.displayMenu(
            String.format("🏠 Main Menu - Welcome %s (%s)", userName, userRole),
            authService.isCurrentUserAdmin() ? "Admin Panel" : "User Dashboard",
            "Change Password",
            "Logout"
        );
        
        switch (choice) {
            case 0 -> {
                if (authService.isCurrentUserAdmin()) {
                    adminConsole.showAdminMenu();
                } else {
                    userConsole.showUserMenu();
                }
                return true;
            }
            case 1 -> {
                return handleChangePassword();
            }
            case 2 -> {
                authService.logout();
                MenuUtil.showInfo("Logged out successfully.");
                return true;
            }
            case -1 -> {
                return false; // Exit
            }
            default -> {
                MenuUtil.showError("Invalid option. Please try again.");
                return true;
            }
        }
    }
    
    /**
     * Maneja el proceso de inicio de sesión del usuario
     * 
     * @return true para continuar la aplicación
     * 
     * Proceso:
     * - Solicita nombre de usuario y contraseña
     * - Valida credenciales con el servicio de autenticación
     * - Muestra mensaje de éxito o error según el resultado
     * - Valida que los campos no estén vacíos
     */
    private boolean handleLogin() {
        System.out.println("\n🔑 User Login");
        
        String username = MenuUtil.getNonEmptyStringInput("Username: ");
        String password = MenuUtil.getNonEmptyStringInput("Password: ");
        
        if (authService.login(username, password)) {
            MenuUtil.showSuccess("Login successful! Welcome, " + username + "!");
        } else {
            MenuUtil.showError("Invalid username or password.");
        }
        
        return true;
    }
    
    /**
     * Maneja el proceso de registro de usuarios
     * 
     * @param role El rol para el cual registrar al usuario
     * @return true para continuar la aplicación
     * 
     * Proceso:
     * - Solicita nombre de usuario, contraseña y confirmación
     * - Valida que las contraseñas coincidan
     * - Registra el usuario con el rol especificado
     * - Muestra mensaje de éxito o error según el resultado
     * - Valida que los campos no estén vacíos
     */
    private boolean handleRegister(UserRole role) {
        System.out.println("\n📝 User Registration - " + role.getDisplayName());
        
        String username = MenuUtil.getNonEmptyStringInput("Username: ");
        String password = MenuUtil.getNonEmptyStringInput("Password: ");
        String confirmPassword = MenuUtil.getNonEmptyStringInput("Confirm Password: ");
        
        if (!password.equals(confirmPassword)) {
            MenuUtil.showError("Passwords do not match.");
            return true;
        }
        
        if (authService.register(username, password, role) != null) {
            MenuUtil.showSuccess("Registration successful! You can now log in.");
        } else {
            MenuUtil.showError("Registration failed. Username may already exist.");
        }
        
        return true;
    }
    
    /**
     * Maneja el proceso de cambio de contraseña
     * 
     * @return true para continuar la aplicación
     * 
     * Proceso:
     * - Solicita contraseña actual, nueva contraseña y confirmación
     * - Valida que la nueva contraseña y confirmación coincidan
     * - Verifica la contraseña actual antes de permitir el cambio
     * - Muestra mensaje de éxito o error según el resultado
     * - Valida que los campos no estén vacíos
     */
    private boolean handleChangePassword() {
        System.out.println("\n🔒 Change Password");
        
        String oldPassword = MenuUtil.getNonEmptyStringInput("Current Password: ");
        String newPassword = MenuUtil.getNonEmptyStringInput("New Password: ");
        String confirmPassword = MenuUtil.getNonEmptyStringInput("Confirm New Password: ");
        
        if (!newPassword.equals(confirmPassword)) {
            MenuUtil.showError("New passwords do not match.");
            return true;
        }
        
        if (authService.changePassword(oldPassword, newPassword)) {
            MenuUtil.showSuccess("Password changed successfully!");
        } else {
            MenuUtil.showError("Failed to change password. Current password may be incorrect.");
        }
        
        return true;
    }
}
