package com.example.demo.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.concurrent.ScheduledFuture;

@Data
public class ScheduleJob {

        private long tourId;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private ScheduledFuture<?> activationTask;
        private ScheduledFuture<?> deactivationTask;

    public void cancel() {
        if (activationTask != null) {
            activationTask.cancel(false);
        }
        if (deactivationTask != null) {
            deactivationTask.cancel(false);
        }
    }

}
