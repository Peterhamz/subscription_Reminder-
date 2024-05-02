package com.structured.liceneseReminder.service;

import com.structured.liceneseReminder.dto.SubDto;
import com.structured.liceneseReminder.model.Sub_reminder;
import org.quartz.SchedulerException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SubReminderService {
    SubDto createReminder(SubDto subDto) throws SchedulerException, InterruptedException;
    List<Sub_reminder> getAllSubscription();
    void sendEmail();
    Page<Sub_reminder> allPaginated(int pageNumber, int pageSize);

    Page<Sub_reminder> pendingPaginated(int pageNumber, int pageSize);

    Page<Sub_reminder> activePaginated(int pageNumber, int pageSize);

    Page<Sub_reminder> overduePaginated(int pageNumber, int pageSize);
}
