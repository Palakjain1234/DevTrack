package com.devrelease.repository;

import com.devrelease.enums.ReleaseStatus;
import com.devrelease.model.Release;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReleaseRepository extends JpaRepository<Release, Long> {
    List<Release> findByProjectId(Long projectId);
    List<Release> findByProjectIdAndStatus(Long projectId, ReleaseStatus status);
    long countByProjectIdIn(List<Long> projectIds);
}
