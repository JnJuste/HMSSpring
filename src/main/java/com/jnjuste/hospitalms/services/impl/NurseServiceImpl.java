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

    @Autowired
    public NurseServiceImpl(NurseRepository nurseRepository) {
        this.nurseRepository = nurseRepository;
    }

    @Override
    public Nurse saveNurse(Nurse nurse) {
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
            // Update User fields
            existingNurse.setFirstName(nurseDetails.getFirstName());
            existingNurse.setLastName(nurseDetails.getLastName());
            existingNurse.setEmail(nurseDetails.getEmail());
            existingNurse.setGender(nurseDetails.getGender());
            // Update Nurse-specific fields if any
            // Currently, there are no Nurse-specific fields in the provided entity
            return nurseRepository.save(existingNurse);
        }).orElseThrow(() -> new ResourceNotFoundException("Nurse not found with id: " + id));
    }

    @Override
    public void deleteNurse(UUID id) {
        nurseRepository.deleteById(id);
    }
}
