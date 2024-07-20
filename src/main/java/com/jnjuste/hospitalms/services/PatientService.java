package com.jnjuste.hospitalms.services;

import com.jnjuste.hospitalms.models.Patient;

import java.util.List;
import java.util.UUID;

public interface PatientService {
    Patient savePatient(Patient patient);
    Patient getPatientById(UUID id);
    List<Patient> getAllPatients();
    Patient updatePatient(UUID id, Patient patient);
    void deletePatient(UUID id);
}
