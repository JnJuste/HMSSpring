package com.jnjuste.hospitalms.controllers;

import com.jnjuste.hospitalms.models.Appointment;
import com.jnjuste.hospitalms.models.Doctor;
import com.jnjuste.hospitalms.models.enums.AppointmentStatus;
import com.jnjuste.hospitalms.services.AppointmentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/doctor")
public class AuthDoctorController {

    private final AppointmentService appointmentService;

    @Autowired
    public AuthDoctorController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/appointments/scheduled")
    public ResponseEntity<List<Appointment>> getScheduledAppointments(HttpSession session) {
        Doctor doctor = (Doctor) session.getAttribute("doctor");
        if (doctor == null) {
            return ResponseEntity.status(401).body(null);
        }
        List<Appointment> appointments = appointmentService.getScheduledAppointmentsByDoctor(doctor.getDoctorID());
        return ResponseEntity.ok(appointments);
    }

    @PutMapping("/appointments/{appointmentId}/status")
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
}
