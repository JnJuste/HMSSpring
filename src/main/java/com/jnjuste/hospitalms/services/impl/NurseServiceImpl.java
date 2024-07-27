package com.jnjuste.hospitalms.services.impl;

import com.jnjuste.hospitalms.models.Nurse;
import com.jnjuste.hospitalms.repositories.NurseRepository;
import com.jnjuste.hospitalms.services.NurseService;
import com.jnjuste.hospitalms.utils.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class NurseServiceImpl implements NurseService {
    private final NurseRepository nurseRepository;
    private final NurseRegNumberServiceImpl nurseRegNumberServiceImpl;
    private final EmailServiceImpl emailServiceImpl;


    @Autowired
    public NurseServiceImpl(NurseRepository nurseRepository, NurseRegNumberServiceImpl nurseRegNumberServiceImpl, EmailServiceImpl emailServiceImpl) {
        this.nurseRepository = nurseRepository;
        this.nurseRegNumberServiceImpl = nurseRegNumberServiceImpl;
        this.emailServiceImpl = emailServiceImpl;
    }

    @Override
    public Nurse saveNurse(Nurse nurse) {
        String generatedPassword = PasswordGenerator.generateRandomPassword(8);
        nurse.setPassword(generatedPassword);
        String regNumber = nurseRegNumberServiceImpl.getNextRegNumber();
        nurse.setRegNumber(regNumber);
        Nurse savedNurse = nurseRepository.save(nurse);
        emailServiceImpl.sendEmail(nurse.getEmail(), "Welcome to Our Hospital's Nursing Team",
                "Dear " + nurse.getFirstName() + ",\n\n" +
                        "Welcome to our hospital's nursing team! We're excited to have you join us in providing exceptional patient care.\n\n" +
                        "Your account for the Hospital Management System has been created. Here are your details:\n\n" +
                        "Registration Number: " + regNumber + "\n" +
                        "Username: " + nurse.getEmail() + "\n" +
                        "Password: " + generatedPassword + "\n\n" +
                        "For security reasons, please change your password upon your first login.\n\n" +
                        "If you have any questions or need assistance, please don't hesitate to contact our IT support team.\n\n" +
                        "We look forward to working with you!\n\n" +
                        "Best regards,\n" +
                        "Hospital Administration");
        return savedNurse;
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
