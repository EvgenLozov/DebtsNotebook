package com.example.lozov.debtsnotebook;

/**
 * Created by Yevhen on 2015-05-15.
 */
public class User {
    private String name;
    private String username;
    private int age;
    private String password;

    public User(String name, String username, int age, String password) {
        this.name = name;
        this.username = username;
        this.age = age;
        this.password = password;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public int getAge() {
        return age;
    }

    public String getPassword() {
        return password;
    }
}
