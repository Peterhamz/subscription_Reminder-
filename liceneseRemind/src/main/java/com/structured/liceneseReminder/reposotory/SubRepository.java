package com.structured.liceneseReminder.reposotory;

import com.structured.liceneseReminder.model.Sub_reminder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubRepository extends JpaRepository<Sub_reminder,Long> {
}
