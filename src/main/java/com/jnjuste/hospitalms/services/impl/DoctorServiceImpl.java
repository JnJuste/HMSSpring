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
    private final DoctorRegNumberServiceImpl doctorRegNumberServiceImpl;

    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository, DoctorRegNumberServiceImpl doctorRegNumberServiceImpl) {
        this.doctorRepository = doctorRepository;
        this.doctorRegNumberServiceImpl = doctorRegNumberServiceImpl;
    }

    @Override
    public Doctor saveDoctor(Doctor doctor) {
        doctor.setRegNumber(doctorRegNumberServiceImpl.getNextRegNumber());
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
            // Update Doctor fields
            // The doctorID, RegNumber and NationalID remain unchanged
            existingDoctor.setFirstName(doctorDetails.getFirstName());
            existingDoctor.setLastName(doctorDetails.getLastName());
            existingDoctor.setPassword(doctorDetails.getPassword());
            existingDoctor.setEmail(doctorDetails.getEmail());
            existingDoctor.setGender(doctorDetails.getGender());
            existingDoctor.setPassword(doctorDetails.getPassword());
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

    @Override
    public Optional<Doctor> getDoctorByEmail(String email) {
        return doctorRepository.findByEmail(email);
    }

    @Override
    public Optional<Doctor> getDoctorByNationalID(Integer nationalID) {
        return doctorRepository.findByNationalID(nationalID);
    }

    @Override
    public Optional<Doctor> getDoctorByRegNumber(String regNumber) {
        return doctorRepository.findByRegNumber(regNumber);
    }
}