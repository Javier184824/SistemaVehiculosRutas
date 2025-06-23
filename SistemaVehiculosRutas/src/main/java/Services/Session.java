/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Services;

import User.User;

/**
 *
 * @author JE
 */
public class Session {
    
    private final User user;
    private final long loginTime;
    
    public Session(User user) {
        this.user = user;
        this.loginTime = System.currentTimeMillis();
    }
    
    public User getUser() {
        return user;
    }
    
    public long getLoginTime() {
        return loginTime;
    }
    
    public boolean isAdmin() {
        return user != null && user.isAdmin();
    }
    
    @Override
    public String toString() {
        return "Session{user=" + user.getUsername() + ", loginTime=" + loginTime + "}";
    }
}
