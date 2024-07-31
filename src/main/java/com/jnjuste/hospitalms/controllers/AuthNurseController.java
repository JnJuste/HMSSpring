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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

        // Check if the patient already has an appointment
        List<Appointment> patientAppointments = appointmentService.getAllAppointments().stream()
                .filter(a -> a.getPatient().getPatientID().equals(appointment.getPatient().getPatientID()))
                .collect(Collectors.toList());

        if (!patientAppointments.isEmpty()) {
            return ResponseEntity.status(409).body("Patient already has an appointment.");
        }

        // Check for appointment collision with the same doctor
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

    @GetMapping("/patients")
    public ResponseEntity<List<Patient>> getPatientsByNurse(HttpSession session) {
        Nurse nurse = (Nurse) session.getAttribute("nurse");
        if (nurse == null) {
            return ResponseEntity.status(401).body(null);
        }

        List<Patient> patients = appointmentService.getAllAppointments().stream()
                .filter(appointment -> appointment.getRegisteredBy().getNurseID().equals(nurse.getNurseID()))
                .map(Appointment::getPatient)
                .distinct()
                .collect(Collectors.toList());

        return ResponseEntity.ok(patients);
    }
}
