package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;
import com.example.application.data.entity.enums.Speciality;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="doctor")
public class Doctor extends AbstractEntity implements Cloneable, Serializable {
    @NotNull
    @NotEmpty
private String surname;
    @NotNull
    @NotEmpty
private String name;
    @NotNull
    @NotEmpty
private String otchestvo;
@Enumerated(EnumType.STRING)
private Speciality speciality;
private String experience;
@OneToOne
@JoinColumn(name = "username")
private User user;
   @OneToMany(mappedBy = "doctor", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.REFRESH)
   @NotFound(action = NotFoundAction.IGNORE)
   private Set<Schedule> visits;

    public String getExperience() {
        return experience;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Schedule> getVisits() {
        return visits;
    }

    public Doctor() {
    }

    public Doctor(User user) {
        this.user = user;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public Doctor(String surname, String name, String otchestvo, Speciality speciality, String experience, User user) {
      //  super();
        this.surname = surname;
        this.name = name;
        this.otchestvo = otchestvo;
        this.speciality = speciality;
        this.experience = experience;
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

    public Speciality getSpeciality() {
        return speciality;
    }

    public void setSpeciality(Speciality speciality) {
        this.speciality = speciality;
    }

    public String toString(){
        return this.surname + " " + this.name + " " + this.otchestvo + " " + this.speciality + " " + this.experience;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Doctor doctor = (Doctor) o;
        return Objects.equals(surname, doctor.surname) && Objects.equals(name, doctor.name) && Objects.equals(otchestvo, doctor.otchestvo) && speciality == doctor.speciality && Objects.equals(experience, doctor.experience) && Objects.equals(user, doctor.user) && Objects.equals(visits, doctor.visits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), surname, name, otchestvo, speciality, experience, user, visits);
    }
}
