package com.joaovidal.receitron.domain.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class User {

    private UUID id;
    private String email;
    private String password;
    private Set<String> roles = new HashSet<>();

    public User(UUID id, String email, String password, Set<String> roles) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public User() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
