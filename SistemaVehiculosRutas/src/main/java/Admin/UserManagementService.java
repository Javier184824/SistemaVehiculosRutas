/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Admin;

import Binary.FileConstants;
import Interfaces.DataManager;
import Interfaces.SerializationException;
import User.User;
import User.UserRole;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JE
 */
public class UserManagementService {
    
    private final DataManager dataManager;
    
    public UserManagementService(DataManager dataManager) {
        this.dataManager = dataManager;
    }
    
    /**
     * Gets all users
     * @return list of all users
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
     * Creates a new user
     * @param user the user to create
     * @return true if created successfully
     */
    public boolean createUser(User user) {
        if (user == null) {
            return false;
        }
        
        try {
            List<User> users = getAllUsers();
            
            // Check for duplicate usernames
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
     * Updates an existing user
     * @param userId the ID of the user to update
     * @param updatedUser the updated user information
     * @return true if updated successfully
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
     * Deletes a user
     * @param userId the ID of the user to delete
     * @return true if deleted successfully
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
     * Changes a user's role
     * @param userId the user ID
     * @param newRole the new role
     * @return true if role changed successfully
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
     * Finds a user by ID
     * @param userId the user ID
     * @return the user, or null if not found
     */
    public User findUserById(String userId) {
        return getAllUsers().stream()
            .filter(user -> userId.equals(user.getId()))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Finds a user by username
     * @param username the username
     * @return the user, or null if not found
     */
    public User findUserByUsername(String username) {
        return getAllUsers().stream()
            .filter(user -> username.equals(user.getUsername()))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Gets users by role
     * @param role the role to filter by
     * @return list of users with the specified role
     */
    public List<User> getUsersByRole(UserRole role) {
        return getAllUsers().stream()
            .filter(user -> role.equals(user.getRole()))
            .toList();
    }
    
    /**
     * Gets user statistics
     * @return formatted user statistics
     */
    public String getUserStatistics() {
        List<User> users = getAllUsers();
        long adminCount = users.stream().filter(u -> u.getRole() == UserRole.ADMIN).count();
        long userCount = users.stream().filter(u -> u.getRole() == UserRole.USER).count();
        
        return String.format("Total Users: %d%nAdmins: %d%nRegular Users: %d", 
                           users.size(), adminCount, userCount);
    }
}
