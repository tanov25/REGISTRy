package com.example.application.data.service;

import com.example.application.data.entity.Lab;
import com.example.application.data.service.repo.LabRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class LabService {
    private static final Logger LOGGER = Logger.getLogger(LabService.class.getName());
    private LabRepository LabRepository;


    public LabService(LabRepository LabRepository) {
        this.LabRepository = LabRepository;
    }

    public List<Lab> findAll() {
        return LabRepository.findAll();
    }

    public long count() {
        return LabRepository.count();
    }

    public void delete(Lab Lab) {
        LabRepository.delete(Lab);
    }

    public void save(Lab Lab) {
        if (Lab == null) {
            LOGGER.log(Level.SEVERE,
                    "Lab is null. Are you sure you have connected your form to the application?");
            return;
        }
        LabRepository.save(Lab);
    }
}
