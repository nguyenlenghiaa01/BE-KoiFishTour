package com.example.demo.service;

import com.example.demo.entity.OpenTour;
import com.example.demo.entity.Tour;
import com.example.demo.repository.OpenTourRepository;
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
public class ActivateSemesterJob implements Job {

    @Autowired
    private OpenTourRepository openTourRepository;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String openTourId = context.getMergedJobDataMap().getString("openTourId");
        long startTimeMillis = context.getMergedJobDataMap().getLong("startTime");
        Timestamp startTime = new Timestamp(startTimeMillis);
        Date currentTime = new Date();
        if (currentTime.after(startTime) || Math.abs(currentTime.getTime() - startTime.getTime()) < 1000) {
            Optional<OpenTour> tourOpt = openTourRepository.findById(Long.parseLong(openTourId));
            if (tourOpt.isPresent()) {
                OpenTour tour = tourOpt.get();
                tour.setStatus("OPEN");
                openTourRepository.save(tour);
                System.out.println("Tour " + openTourId + " active (status: Active).");
            } else {
                System.out.println("Not Found Tour ID: " + openTourId);
            }
        } else {
            System.out.println("Err.");
        }
    }

}
