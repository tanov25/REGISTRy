package com.example.application.data.service;

import com.example.application.data.entity.Doctor;
import com.example.application.data.entity.Patient;
import com.example.application.data.entity.Schedule;
import com.example.application.data.service.repo.DoctorRepository;
import com.example.application.data.service.repo.PatientRepository;
import com.example.application.data.service.repo.ScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Transactional
public class ScheduleService {
    private static final Logger LOGGER = Logger.getLogger(ScheduleService.class.getName());
    private ScheduleRepository ScheduleRepository;
    private DoctorRepository doctorRepository;
    private PatientRepository patientRepository;
    


    public ScheduleService(ScheduleRepository ScheduleRepository, DoctorRepository doctorRepository, PatientRepository patientRepository) {
        this.ScheduleRepository = ScheduleRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    public List<Schedule> findAll() {
        return ScheduleRepository.findAll();
    }

    public List<Schedule> findAll(Doctor doctor) {
        if(doctor == null) {
            return ScheduleRepository.findAll();
        } else  {
            return  ScheduleRepository.getByDoctor(doctor);
        }
    }

    public List<Schedule> findAll(Patient patient) {
        if(patient == null) {
            return ScheduleRepository.findAll();
        } else  {
            return  ScheduleRepository.getByPatient(patient);
        }
    }


    public long count() {
        return ScheduleRepository.count();
    }
    @Transactional
    public void delete(Schedule Schedule) {
        ScheduleRepository.deleteSch(Schedule);
    }
    @Transactional
    public void deleteAll() {
        ScheduleRepository.deleteAllAll();
    }
    @Transactional
    public void delete(Doctor doctor) {
        ScheduleRepository.deleteDoc(doctor);
    }

    public void save(Schedule Schedule) {
        if (Schedule == null) {
            LOGGER.log(Level.SEVERE,
                    "Schedule is null. Are you sure you have connected your form to the application?");
            return;
        }
        ScheduleRepository.save(Schedule);
    }
    @PostConstruct
    public void populateTestData() {


    }


}
