package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Booking;
import com.example.demo.entity.CustomBooking;
import com.example.demo.entity.Schedule;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.ScheduleRequest;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.CustomBookingRepository;
import com.example.demo.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ScheduleService {
    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    CustomBookingRepository customBookingRepository;
    @Autowired
    AuthenticationService authenticationService;
    public Schedule createSchedule(ScheduleRequest scheduleRequest){
        Schedule schedule = new Schedule();
        Account consulting = authenticationService.getCurrentAccount();
        Booking booking = bookingRepository.findBookingByBookingId(scheduleRequest.getBookingId());
        CustomBooking customBooking = customBookingRepository.
                findCustomBookingByCustomBookingId(scheduleRequest.getCustomBookingId());

        if ((booking == null && customBooking != null) || (booking != null && customBooking == null)) {
            throw new IllegalArgumentException("Only one of Booking or CustomBooking must be non-null!");
        }
        schedule.setCreateAt(new Date());
        schedule.setFile(scheduleRequest.getFile());
        schedule.setBooking(booking);
        schedule.setCustomBooking(customBooking);
        schedule.setConsulting(consulting);
        return scheduleRepository.save(schedule);
    }

    public Schedule getSchedule(String id) {
        Booking booking = bookingRepository.findBookingByBookingId(id);
        if (booking == null) {
            throw new NotFoundException("Booking not found!");
        }

        Schedule schedule = booking.getSchedule();
        if (schedule == null) {
            throw new NotFoundException("Schedule not found!");
        }

        if (schedule.isDeleted()) {
            throw new NotFoundException("Schedule not found!");
        } else {
            return schedule;
        }
    }


    public Schedule getSchedules(String id){
        CustomBooking customBooking = customBookingRepository.findCustomBookingByCustomBookingId(id);
        if (customBooking == null || customBooking.getSchedule() == null) {
            throw new NotFoundException("Schedule not found!");
        }
        Schedule schedule = customBooking.getSchedule();
        if(schedule.isDeleted()){
            return schedule;
        }else{
            throw  new NotFoundException("Schedule not found!");
        }
    }
    public Schedule update(String file, long id){
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Schedule not found!"));
        schedule.setFile(file);
        return scheduleRepository.save(schedule);

    }

    public Schedule delete(long id ){
        Schedule schedule =scheduleRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Schedule not found"));
        schedule.setDeleted(true);
        return scheduleRepository.save(schedule);
    }

}
