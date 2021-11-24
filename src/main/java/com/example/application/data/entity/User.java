package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;
import com.example.application.data.entity.enums.Role;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name="user")
public class User extends AbstractEntity implements Serializable {

    @NotNull
    @NotEmpty
    @Column(unique = true)
    private String username;
    @NotNull
    @NotEmpty
    private String passwordSalt;
    @NotNull
    @NotEmpty
    private String passwordHash;
    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role;
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    @NotFound(action = NotFoundAction.IGNORE)
    private Doctor doctor = null;

    public Doctor getDoctor() {
        return doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    @NotFound(action = NotFoundAction.IGNORE)
    private Patient patient = null;


    public User() {
    }

    public User(String username, String password, Role role) {
        this.username = username;
        this.role = role;
        //cypher
        this.passwordSalt = RandomStringUtils.random(32);
        this.passwordHash = DigestUtils.sha1Hex(password + passwordSalt);
    }

    public boolean checkPassword(String password) {
        return DigestUtils.sha1Hex(password + passwordSalt).equals(passwordHash);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }
    //admin13 pass: pass
    public void setPassword(String password) {
        this.passwordSalt = RandomStringUtils.random(32);
        this.passwordHash = DigestUtils.sha1Hex(password + passwordSalt);
    }

    public String getPasswordHash() {
        return passwordHash;
    }


    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}
