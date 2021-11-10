package com.example.application.data.service;

import com.example.application.data.entity.Doctor;
import com.example.application.data.entity.User;
import com.example.application.data.service.repo.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
@Service
public class
DoctorService {
    private static final Logger LOGGER = Logger.getLogger(DoctorService.class.getName());
    private com.example.application.data.service.repo.DoctorRepository DoctorRepository;
    

    public DoctorService(DoctorRepository DoctorRepository) {
        this.DoctorRepository = DoctorRepository;
    }

    public List<Doctor> findAll() {
        return DoctorRepository.findAll();
    }

    public long count() {
        return DoctorRepository.count();
    }

    public void delete(Doctor Doctor) {
        DoctorRepository.delete(Doctor);
    }

    public Doctor findByUser(User user){return DoctorRepository.findByUser(user);}

    public void save(Doctor Doctor) {
        if (Doctor == null) {
            LOGGER.log(Level.SEVERE,
                    "Doctor is null. Are you sure you have connected your form to the application?");
            return;
        }
        DoctorRepository.save(Doctor);
    }

}
