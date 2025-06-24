/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Console;

import Admin.AdminService;
import Services.AuthenticationService;
import User.UserConsole;
import User.UserRole;
import User.UserService;

/**
 *
 * @author JE
 */
public class ConsoleUI {
    
    private final AuthenticationService authService;
    private final AdminService adminService;
    private final UserService userService;
    private final AdminConsole adminConsole;
    private final UserConsole userConsole;
    
    public ConsoleUI(AuthenticationService authService, AdminService adminService, UserService userService) {
        this.authService = authService;
        this.adminService = adminService;
        this.userService = userService;
        this.adminConsole = new AdminConsole(adminService);
        this.userConsole = new UserConsole(userService);
    }
    
    /**
     * Starts the main application loop
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
     * Shows the login/registration menu
     * @return true to continue, false to exit
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
     * Shows the main menu after authentication
     * @return true to continue, false to exit
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
     * Handles user login
     * @return true to continue
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
     * Handles user registration
     * @param role the role to register as
     * @return true to continue
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
     * Handles password change
     * @return true to continue
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
