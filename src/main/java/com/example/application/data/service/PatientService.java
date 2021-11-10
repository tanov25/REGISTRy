package com.example.application.data.service;

import com.example.application.data.entity.Patient;
import com.example.application.data.entity.User;
import com.example.application.data.service.repo.PatientRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Transactional
public class PatientService {
    private static final Logger LOGGER = Logger.getLogger(PatientService.class.getName());
    private PatientRepository PatientRepository;
    public Patient nullPat(){
        List<Patient> find = findAll();
        for(Patient f: find){
            if(f.getName().equals("null")){
                return f;
            }
        }
       return null;
    }

    public Patient findByUser(User user){return PatientRepository.findByUser(user);}

    public Patient findByPassport(String passport) {return PatientRepository.findByPassport(passport);}

    public PatientService(PatientRepository PatientRepository) {
        this.PatientRepository = PatientRepository;
    }

    public List<Patient> findAll() {
        return PatientRepository.findAll();
    }

    public long count() {
        return PatientRepository.count();
    }

    public void delete(Patient Patient) {
        PatientRepository.delete(Patient);
    }

    public void save(Patient Patient) throws DataIntegrityViolationException {
        if (Patient == null) {
            LOGGER.log(Level.SEVERE,
                    "Patient is null. Are you sure you have connected your form to the application?");
            return;
        }
        PatientRepository.save(Patient);
    }
}
