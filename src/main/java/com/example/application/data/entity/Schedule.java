package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
@Entity
@Table(name="schedule")
public class Schedule extends AbstractEntity implements Serializable {
    private Date date;

    public Schedule(Date date, Doctor doctor) {
        this.date = date;
        this.doctor = doctor;
        this.patient = null;
        free = false;
    }

    @ManyToOne(fetch = FetchType.EAGER )
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id")
    private Patient patient;
    private Boolean free;

    public Schedule() {
    }

    public Boolean getFree() {
        return free;
    }

    public void setFree(Boolean free) {
        this.free = free;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Schedule(Date date, Doctor doctor, Patient patient) {
        this.date = date;
        this.doctor = doctor;
        this.patient = patient;
        if(this.patient == null) free = false;
        else free = true;
    }
}
