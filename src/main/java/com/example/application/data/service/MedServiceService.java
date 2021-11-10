package com.example.application.data.service;

import com.example.application.data.entity.MedService;
import com.example.application.data.service.repo.MedServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class MedServiceService {
    private static final Logger LOGGER = Logger.getLogger(MedServiceService.class.getName());
    private com.example.application.data.service.repo.MedServiceRepository MedServiceRepository;


    public MedServiceService(MedServiceRepository MedServiceRepository) {
        this.MedServiceRepository = MedServiceRepository;
    }

    public List<MedService> findAll() {
        return MedServiceRepository.findAll();
    }

    public long count() {
        return MedServiceRepository.count();
    }

    public void delete(MedService MedService) {
        MedServiceRepository.delete(MedService);
    }

    public void save(MedService MedService) {
        if (MedService == null) {
            LOGGER.log(Level.SEVERE,
                    "MedService is null. Are you sure you have connected your form to the application?");
            return;
        }
        MedServiceRepository.save(MedService);
    }
}
