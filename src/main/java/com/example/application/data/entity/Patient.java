package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="patient")
public class Patient extends AbstractEntity implements Serializable {
    @NotNull
    @NotEmpty
    private String surname;
    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    @NotEmpty
    private String otchestvo;
    private Date birthdate;
    @NotNull
    @NotEmpty
    @Column(unique = true)
    private String passport;

    public Set<Schedule> getVisits() {
        return visits;
    }

    @OneToOne
    @JoinColumn(name = "username")
    private User user;

    @OneToMany(mappedBy = "patient", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @NotFound(action = NotFoundAction.IGNORE)
    private Set<Schedule> visits;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Patient(){

    }
    public Patient(User user) {
        this.user = user;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOtchestvo() {
        return otchestvo;
    }

    public void setOtchestvo(String otchestvo) {
        this.otchestvo = otchestvo;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public Patient(String surname, String name, String otchestvo, Date birthdate, String passport, User user) {
        this.surname = surname;
        this.name = name;
        this.otchestvo = otchestvo;
        this.birthdate = birthdate;
        this.passport = passport;
        this.user = user;
    }

    public String toString(){
        return this.surname + " " + this.name + " " + this.otchestvo;
    }

}
