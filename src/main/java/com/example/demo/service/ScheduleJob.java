package com.example.demo.service;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class ScheduleJob {

    @Autowired
    private Scheduler scheduler;

    public void scheduleActivation(Long id, Timestamp startTime) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(ActivateSemesterJob.class)
                .withIdentity("activateJob-" + id, "group1")
                .usingJobData("openTourId", id.toString())
                .usingJobData("startTime", startTime.getTime()) // Chuyển đổi thành long
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("activateTrigger-" + id, "group1")
                .startAt(startTime)
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
    }


    public void scheduleDeactivation(Long id, Timestamp endTime) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(DeactivateSemesterJob.class)
                .withIdentity("deactivateJob-" + id, "group1")
                .usingJobData("openTourId", id.toString())
                .usingJobData("endTime", endTime.getTime()) // Thêm endTime nếu cần
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("deactivateTrigger-" + id, "group1")
                .startAt(endTime)
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
    }
}
