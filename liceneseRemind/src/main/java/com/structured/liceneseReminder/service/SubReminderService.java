package com.structured.liceneseReminder.service;

import com.structured.liceneseReminder.dto.SubDto;
import com.structured.liceneseReminder.model.Sub_reminder;
import org.quartz.SchedulerException;

import java.util.List;

public interface SubReminderService {
    SubDto createReminder(SubDto subDto) throws SchedulerException, InterruptedException;
    List<Sub_reminder> getAllSubscription();
    void sendEmail();

}
