package com.devrelease.repository;

import com.devrelease.enums.DeploymentStatus;
import com.devrelease.model.Deployment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface DeploymentRepository extends JpaRepository<Deployment, Long> {

    List<Deployment> findByReleaseId(Long releaseId);
    List<Deployment> findByEnvironmentId(Long environmentId);

    boolean existsByReleaseIdAndStatus(Long releaseId, DeploymentStatus status);

    @Query("SELECT COUNT(d) FROM Deployment d WHERE CAST(d.startedAt AS date) = :today")
    long countDeploymentsToday(@Param("today") LocalDate today);

    @Query("SELECT COUNT(d) FROM Deployment d WHERE d.status = 'SUCCESS'")
    long countSuccessful();

    @Query("SELECT d FROM Deployment d WHERE d.release.project.id IN :projectIds ORDER BY d.startedAt DESC")
    List<Deployment> findRecentByProjectIds(@Param("projectIds") List<Long> projectIds);

    @Query("SELECT COUNT(d) FROM Deployment d WHERE d.release.project.id IN :ids AND CAST(d.startedAt AS date) = :today")
    long countDeploymentsTodayForProjects(@Param("ids") List<Long> projectIds, @Param("today") LocalDate today);

    @Query("SELECT COUNT(d) FROM Deployment d WHERE d.release.project.id IN :ids AND d.status = 'SUCCESS'")
    long countSuccessfulForProjects(@Param("ids") List<Long> projectIds);

    @Query("SELECT COUNT(d) FROM Deployment d WHERE d.release.project.id IN :ids")
    long countTotalForProjects(@Param("ids") List<Long> projectIds);
}
