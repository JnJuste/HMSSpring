package com.jnjuste.hospitalms.services;

import com.jnjuste.hospitalms.models.Doctor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DoctorService {
    Doctor saveDoctor(Doctor doctor);
    List<Doctor> getAllDoctors();
    Optional<Doctor> getDoctorById(UUID id);
    Doctor updateDoctor(UUID id, Doctor doctorDetails);
    void deleteDoctor(UUID id);
}
