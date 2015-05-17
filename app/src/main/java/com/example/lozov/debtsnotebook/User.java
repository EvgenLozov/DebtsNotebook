package com.example.lozov.debtsnotebook;

/**
 * Created by Yevhen on 2015-05-15.
 */
public class User {
    private String username;
    private String password;
    private String email;

    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
