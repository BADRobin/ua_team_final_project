package com.final_project.ua_team_final_project.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table (name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "dept_id", nullable = false)
    private Department department;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    private Role role;

    @Column(name = "login", unique = true, nullable = false)
    private String login;

    @Column(name = "password_enc", nullable = false)
    private String passwordEnc;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User user)) return false;
        return Objects.equals(userId, user.userId) && Objects.equals(department, user.department)
                && Objects.equals(name, user.name) && Objects.equals(role, user.role)
                && Objects.equals(login, user.login) && Objects.equals(passwordEnc, user.passwordEnc)
                && Objects.equals(phone, user.phone) && Objects.equals(email, user.email)
                && Objects.equals(createdAt, user.createdAt) && Objects.equals(updatedAt, user.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, department, name, role, login, passwordEnc, phone, email, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + userId +
                ", dept_id=" + department.getDeptId() +
                ", name='" + name + '\'' +
                ", role_id=" + role.getRoleId() +
                ", login='" + login + '\'' +
                ", password_enc='" + passwordEnc + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
