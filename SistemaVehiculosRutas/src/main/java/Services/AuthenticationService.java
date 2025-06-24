/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Services;

import Binary.FileConstants;
import Interfaces.DataManager;
import Interfaces.SerializationException;
import User.User;
import User.UserRole;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author JE
 */
public class AuthenticationService {
    
    private final DataManager dataManager;
    private Session currentSession;
    
    public AuthenticationService(DataManager dataManager) {
        this.dataManager = dataManager;
    }
    
    /**
     * Authenticates a user with username and password
     * @param username the username
     * @param password the password
     * @return true if authentication successful
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
     * Registers a new user
     * @param username the username
     * @param password the password
     * @param role the user role
     * @return the created user, or null if registration failed
     */
    public User register(String username, String password, UserRole role) {
        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            return null;
        }
        
        try {
            List<User> users = dataManager.loadList(FileConstants.USERS_FILE, User::new);
            
            // Check if username already exists
            boolean usernameExists = users.stream()
                .anyMatch(user -> username.equals(user.getUsername()));
            
            if (usernameExists) {
                return null; // Username already taken
            }
            
            // Create new user
            User newUser = new User(username, password, role);
            users.add(newUser);
            
            // Save updated user list
            dataManager.saveList(users, FileConstants.USERS_FILE);
            
            return newUser;
            
        } catch (SerializationException e) {
            System.err.println("Error during user registration: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Logs out the current user
     */
    public void logout() {
        currentSession = null;
    }
    
    /**
     * Gets the current authenticated user
     * @return the current user, or null if not authenticated
     */
    public User getCurrentUser() {
        return currentSession != null ? currentSession.getUser() : null;
    }
    
    /**
     * Gets the current session
     * @return the current session, or null if not authenticated
     */
    public Session getCurrentSession() {
        return currentSession;
    }
    
    /**
     * Checks if a user is currently authenticated
     * @return true if authenticated
     */
    public boolean isAuthenticated() {
        return currentSession != null;
    }
    
    /**
     * Checks if the current user is an admin
     * @return true if current user is admin
     */
    public boolean isCurrentUserAdmin() {
        return currentSession != null && currentSession.isAdmin();
    }
    
    /**
     * Updates the current user's information
     * @param updatedUser the updated user information
     * @return true if update successful
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
                    
                    // Update current session
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
     * Changes the current user's password
     * @param oldPassword the current password
     * @param newPassword the new password
     * @return true if password change successful
     */
    public boolean changePassword(String oldPassword, String newPassword) {
        if (currentSession == null || oldPassword == null || newPassword == null) {
            return false;
        }
        
        User currentUser = currentSession.getUser();
        if (!oldPassword.equals(currentUser.getPassword())) {
            return false; // Old password doesn't match
        }
        
        currentUser.setPassword(newPassword);
        return updateCurrentUser(currentUser);
    }
}
