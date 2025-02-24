package com.nyanmyohtet.springbootstarter.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nyanmyohtet.springbootstarter.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"id"})})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @JsonIgnore
    // BCrypt typically produce fixed-length outputs.
    @Column(nullable = false, length = 60)
    private String passwordHash;

    private Set<Role> authorities;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    @Column(nullable = false)
    @Builder.Default
    private boolean enabled = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    @SuppressWarnings("unused")
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
