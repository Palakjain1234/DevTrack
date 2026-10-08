package com.devrelease.repository;

import com.devrelease.model.Project;
import com.devrelease.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByOwner(User owner);
    List<Project> findByMembersContaining(User user);

    @Query("SELECT p FROM Project p WHERE p.owner = :user OR :user MEMBER OF p.members")
    List<Project> findAllAccessibleByUser(@Param("user") User user);
}
