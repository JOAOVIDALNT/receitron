package com.joaovidal.receitron.domain.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class User {

    private UUID id;
    private String email;
    private String password;
    private Set<String> roles = new HashSet<>();
    private Set<String> favoriteCultures = new HashSet<>();
    private Set<String> preferences = new HashSet<>();
    private Set<String> restrictions = new HashSet<>();

    public User(UUID id, String email, String password, Set<String> roles) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public User(UUID id, String email, String password, Set<String> roles, Set<String> favoriteCultures, Set<String> preferences, Set<String> restrictions) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.favoriteCultures = favoriteCultures;
        this.preferences = preferences;
        this.restrictions = restrictions;
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

    public Set<String> getFavoriteCultures() {
        return favoriteCultures;
    }

    public void setFavoriteCultures(Set<String> favoriteCultures) {
        this.favoriteCultures = favoriteCultures;
    }

    public Set<String> getPreferences() {
        return preferences;
    }

    public void setPreferences(Set<String> preferences) {
        this.preferences = preferences;
    }

    public Set<String> getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(Set<String> restrictions) {
        this.restrictions = restrictions;
    }
}
