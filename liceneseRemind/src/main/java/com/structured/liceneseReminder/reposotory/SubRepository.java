package com.structured.liceneseReminder.reposotory;

import com.structured.liceneseReminder.enums.Status;
import com.structured.liceneseReminder.model.Sub_reminder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SubRepository extends JpaRepository<Sub_reminder,Long> {
    @Query(value = "select  * from Sub_reminder",nativeQuery = true)
    Page<Sub_reminder>getInfo(Pageable pageable);

    @Query(value = "SELECT * FROM sub_reminder where status = '0'",nativeQuery = true)
    Page<Sub_reminder>getActiveInfo(Pageable pageable);

    @Query(value = "SELECT * FROM sub_reminder where status = '1'",nativeQuery = true)
    Page<Sub_reminder>getPendingInfo(Pageable pageable);

    @Query(value = "SELECT * FROM sub_reminder where status = '2'",nativeQuery = true)
    Page<Sub_reminder>getExpiredInfo(Pageable pageable);

   // List<Sub_reminder> findAllByStatus(String status);


    long countByStatus (Status status);
}