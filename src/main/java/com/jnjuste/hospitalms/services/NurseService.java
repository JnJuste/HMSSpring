package com.jnjuste.hospitalms.services;

import com.jnjuste.hospitalms.models.Nurse;
import jakarta.servlet.http.HttpSession;

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

    boolean login(String email, String password, HttpSession session);

    boolean isFirstLogin(Nurse nurse);

    void changePassword(Nurse nurse, String newPassword);
}
