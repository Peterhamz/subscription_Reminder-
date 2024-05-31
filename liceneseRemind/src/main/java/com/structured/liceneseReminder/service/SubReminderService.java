package com.structured.liceneseReminder.service;

import com.structured.liceneseReminder.dto.SubDto;
import com.structured.liceneseReminder.dto.UserDto;
import com.structured.liceneseReminder.model.Department;
import com.structured.liceneseReminder.model.SubReminder;
import org.quartz.SchedulerException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SubReminderService {
    SubDto createReminder(SubDto subDto) throws SchedulerException, InterruptedException;
    List<SubReminder> getAllSubscription();
    void sendEmail();

    SubReminder getSubById(Long id);
    SubReminder updateSub(Long subId, SubDto subDto);
    UserDto createUser(UserDto userDto);

    List<Department> getAllDepartment();
    Page<SubReminder> allPaginated(int pageNumber, int pageSize);

    Page<SubReminder> pendingPaginated(int pageNumber, int pageSize);

    Page<SubReminder> activePaginated(int pageNumber, int pageSize);

    Page<SubReminder> overduePaginated(int pageNumber, int pageSize);
}
