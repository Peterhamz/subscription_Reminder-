package com.structured.liceneseReminder.repository;

import com.structured.liceneseReminder.model.Department;
import com.structured.liceneseReminder.model.SubReminder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department>getDepartmentById(Long id);
   // Page<Department>getAllByDepartment(Pageable pageable);
    Optional<Department>getDepartmentByEmail(String email);
    Optional<Department>getDepartmentByDepartment(String department);
}
