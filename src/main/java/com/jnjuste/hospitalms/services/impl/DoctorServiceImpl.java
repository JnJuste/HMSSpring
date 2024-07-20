package com.jnjuste.hospitalms.services.impl;

import com.jnjuste.hospitalms.models.Doctor;
import com.jnjuste.hospitalms.repositories.DoctorRepository;
import com.jnjuste.hospitalms.services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @Override
    public Optional<Doctor> getDoctorById(UUID id) {
        return doctorRepository.findById(id);
    }

    @Override
    public Doctor updateDoctor(UUID id, Doctor doctorDetails) {
        return doctorRepository.findById(id).map(existingDoctor -> {
            // Update User fields
            existingDoctor.setFirstName(doctorDetails.getFirstName());
            existingDoctor.setLastName(doctorDetails.getLastName());
            existingDoctor.setEmail(doctorDetails.getEmail());
            existingDoctor.setGender(doctorDetails.getGender());
            // Update Doctor-specific fields
            existingDoctor.setSpecialty(doctorDetails.getSpecialty());
            existingDoctor.setEmploymentType(doctorDetails.getEmploymentType());
            // Don't update appointments here. They should be managed separately
            return doctorRepository.save(existingDoctor);
        }).orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));
    }

    @Override
    public void deleteDoctor(UUID id) {
        doctorRepository.deleteById(id);
    }
}