package com.devrelease.repository;

import com.devrelease.model.Environment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EnvironmentRepository extends JpaRepository<Environment, Long> {
    List<Environment> findByProjectId(Long projectId);
}
