package com.structured.liceneseReminder.repository;

import com.structured.liceneseReminder.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department>getDepartmentById(Long id);
}