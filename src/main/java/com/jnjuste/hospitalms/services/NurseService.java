package com.jnjuste.hospitalms.services;

import com.jnjuste.hospitalms.models.Nurse;

import java.util.List;
import java.util.UUID;

public interface NurseService {
    Nurse saveNurse(Nurse nurse);
    Nurse getNurseById(UUID id);
    List<Nurse> getAllNurses();
    Nurse updateNurse(UUID id, Nurse nurse);
    void deleteNurse(UUID id);
}
