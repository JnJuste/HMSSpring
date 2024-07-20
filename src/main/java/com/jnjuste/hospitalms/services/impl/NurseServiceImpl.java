package com.jnjuste.hospitalms.services.impl;

import com.jnjuste.hospitalms.models.Nurse;
import com.jnjuste.hospitalms.repositories.NurseRepository;
import com.jnjuste.hospitalms.services.NurseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class NurseServiceImpl implements NurseService {

    private final NurseRepository nurseRepository;

    @Autowired
    public NurseServiceImpl(NurseRepository nurseRepository) {
        this.nurseRepository = nurseRepository;
    }

    @Override
    public Nurse saveNurse(Nurse nurse) {
        return nurseRepository.save(nurse);
    }

    @Override
    public Nurse getNurseById(UUID id) {
        return nurseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nurse not found"));
    }

    @Override
    public List<Nurse> getAllNurses() {
        return nurseRepository.findAll();
    }

    @Override
    public Nurse updateNurse(UUID id, Nurse nurse) {
        Nurse existingNurse = getNurseById(id);
        existingNurse.setFirstName(nurse.getFirstName());
        existingNurse.setLastName(nurse.getLastName());
        existingNurse.setEmail(nurse.getEmail());
        existingNurse.setLicenseNumber(nurse.getLicenseNumber());
        existingNurse.setSpecialty(nurse.getSpecialty());
        existingNurse.setYearsOfExperience(nurse.getYearsOfExperience());
        existingNurse.setEmploymentType(nurse.getEmploymentType());
        return nurseRepository.save(existingNurse);
    }

    @Override
    public void deleteNurse(UUID id) {
        nurseRepository.deleteById(id);
    }
}
