package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;
import com.example.application.data.entity.exception.LabScheduleException;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="lab")
public class Lab extends AbstractEntity implements Serializable {
    private String name;
    private Integer open;
    private Integer close;
    private String days;

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public Lab() {
    }

    public Lab(String name, Integer open, Integer close, String days) throws LabScheduleException {
        this.name = name;
        this.open = open;
        this.close = close;
        if(open > close){
            throw new LabScheduleException("Open cannot be after close");
        }
        this.days = days;
    }

    public Lab(String name, Integer open, Integer close) throws LabScheduleException {
        this.name = name;
        this.open = open;
        this.close = close;
        if(open > close){
            throw new LabScheduleException("Open cannot be after close");
        }
        this.days = "пн-вс";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOpen() {
        return open;
    }

    public void setOpen(Integer open) {
        this.open = open;
    }

    public Integer getClose() {
        return close;
    }

    public void setClose(Integer close) {
        this.close = close;
    }
}
