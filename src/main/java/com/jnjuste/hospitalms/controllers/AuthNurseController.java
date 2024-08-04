package com.jnjuste.hospitalms.controllers;

import com.jnjuste.hospitalms.models.Appointment;
import com.jnjuste.hospitalms.models.Doctor;
import com.jnjuste.hospitalms.models.Nurse;
import com.jnjuste.hospitalms.models.Patient;
import com.jnjuste.hospitalms.models.enums.AppointmentStatus;
import com.jnjuste.hospitalms.services.AppointmentService;
import com.jnjuste.hospitalms.services.DoctorService;
import com.jnjuste.hospitalms.services.PatientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/nurse")
public class AuthNurseController {

    private final PatientService patientService;
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;

    @Autowired
    public AuthNurseController(PatientService patientService, DoctorService doctorService, AppointmentService appointmentService) {
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.appointmentService = appointmentService;
    }
    // Login Nurse Add New Patient (POST Method)
    // URL example used in Postman: http://localhost:7777/api/nurse/patient
    // After that Select "Body" > then "raw"
    /*
    {
    "nationalID": 119968009,
    "firstName": "Jean Juste",
    "lastName": "IRAKOZE",
    "dateOfBirth": "1989-05-16",
    "phoneNumber": "+250790439778",
    "email": "jeanjusteirakoze@gmail.com",
    "gender": "M",
    "address": "Dortmund, GERMANY"
    }
    */
    // Then Send
    // Remember that Gender is an enum having M, F, OTHER
    @PostMapping("/patient")
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        if (patient.getEmail() == null || patient.getEmail().isEmpty()) {
            return ResponseEntity.status(400).body(null); // Bad request if email is null or empty
        }
        return ResponseEntity.ok(patientService.savePatient(patient));
    }

    // Login Nurse Make an Appointment for A Patient he/she created (POST Method)
    // URL example used in Postman: http://localhost:7777/api/nurse/appointment
    // After that Select "Body" > then "raw"
    /*
    {
    "doctor": {
        "doctorID": "930f8989-e4d2-46a1-9d68-a13c4710f309"
    },
    "patient": {
        "patientID": "ebc115fd-ae4d-4db8-93f0-8ad110cdc098"
    },
    "startTime": "2024-08-02T09:30:00",
    "endTime": "2024-08-02T10:00:00",
    "durationMinutes": 30,
    "reason": "Fever with diarrhea symptoms"
    }
    */
    // Then click on Send
    @PostMapping("/appointment")
    public ResponseEntity<String> createAppointment(@RequestBody Appointment appointment, HttpSession session) {
        Nurse nurse = (Nurse) session.getAttribute("nurse");
        if (nurse == null) {
            return ResponseEntity.status(401).body("Unauthorized.");
        }

        // Ensure doctor is fully populated
        Optional<Doctor> doctorOpt = doctorService.getDoctorById(appointment.getDoctor().getDoctorID());
        if (doctorOpt.isEmpty() || doctorOpt.get().getEmail() == null || doctorOpt.get().getEmail().isEmpty()) {
            return ResponseEntity.status(400).body("Doctor email is missing.");
        }
        Doctor doctor = doctorOpt.get();

        // Ensure patient is fully populated
        Optional<Patient> patientOpt = patientService.getPatientById(appointment.getPatient().getPatientID());
        if (patientOpt.isEmpty() || patientOpt.get().getEmail() == null || patientOpt.get().getEmail().isEmpty()) {
            return ResponseEntity.status(400).body("Patient email is missing.");
        }
        Patient patient = patientOpt.get();

        // Check if the patient already has an appointment
        List<Appointment> patientAppointments = appointmentService.getAllAppointments().stream()
                .filter(a -> a.getPatient().getPatientID().equals(patient.getPatientID()))
                .collect(Collectors.toList());

        if (!patientAppointments.isEmpty()) {
            return ResponseEntity.status(409).body("Patient already has an appointment.");
        }

        // Check for appointment collision with the same doctor
        List<Appointment> existingAppointments = appointmentService.getAllAppointments();
        for (Appointment existingAppointment : existingAppointments) {
            if (existingAppointment.getDoctor().getDoctorID().equals(doctor.getDoctorID())) {
                LocalDateTime newStart = appointment.getStartTime();
                LocalDateTime newEnd = appointment.getEndTime();
                LocalDateTime existingStart = existingAppointment.getStartTime();
                LocalDateTime existingEnd = existingAppointment.getEndTime();
                if (newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart)) {
                    return ResponseEntity.status(409).body("Appointment collides with an existing appointment.");
                }
            }
        }

        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointment.setRegisteredBy(nurse);
        appointmentService.saveAppointment(appointment);
        return ResponseEntity.ok("Appointment created successfully.");
    }
    // Get Patients by Nurse Logged In (GET Method)
    // URL example used in Postman: http://localhost:7777/api/nurse/patients
    // Then click on Send
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
    @GetMapping("/me")
    public ResponseEntity<Nurse> getCurrentNurse(HttpSession session) {
        Nurse nurse = (Nurse) session.getAttribute("nurse");
        if (nurse == null) {
            return ResponseEntity.status(401).body(null);
        }
        return ResponseEntity.ok(nurse);
    }
    @GetMapping("/all")
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }
}
