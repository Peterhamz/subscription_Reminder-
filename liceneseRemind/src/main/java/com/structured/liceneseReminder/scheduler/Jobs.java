package com.structured.liceneseReminder.scheduler;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


public class Jobs implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // Get the fire time of the trigger
        LocalDateTime fireTime = LocalDateTime.now();


        System.out.println("This is the firetime" + fireTime);
        // Get the expiry date from the JobDataMap
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        Duration duration =  (Duration) jobDataMap.get("expiryDate");

        System.out.println( " This is the duration " + duration);

        // Calculate the time until the end of the duration
        LocalDateTime endTime = fireTime.plus(duration).minusMinutes(1);
        long minutesUntilEnd = Duration.between(fireTime, endTime).toMinutes();

        System.out.println( "This is the firetime again " + fireTime);


        System.out.println( "This is the endTime " + endTime);

        System.out.println( "This is the minutesUntilEnd " + minutesUntilEnd);

        // Check if it's 30 minutes before the end of the duration
        if (minutesUntilEnd == 1) {
            System.out.println("1 minutes to go! testing");
        }
    }
    }

