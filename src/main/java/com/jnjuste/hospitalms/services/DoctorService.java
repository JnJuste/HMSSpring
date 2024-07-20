package com.jnjuste.hospitalms.services;

import com.jnjuste.hospitalms.models.Doctor;

import java.util.List;
import java.util.UUID;

public interface DoctorService {
    Doctor saveDoctor(Doctor doctor);
    Doctor getDoctorById(UUID id);
    List<Doctor> getAllDoctors();
    Doctor updateDoctor(UUID id, Doctor doctor);
    void deleteDoctor(UUID id);
}
