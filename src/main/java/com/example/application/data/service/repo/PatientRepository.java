package com.example.application.data.service.repo;

import com.example.application.data.entity.Patient;
import com.example.application.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {
    Patient findByUser(User user);
    Patient findByPassport(String passport);
}
