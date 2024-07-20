// DoctorServiceImpl.java
package com.jnjuste.hospitalms.services.impl;

import com.jnjuste.hospitalms.models.Doctor;
import com.jnjuste.hospitalms.repositories.DoctorRepository;
import com.jnjuste.hospitalms.services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    @Override
    public Doctor getDoctorById(UUID id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @Override
    public Doctor updateDoctor(UUID id, Doctor doctor) {
        Doctor existingDoctor = getDoctorById(id);
        existingDoctor.setFirstName(doctor.getFirstName());
        existingDoctor.setLastName(doctor.getLastName());
        existingDoctor.setEmail(doctor.getEmail());
        existingDoctor.setEmploymentType(doctor.getEmploymentType());
        existingDoctor.setSpecialties(doctor.getSpecialties());
        existingDoctor.setLicenseNumber(doctor.getLicenseNumber());
        existingDoctor.setYearsOfExperience(doctor.getYearsOfExperience());
        existingDoctor.setBiography(doctor.getBiography());
        existingDoctor.setAvailable(doctor.isAvailable());
        return doctorRepository.save(existingDoctor);
    }

    @Override
    public void deleteDoctor(UUID id) {
        doctorRepository.deleteById(id);
    }
}