package com.jnjuste.hospitalms.services.impl;

import com.jnjuste.hospitalms.models.Patient;
import com.jnjuste.hospitalms.repositories.PatientRepository;
import com.jnjuste.hospitalms.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final PatientNumberServiceImpl patientNumberServiceImpl;

    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository, PatientNumberServiceImpl patientNumberServiceImpl) {
        this.patientRepository = patientRepository;
        this.patientNumberServiceImpl = patientNumberServiceImpl;
    }

    @Override
    public Patient savePatient(Patient patient) {
        if (patient.getEmail() == null || patient.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Patient email cannot be null or empty");
        }
        patient.setPatientNumber(patientNumberServiceImpl.getNextPatientNumber());
        return patientRepository.save(patient);
    }

    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public Optional<Patient> getPatientById(UUID id) {
        return patientRepository.findById(id);
    }

    @Override
    public Patient updatePatient(UUID id, Patient patientDetails) {
        return patientRepository.findById(id).map(existingPatient -> {
            // Update Patient fields
            // The patientID and NationalID remain unchanged
            existingPatient.setFirstName(patientDetails.getFirstName());
            existingPatient.setLastName(patientDetails.getLastName());
            existingPatient.setDateOfBirth(patientDetails.getDateOfBirth());
            existingPatient.setPhoneNumber(patientDetails.getPhoneNumber());
            existingPatient.setEmail(patientDetails.getEmail());
            existingPatient.setGender(patientDetails.getGender());
            existingPatient.setAddress(patientDetails.getAddress());
            // Don't update appointments here. They should be managed separately
            return patientRepository.save(existingPatient);
        }).orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
    }

    @Override
    public void deletePatient(UUID id) {
        patientRepository.deleteById(id);
    }

    @Override
    public Optional<Patient> getPatientByNationalID(Integer nationalID) {
        return patientRepository.findByNationalID(nationalID);
    }

    @Override
    public Optional<Patient> getPatientByPatientNumber(String patientNumber) {
        return patientRepository.findByPatientNumber(patientNumber);
    }
}
