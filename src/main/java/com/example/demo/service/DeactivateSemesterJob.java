package com.example.demo.service;

import com.example.demo.entity.Tour;
import com.example.demo.repository.TourRepository;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

@Component
public class DeactivateSemesterJob implements Job {
    @Autowired
    private TourRepository tourRepository;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String openTourId = context.getMergedJobDataMap().getString("openTourId");
        long endTimeMillis = context.getMergedJobDataMap().getLong("endTime");
        Timestamp endTime = new Timestamp(endTimeMillis);
        Date currentTime = new Date();

        if (currentTime.after(endTime)) {
            Optional<Tour> tourOpt = tourRepository.findById(Long.parseLong(openTourId));
            if (tourOpt.isPresent()) {
                Tour tour = tourOpt.get();
                if (tour.getStatus().equals("open")) {
                    tour.setStatus("Not open");
                    tourRepository.save(tour);
                    System.out.println("Tour " + openTourId + " inactive (status: INACTIVE).");
                } else {
                    System.out.println("Tour " + openTourId + " not status ACTIVE, not do it.");
                }
            } else {
                System.out.println("Not found Tour ID: " + openTourId);
            }
        } else {
            System.out.println("Current time is not after endTime, skipping deactivation for Tour ID: " + openTourId);
        }
    }
}
