package com.jnjuste.hospitalms.controllers;

import com.jnjuste.hospitalms.models.Appointment;
import com.jnjuste.hospitalms.models.Doctor;
import com.jnjuste.hospitalms.models.enums.AppointmentStatus;
import com.jnjuste.hospitalms.services.AppointmentService;
import com.jnjuste.hospitalms.services.impl.ResourceNotFoundException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@RequestBody Appointment appointment) {
        return ResponseEntity.ok(appointmentService.saveAppointment(appointment));
    }

    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable UUID id) {
        Optional<Appointment> appointment = appointmentService.getAppointmentById(id);
        return appointment.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/appointmentNumber/{appointmentNumber}")
    public ResponseEntity<Appointment> getAppointmentByAppointmentNumber(@PathVariable String appointmentNumber) {
        Optional<Appointment> appointment = appointmentService.getAppointmentByAppointmentNumber(appointmentNumber);
        return appointment.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Doctor Scheduled Appointment Controllers Start
    @GetMapping("/scheduled")
    public ResponseEntity<List<Appointment>> getScheduledAppointments(HttpSession session) {
        Doctor doctor = (Doctor) session.getAttribute("doctor");
        if (doctor == null) {
            return ResponseEntity.status(401).body(null);
        }
        List<Appointment> appointments = appointmentService.getScheduledAppointmentsByDoctor(doctor.getDoctorID());
        return ResponseEntity.ok(appointments);
    }

    @PutMapping("/{appointmentId}/status")
    public ResponseEntity<Appointment> updateAppointmentStatus(@PathVariable UUID appointmentId,
                                                               @RequestParam AppointmentStatus status,
                                                               HttpSession session) {
        Doctor doctor = (Doctor) session.getAttribute("doctor");
        if (doctor == null) {
            return ResponseEntity.status(401).body(null);
        }
        Optional<Appointment> updatedAppointment = appointmentService.updateAppointmentStatus(appointmentId, status);
        return updatedAppointment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Doctor Scheduled Appointment Controllers End
    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable UUID id, @RequestBody Appointment appointmentDetails) {
        try {
            return ResponseEntity.ok(appointmentService.updateAppointment(id, appointmentDetails));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable UUID id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }


}
