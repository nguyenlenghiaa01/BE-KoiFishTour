package com.example.demo.api;

import com.example.demo.entity.Schedule;
import com.example.demo.model.Request.ScheduleRequest;
import com.example.demo.service.ScheduleService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedule")
@CrossOrigin("*")
@SecurityRequirement(name="api")
public class ScheduleAPI {
    @Autowired
    ScheduleService scheduleService;
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @PreAuthorize("hasAuthority('SALE')")
    @PostMapping
    public ResponseEntity<Schedule> create(@Valid @RequestBody ScheduleRequest scheduleRequest){
        Schedule schedule = scheduleService.createSchedule(scheduleRequest);
        simpMessagingTemplate.convertAndSend("/topic/schedule","CREATE SCHEDULE");
        return  ResponseEntity.ok(schedule);
    }
    @PreAuthorize("hasAuthority('SALE')")
    @GetMapping("/custom-booking")
    public ResponseEntity<Schedule> getBooking(String id){
        Schedule schedule = scheduleService.getSchedule(id);
        return  ResponseEntity.ok(schedule);
    }
    @PreAuthorize("hasAuthority('SALE')")
    @PutMapping
    public ResponseEntity<Schedule> update(String file, long id){
        Schedule schedule = scheduleService.update(file,id);
        simpMessagingTemplate.convertAndSend("/topic/schedule","UPDATE SCHEDULE");
        return ResponseEntity.ok(schedule);
    }
    @PreAuthorize("hasAuthority('SALE')")
    @DeleteMapping
    public ResponseEntity<Schedule> delete(long id){
        Schedule schedule = scheduleService.delete(id);
        simpMessagingTemplate.convertAndSend("/topic/schedule","DELETE SCHEDULE");

        return ResponseEntity.ok(schedule);
    }
}
