package com.jnjuste.hospitalms.controllers;

import com.jnjuste.hospitalms.models.Appointment;
import com.jnjuste.hospitalms.models.Nurse;
import com.jnjuste.hospitalms.models.Patient;
import com.jnjuste.hospitalms.models.enums.AppointmentStatus;
import com.jnjuste.hospitalms.services.AppointmentService;
import com.jnjuste.hospitalms.services.PatientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/nurse")
public class AuthNurseController {

    private final PatientService patientService;
    private final AppointmentService appointmentService;

    @Autowired
    public AuthNurseController(PatientService patientService, AppointmentService appointmentService) {
        this.patientService = patientService;
        this.appointmentService = appointmentService;
    }

    @PostMapping("/patient")
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        return ResponseEntity.ok(patientService.savePatient(patient));
    }

    @PostMapping("/appointment")
    public ResponseEntity<String> createAppointment(@RequestBody Appointment appointment, HttpSession session) {
        Nurse nurse = (Nurse) session.getAttribute("nurse");
        if (nurse == null) {
            return ResponseEntity.status(401).body("Unauthorized.");
        }

        List<Appointment> existingAppointments = appointmentService.getAllAppointments();
        for (Appointment existingAppointment : existingAppointments) {
            if (existingAppointment.getDoctor().getDoctorID().equals(appointment.getDoctor().getDoctorID())) {
                LocalDateTime newStart = appointment.getStartTime();
                LocalDateTime newEnd = appointment.getEndTime();
                LocalDateTime existingStart = existingAppointment.getStartTime();
                LocalDateTime existingEnd = existingAppointment.getEndTime();
                if (newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart)) {
                    return ResponseEntity.status(409).body("Appointment collides with an existing appointment.");
                }
            }
        }

        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointment.setRegisteredBy(nurse);
        appointmentService.saveAppointment(appointment);
        return ResponseEntity.ok("Appointment created successfully.");
    }
}
