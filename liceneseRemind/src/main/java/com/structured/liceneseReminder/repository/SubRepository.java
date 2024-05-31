package com.structured.liceneseReminder.repository;

import com.structured.liceneseReminder.enums.Status;
import com.structured.liceneseReminder.model.SubReminder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SubRepository extends JpaRepository<SubReminder,Long> {
    @Query(value = "select  * from Sub_reminder",nativeQuery = true)
    Page<SubReminder>getInfo(Pageable pageable);

    @Query(value = "SELECT * FROM sub_reminder where status = '0'",nativeQuery = true)
    Page<SubReminder>getActiveInfo(Pageable pageable);

    @Query(value = "SELECT * FROM sub_reminder where status = '1'",nativeQuery = true)
    Page<SubReminder>getPendingInfo(Pageable pageable);

    @Query(value = "SELECT * FROM sub_reminder where status = '2'",nativeQuery = true)
    Page<SubReminder>getExpiredInfo(Pageable pageable);

   // List<Sub_reminder> findAllByStatus(String status);

    long countByStatus (Status status);
}