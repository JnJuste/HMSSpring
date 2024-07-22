package com.jnjuste.hospitalms.services.impl;

import com.jnjuste.hospitalms.models.Nurse;
import com.jnjuste.hospitalms.repositories.NurseRepository;
import com.jnjuste.hospitalms.services.NurseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class NurseServiceImpl implements NurseService {
    private final NurseRepository nurseRepository;
    private final NurseRegNumberServiceImpl nurseRegNumberServiceImpl;

    @Autowired
    public NurseServiceImpl(NurseRepository nurseRepository, NurseRegNumberServiceImpl nurseRegNumberServiceImpl) {
        this.nurseRepository = nurseRepository;
        this.nurseRegNumberServiceImpl = nurseRegNumberServiceImpl;
    }

    @Override
    public Nurse saveNurse(Nurse nurse) {
        nurse.setRegNumber(nurseRegNumberServiceImpl.getNextRegNumber());
        return nurseRepository.save(nurse);
    }

    @Override
    public List<Nurse> getAllNurses() {
        return nurseRepository.findAll();
    }

    @Override
    public Optional<Nurse> getNurseById(UUID id) {
        return nurseRepository.findById(id);
    }

    @Override
    public Nurse updateNurse(UUID id, Nurse nurseDetails) {
        return nurseRepository.findById(id).map(existingNurse -> {
            // Update Nurse fields
            // The nurseID, RegNumber and NationalID remain unchanged
            existingNurse.setFirstName(nurseDetails.getFirstName());
            existingNurse.setLastName(nurseDetails.getLastName());
            existingNurse.setPassword(nurseDetails.getPassword());
            existingNurse.setEmail(nurseDetails.getEmail());
            existingNurse.setGender(nurseDetails.getGender());
            existingNurse.setDoctor(nurseDetails.getDoctor());
            // Update Nurse-specific fields if any
            // Currently, there are no Nurse-specific fields in the provided entity
            return nurseRepository.save(existingNurse);
        }).orElseThrow(() -> new ResourceNotFoundException("Nurse not found with id: " + id));
    }

    @Override
    public void deleteNurse(UUID id) {
        nurseRepository.deleteById(id);
    }

    @Override
    public Optional<Nurse> getNurseByEmail(String email) {
        return nurseRepository.findByEmail(email);
    }

    @Override
    public Optional<Nurse> getNurseByNationalID(Integer nationalID) {
        return nurseRepository.findByNationalID(nationalID);
    }

    @Override
    public Optional<Nurse> getNurseByRegNumber(String regNumber) {
        return nurseRepository.findByRegNumber(regNumber);
    }
}
