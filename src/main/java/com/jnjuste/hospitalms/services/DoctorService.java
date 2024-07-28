package com.jnjuste.hospitalms.services;

import com.jnjuste.hospitalms.models.Doctor;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DoctorService {
    Doctor saveDoctor(Doctor doctor);
    List<Doctor> getAllDoctors();
    Optional<Doctor> getDoctorById(UUID id);
    Doctor updateDoctor(UUID id, Doctor doctorDetails);
    void deleteDoctor(UUID id);
    Optional<Doctor> getDoctorByEmail(String email);
    Optional<Doctor> getDoctorByNationalID(Integer nationalID);
    Optional<Doctor> getDoctorByRegNumber(String regNumber);

    boolean login(String email, String password, HttpSession session);

    boolean isFirstLogin(Doctor doctor);

    void changePassword(Doctor doctor, String newPassword);
}
