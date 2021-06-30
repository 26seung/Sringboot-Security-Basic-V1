package com.cos.security1.model;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "아이디는 필수 값 입니다.")
    private String username;
    @NotBlank(message = "비밀번호는 필수 값 입니다.")
    private String password;
    @Email
    @NotBlank(message = "이메일은 필수 값 입니다.")
    private String email;
    private String role; //ROLE_USER, ROLE_ADMIN

    private String provider;
    private String providerId;

    @CreationTimestamp
    private Timestamp createDate;

    public User(String username, String password, String email, String role, String provider, String providerId, Timestamp createDate) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.createDate = createDate;
    }
}
