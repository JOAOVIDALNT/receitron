package com.joaovidal.receitron.adapter.out.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserEntity implements UserDetails {
    @Id
    @Column(nullable = false)
    private UUID id;
    @Email
    @NotBlank
    @Column(unique = true, length = 100, nullable = false)
    private String email;
    @NotBlank
    @Column(nullable = false)
    private String password;
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private Set<String> roles = new HashSet<>();
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private Set<String> favoriteCultures = new HashSet<>();
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private Set<String> preferences = new HashSet<>();
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private Set<String> restrictions = new HashSet<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return email;
    }
}
