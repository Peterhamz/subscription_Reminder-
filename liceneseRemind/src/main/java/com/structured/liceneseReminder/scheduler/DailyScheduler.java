package com.structured.liceneseReminder.scheduler;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@AllArgsConstructor
@NoArgsConstructor
public class DailyScheduler {

    private LocalDate expiryDate;

    public void startScheduler() throws SchedulerException, InterruptedException {
        // Create a Quartz scheduler
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        // Start the scheduler
        scheduler.start();

        // Define a job
        JobDetail job = JobBuilder.newJob(DailyJob.class)
                .withIdentity("myJob", "group1")
                .build();

        // Pass the expiry date as a parameter using JobDataMap
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("expiryDate", expiryDate);

        // Associate the JobDataMap with the job
        job.getJobDataMap().putAll(jobDataMap);

        // Calculate the number of days until the expiry date
        long daysUntilExpiry = ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);

        // Define a trigger that fires every 24 hours until the expiry date
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("myTrigger", "group1")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInHours(24)
                        .withRepeatCount((int) daysUntilExpiry)) // Repeat until the expiry date
                .build();

        System.out.println("Job about to start");
        // Schedule the job with the trigger
        scheduler.scheduleJob(job, trigger);

        System.out.println("Job started");

        // Keep the program running until the expiry date
        Thread.sleep(daysUntilExpiry * 24 * 60 * 60 * 1000); // Convert days to milliseconds

        // Shut down the scheduler
        scheduler.shutdown();
    }
}
