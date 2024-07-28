package com.jnjuste.hospitalms.services.impl;

import com.jnjuste.hospitalms.models.Nurse;
import com.jnjuste.hospitalms.repositories.NurseRepository;
import com.jnjuste.hospitalms.services.NurseService;
import com.jnjuste.hospitalms.utils.PasswordEncryptionUtil;
import com.jnjuste.hospitalms.utils.PasswordGenerator;
import jakarta.servlet.http.HttpSession;
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
        String encryptedPassword = PasswordEncryptionUtil.encryptPassword(generatedPassword);
        nurse.setPassword(encryptedPassword);
        String regNumber = nurseRegNumberServiceImpl.getNextRegNumber();
        nurse.setRegNumber(regNumber);
        nurse.setFirstLogin(true); // Set firstLogin to true
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
            StringBuilder updatedFields = new StringBuilder("The following details were updated:\n");

            if (!existingNurse.getFirstName().equals(nurseDetails.getFirstName())) {
                existingNurse.setFirstName(nurseDetails.getFirstName());
                updatedFields.append("First Name: ").append(nurseDetails.getFirstName()).append("\n");
            }
            if (!existingNurse.getLastName().equals(nurseDetails.getLastName())) {
                existingNurse.setLastName(nurseDetails.getLastName());
                updatedFields.append("Last Name: ").append(nurseDetails.getLastName()).append("\n");
            }
            if (!existingNurse.getEmail().equals(nurseDetails.getEmail())) {
                existingNurse.setEmail(nurseDetails.getEmail());
                updatedFields.append("Email: ").append(nurseDetails.getEmail()).append("\n");
            }
            if (!existingNurse.getGender().equals(nurseDetails.getGender())) {
                existingNurse.setGender(nurseDetails.getGender());
                updatedFields.append("Gender: ").append(nurseDetails.getGender()).append("\n");
            }

            Nurse updatedNurse = nurseRepository.save(existingNurse);

            String emailBody = "Dear " + existingNurse.getLastName() + ",\n\n" +
                    "The following details of your profile have been updated:\n" + updatedFields.toString() +
                    "\nThank you,\n" +
                    "Your System";
            emailServiceImpl.sendEmail(existingNurse.getEmail(), "Profile Update Notification", emailBody);

            return updatedNurse;
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
    @Override
    public boolean login(String email, String password, HttpSession session) {
        Optional<Nurse> nurseOpt = nurseRepository.findByEmail(email);
        if (nurseOpt.isPresent()) {
            Nurse nurse = nurseOpt.get();
            String encryptedPassword = PasswordEncryptionUtil.encryptPassword(password);
            if (nurse.getPassword().equals(encryptedPassword)) {
                session.setAttribute("nurse", nurse);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isFirstLogin(Nurse nurse) {
        return nurse.isFirstLogin();
    }

    @Override
    public void changePassword(Nurse nurse, String newPassword) {
        nurse.setPassword(PasswordEncryptionUtil.encryptPassword(newPassword));
        nurse.setFirstLogin(false);
        nurseRepository.save(nurse);

        String subject = "Hospital MS - Password Changed Successfully";
        String body = "Dear Nurse " + nurse.getFirstName() + ",\n\n" +
                "Your password for the Hospital MS application has been successfully changed.\n\n" +
                "If you did not initiate this change, please contact our IT support immediately.\n\n" +
                "For security reasons, we do not include your new password in this email. \n\n" +
                "Best regards,\n" +
                "Hospital MS Support Team";

        emailServiceImpl.sendEmail(nurse.getEmail(), subject, body);
    }
}
