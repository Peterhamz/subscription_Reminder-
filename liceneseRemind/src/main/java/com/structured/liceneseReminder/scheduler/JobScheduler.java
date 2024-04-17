package com.structured.liceneseReminder.scheduler;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;


@AllArgsConstructor
@NoArgsConstructor
@Service
public class JobScheduler {

    private Duration duration;


    public void startSchedulerinHours() throws SchedulerException, InterruptedException {
        // Create a Quartz scheduler
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        // Start the scheduler
        scheduler.start();

        // Define a job
        JobDetail job = JobBuilder.newJob(Jobs.class)
                .withIdentity("myJob", "group1")
                .build();

        // Pass the expiry date as a parameter using JobDataMap
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("expiryDate", duration);

        // Associate the JobDataMap with the job
        job.getJobDataMap().putAll(jobDataMap);


        // Define a trigger that fires every 10 minutes for the specified duration
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("myTrigger", "group1")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInMinutes(1)
                        .withRepeatCount((int) (duration.toMinutes()) - 1)) // Repeat for the specified duration
                .build();

        System.out.println("Job about to start and duration is = :" + duration.toMinutes() );
        // Schedule the job with the trigger
        scheduler.scheduleJob(job, trigger);

        System.out.println("Job started");

        // Wait for the scheduler to complete
        Thread.sleep(duration.toMillis());

        System.out.println("ended");

        // Shut down the scheduler
        scheduler.shutdown();
    }
}
