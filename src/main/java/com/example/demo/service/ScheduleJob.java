package com.example.demo.service;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class ScheduleJob {

    @Autowired
    private Scheduler scheduler;

    private static final String JOB_GROUP = "group1";

    public void scheduleActivation(Long id, Timestamp startTime) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(ActivateSemesterJob.class)
                .withIdentity("activateJob-" + id, JOB_GROUP)
                .usingJobData("openTourId", id.toString())
                .usingJobData("startTime", startTime.getTime())
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("activateTrigger-" + id, JOB_GROUP)
                .startAt(startTime)
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
    }

    public void scheduleDeactivation(Long id, Timestamp endTime) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(DeactivateSemesterJob.class)
                .withIdentity("deactivateJob-" + id, JOB_GROUP)
                .usingJobData("openTourId", id.toString())
                .usingJobData("endTime", endTime.getTime())
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("deactivateTrigger-" + id, JOB_GROUP)
                .startAt(endTime)
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
    }

    public void cancelScheduledJob(Long id) throws SchedulerException {
        JobKey activateJobKey = new JobKey("activateJob-" + id, JOB_GROUP);
        if (scheduler.checkExists(activateJobKey)) {
            scheduler.deleteJob(activateJobKey);
        }

        JobKey deactivateJobKey = new JobKey("deactivateJob-" + id, JOB_GROUP);
        if (scheduler.checkExists(deactivateJobKey)) {
            scheduler.deleteJob(deactivateJobKey);
        }
    }
}
