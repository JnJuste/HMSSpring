package com.jnjuste.hospitalms.services;

import com.jnjuste.hospitalms.models.Patient;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PatientService {
    Patient savePatient(Patient patient);
    List<Patient> getAllPatients();
    Optional<Patient> getPatientById(UUID id);
    Patient updatePatient(UUID id, Patient patientDetails);
    void deletePatient(UUID id);
}
