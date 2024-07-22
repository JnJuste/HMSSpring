package com.jnjuste.hospitalms.services;

import com.jnjuste.hospitalms.models.Nurse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NurseService {
    Nurse saveNurse(Nurse nurse);
    List<Nurse> getAllNurses();
    Optional<Nurse> getNurseById(UUID id);
    Nurse updateNurse(UUID id, Nurse nurseDetails);
    void deleteNurse(UUID id);
    Optional<Nurse> getNurseByEmail(String email);
    Optional<Nurse> getNurseByNationalID(Integer nationalID);
    Optional<Nurse> getNurseByRegNumber(String regNumber);
}
