package com.jnjuste.hospitalms.controllers;

import com.jnjuste.hospitalms.dtos.LoginDTO;
import com.jnjuste.hospitalms.models.Doctor;
import com.jnjuste.hospitalms.models.Nurse;
import com.jnjuste.hospitalms.services.DoctorService;
import com.jnjuste.hospitalms.services.NurseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final DoctorService doctorService;
    private final NurseService nurseService;

    @Autowired
    public LoginController(DoctorService doctorService, NurseService nurseService) {
        this.doctorService = doctorService;
        this.nurseService = nurseService;
    }

    @PostMapping("/login/doctor")
    public ResponseEntity<String> loginDoctor(@RequestBody LoginDTO loginDTO, HttpSession session) {
        boolean success = doctorService.login(loginDTO.getEmail(), loginDTO.getPassword(), session);
        if (success) {
            Doctor doctor = (Doctor) session.getAttribute("doctor");
            System.out.println("Doctor session: " + session.getId() + " - " + doctor.getEmail());
            if (doctorService.isFirstLogin(doctor)) {
                return ResponseEntity.ok("First login. Please change your password.");
            }
            return ResponseEntity.ok("Login successful.");
        } else {
            return ResponseEntity.status(401).body("Invalid email or password.");
        }
    }

    @PostMapping("/login/nurse")
    public ResponseEntity<String> loginNurse(@RequestBody LoginDTO loginDTO, HttpSession session) {
        boolean success = nurseService.login(loginDTO.getEmail(), loginDTO.getPassword(), session);
        if (success) {
            Nurse nurse = (Nurse) session.getAttribute("nurse");
            System.out.println("Nurse session: " + session.getId() + " - " + nurse.getEmail());
            if (nurseService.isFirstLogin(nurse)) {
                return ResponseEntity.ok("First login. Please change your password.");
            }
            return ResponseEntity.ok("Login successful.");
        } else {
            return ResponseEntity.status(401).body("Invalid email or password.");
        }
    }

    @PostMapping("/change-password/doctor")
    public ResponseEntity<String> changeDoctorPassword(@RequestParam String newPassword, HttpSession session) {
        Doctor doctor = (Doctor) session.getAttribute("doctor");
        if (doctor != null) {
            System.out.println("Changing password for doctor: " + doctor.getEmail() + " Session ID: " + session.getId());
            doctorService.changePassword(doctor, newPassword);
            session.removeAttribute("doctor");
            return ResponseEntity.ok("Password changed successfully.");
        }
        System.out.println("Unauthorized attempt to change password. Session ID: " + session.getId());
        return ResponseEntity.status(401).body("Unauthorized.");
    }

    @PostMapping("/change-password/nurse")
    public ResponseEntity<String> changeNursePassword(@RequestParam String newPassword, HttpSession session) {
        Nurse nurse = (Nurse) session.getAttribute("nurse");
        if (nurse != null) {
            System.out.println("Changing password for nurse: " + nurse.getEmail() + " Session ID: " + session.getId());
            nurseService.changePassword(nurse, newPassword);
            session.removeAttribute("nurse");
            return ResponseEntity.ok("Password changed successfully.");
        }
        System.out.println("Unauthorized attempt to change password. Session ID: " + session.getId());
        return ResponseEntity.status(401).body("Unauthorized.");
    }

    @GetMapping("/session-timeout")
    public ResponseEntity<String> sessionTimeout() {
        return ResponseEntity.status(401).body("Session has timed out. Please log in again.");
    }

    @GetMapping("/logout/doctor")
    public ResponseEntity<String> logoutDoctor(HttpSession session) {
        session.removeAttribute("doctor");
        session.invalidate();
        System.out.println("Session invalidated for doctor: " + session.getId());
        return ResponseEntity.ok("Logout successful.");
    }

    @GetMapping("/logout/nurse")
    public ResponseEntity<String> logoutNurse(HttpSession session) {
        session.removeAttribute("nurse");
        session.invalidate();
        System.out.println("Session invalidated for nurse: " + session.getId());
        return ResponseEntity.ok("Logout successful.");
    }
}

