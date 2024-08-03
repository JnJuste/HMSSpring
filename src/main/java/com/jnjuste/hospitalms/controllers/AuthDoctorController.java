package com.jnjuste.hospitalms.controllers;

import com.jnjuste.hospitalms.models.Appointment;
import com.jnjuste.hospitalms.models.Doctor;
import com.jnjuste.hospitalms.models.enums.AppointmentStatus;
import com.jnjuste.hospitalms.models.enums.EmploymentType;
import com.jnjuste.hospitalms.services.AppointmentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    // Get Appointments by Status(SCHEDULED, COMPLETED, RESCHEDULED, CANCELED) (GET method)
    // URL example used in Postman: http://localhost:7777/api/doctor/appointments/SCHEDULED
    // URL example used in Postman: http://localhost:7777/api/doctor/appointments/COMPLETED
    // URL example used in Postman: http://localhost:7777/api/doctor/appointments/RESCHEDULED
    // URL example used in Postman: http://localhost:7777/api/doctor/appointments/CANCELED
    // Then Send
    @GetMapping("/appointments/{status}")
    public ResponseEntity<List<Appointment>> getAppointmentsByStatus(@PathVariable AppointmentStatus status, HttpSession session) {
        Doctor doctor = (Doctor) session.getAttribute("doctor");
        if (doctor == null) {
            return ResponseEntity.status(401).body(null);
        }
        List<Appointment> appointments = appointmentService.getAppointmentsByDoctorAndStatus(doctor.getDoctorID(), status);
        return ResponseEntity.ok(appointments);
    }

    // Update Appointment Status(COMPLETED, CANCELED) (PUT  Method)
    // URL example used in Postman: http://localhost:7777/api/doctor/appointments/0334ba9c-400e-47d8-9707-32b50de78262/status
    // After that Select "Body" > then "x-www-form-urlencoded"
    // Key: "status", Value: "CANCELED" or "COMPLETED"
    // Then Send
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

    // Doctor Reschedule Appointment (PUT Method)
    // URL example used in Postman: http://localhost:7777/api/doctor/appointments/36c8d5ed-8797-4e29-ac53-e55140273118/reschedule
    // After that Select "Body" > then "x-www-form-urlencoded"
    // Key: "newStartTime", Value: "2024-08-03T10:00:00"
    // Key: "newEndTime", Value: "2024-08-03T10:03:00"
    // Then Send
    @PutMapping("/appointments/{appointmentId}/reschedule")
    public ResponseEntity<String> rescheduleAppointment(@PathVariable UUID appointmentId,
                                                        @RequestParam String newStartTime,
                                                        @RequestParam String newEndTime,
                                                        HttpSession session) {
        Doctor doctor = (Doctor) session.getAttribute("doctor");
        if (doctor == null) {
            return ResponseEntity.status(401).body("Unauthorized.");
        }

        try {
            LocalDateTime startTime = LocalDateTime.parse(newStartTime);
            LocalDateTime endTime = LocalDateTime.parse(newEndTime);

            if ((doctor.getEmploymentType() == EmploymentType.FULL_TIME &&
                    (startTime.getHour() < 9 || endTime.getHour() > 17)) ||
                    (doctor.getEmploymentType() == EmploymentType.PART_TIME &&
                            (startTime.getHour() < 17 || endTime.getHour() > 21))) {
                return ResponseEntity.status(400).body("Rescheduled time is outside of working hours.");
            }

            Optional<Appointment> updatedAppointment = appointmentService.rescheduleAppointment(appointmentId, startTime, endTime);
            return updatedAppointment.map(a -> ResponseEntity.ok("Appointment rescheduled successfully."))
                    .orElseGet(() -> ResponseEntity.status(404).body("Appointment not found."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }
}
