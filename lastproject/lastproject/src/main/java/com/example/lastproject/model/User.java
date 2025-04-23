package com.example.lastproject.model;

import com.example.lastproject.model.enums.Role;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name="Users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = true,unique = false)
    private String password;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private Role role = Role.User;

    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime creationDate;

    public User(long id, String email, String password, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User() {

    }

    public User(long id, String email, String password, Role role, LocalDateTime creationDate) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.creationDate = creationDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return null;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
    @PrePersist
    protected void onCreate() {
        this.creationDate = LocalDateTime.now();
    }
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", creationDate=" + creationDate +
                '}';
    }
}
