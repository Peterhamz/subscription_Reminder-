package com.structured.liceneseReminder.scheduler;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DailyJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        // Get the fire time of the trigger
        Date fireTime = jobExecutionContext.getFireTime();

        // Get the expiry date from the JobDataMap
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        LocalDate expiryDate = (LocalDate) jobDataMap.get("expiryDate");

        // Calculate the number of days until the expiry date
        long daysUntilExpiry = ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);

        // Check if it's 30 days before the expiry date
        if (daysUntilExpiry == 30) {
            System.out.println("30 days to go!");
        }

        // Check if it's 10 days before the expiry date
        if (daysUntilExpiry == 10) {
            System.out.println("10 days to go!");
        }

        // Check if it's the expiry date
        if (fireTime.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate().isEqual(expiryDate)) {
            System.out.println("I am done!");
        }
    }
}
