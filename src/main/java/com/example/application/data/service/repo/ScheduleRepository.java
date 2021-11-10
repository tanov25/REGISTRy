package com.example.application.data.service.repo;

import com.example.application.data.entity.Doctor;
import com.example.application.data.entity.Patient;
import com.example.application.data.entity.Schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
public List<Schedule> getByDoctor(Doctor doctor);
public List<Schedule> getByPatient(Patient patient);
    @Transactional
    @Modifying
    @Query("delete from Schedule b where b.doctor=:doctor")
    void deleteDoc(@Param("doctor") Doctor doctor);
    @Transactional
    @Modifying
    @Query("delete from Schedule b where b=:sch")
    void deleteSch(@Param("sch") Schedule schedule);
    @Transactional
    @Modifying
    @Query("delete from Schedule")
    void deleteAllAll();
}
