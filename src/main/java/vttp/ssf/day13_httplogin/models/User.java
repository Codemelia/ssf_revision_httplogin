package vttp.ssf.day13_httplogin.models;

public class User {
    
    private String username;
    private String password;
    private boolean accountLocked;
    private int failedAttempts;

    public User() {
        this.username = "";
        this.password = "";
        this.accountLocked = false;
        this.failedAttempts = 0;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    @Override
    public String toString() {
        return "User [username=" + username + ", password=" + password + ", accountLocked=" + accountLocked
                + ", failedAttempts=" + failedAttempts + "]";
    }

}
