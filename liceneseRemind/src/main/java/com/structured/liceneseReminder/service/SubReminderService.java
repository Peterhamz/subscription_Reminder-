package com.structured.liceneseReminder.service;

import com.structured.liceneseReminder.dto.SubDto;
import com.structured.liceneseReminder.model.Sub_reminder;

import java.util.List;

public interface SubReminderService {
    SubDto createReminder(SubDto subDto);
    List<Sub_reminder> getAllSubscription();

}
