package com.example.application.data.service.repo;

import com.example.application.data.AbstractEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbstractRepository extends JpaRepository<AbstractEntity, Integer> {
}
