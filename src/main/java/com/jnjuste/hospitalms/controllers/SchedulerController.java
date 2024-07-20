package com.jnjuste.hospitalms.controllers;

import com.jnjuste.hospitalms.models.Scheduler;
import com.jnjuste.hospitalms.services.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/appointments")
public class SchedulerController {

    private final SchedulerService schedulerService;

    @Autowired
    public SchedulerController(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    @PostMapping
    public ResponseEntity<Scheduler> scheduleAppointment(@RequestBody Scheduler scheduler) {
        return ResponseEntity.ok(schedulerService.scheduleAppointment(scheduler));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Scheduler> getAppointmentById(@PathVariable UUID id) {
        return ResponseEntity.ok(schedulerService.getAppointmentById(id));
    }

    @GetMapping
    public ResponseEntity<List<Scheduler>> getAllAppointments() {
        return ResponseEntity.ok(schedulerService.getAllAppointments());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Scheduler> updateAppointment(@PathVariable UUID id, @RequestBody Scheduler scheduler) {
        return ResponseEntity.ok(schedulerService.updateAppointment(id, scheduler));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelAppointment(@PathVariable UUID id) {
        schedulerService.cancelAppointment(id);
        return ResponseEntity.ok().build();
    }
}
