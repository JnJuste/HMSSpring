package com.jnjuste.hospitalms.services.impl;

import com.jnjuste.hospitalms.models.Doctor;
import com.jnjuste.hospitalms.repositories.DoctorRepository;
import com.jnjuste.hospitalms.services.DoctorService;
import com.jnjuste.hospitalms.utils.PasswordEncryptionUtil;
import com.jnjuste.hospitalms.utils.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final DoctorRegNumberServiceImpl doctorRegNumberServiceImpl;
    private final EmailServiceImpl emailServiceImpl;



    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository, DoctorRegNumberServiceImpl doctorRegNumberServiceImpl, EmailServiceImpl emailServiceImpl) {
        this.doctorRepository = doctorRepository;
        this.doctorRegNumberServiceImpl = doctorRegNumberServiceImpl;
        this.emailServiceImpl = emailServiceImpl;
    }

    @Override
    public Doctor saveDoctor(Doctor doctor) {
        String generatedPassword = PasswordGenerator.generateRandomPassword(8);
        String encryptedPassword = PasswordEncryptionUtil.encryptPassword(generatedPassword);
        doctor.setPassword(encryptedPassword);
        String regNumber = doctorRegNumberServiceImpl.getNextRegNumber();
        doctor.setRegNumber(regNumber);
        Doctor savedDoctor = doctorRepository.save(doctor);
        emailServiceImpl.sendEmail(doctor.getEmail(), "Welcome to Our Hospital's Medical Staff",
                "Dear Dr. " + doctor.getFirstName() + ",\n\n" +
                        "Welcome to our hospital's medical staff! We're delighted to have you join our team of healthcare professionals.\n\n" +
                        "Your account for the Hospital Management System has been created. Here are your details:\n\n" +
                        "Registration Number: " + regNumber + "\n" +
                        "Username: " + doctor.getEmail() + "\n" +
                        "Password: " + generatedPassword + "\n\n" +
                        "For security purposes, please change your password when you first log in.\n\n" +
                        "If you encounter any issues or need assistance, please contact our IT support department.\n\n" +
                        "We look forward to your contributions to our patients' care and well-being.\n\n" +
                        "Best regards,\n" +
                        "Hospital Administration");
        return savedDoctor;
    }


    @Override
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @Override
    public Optional<Doctor> getDoctorById(UUID id) {
        return doctorRepository.findById(id);
    }

    @Override
    public Doctor updateDoctor(UUID id, Doctor doctorDetails) {
        return doctorRepository.findById(id).map(existingDoctor -> {
            // Update Doctor fields
            // The doctorID, RegNumber and NationalID remain unchanged
            StringBuilder updatedFields = new StringBuilder();

            if (!existingDoctor.getFirstName().equals(doctorDetails.getFirstName())) {
                existingDoctor.setFirstName(doctorDetails.getFirstName());
                updatedFields.append("First Name: ").append(doctorDetails.getFirstName()).append("\n");
            }
            if (!existingDoctor.getLastName().equals(doctorDetails.getLastName())) {
                existingDoctor.setLastName(doctorDetails.getLastName());
                updatedFields.append("Last Name: ").append(doctorDetails.getLastName()).append("\n");
            }
            if (!existingDoctor.getEmail().equals(doctorDetails.getEmail())) {
                existingDoctor.setEmail(doctorDetails.getEmail());
                updatedFields.append("Email: ").append(doctorDetails.getEmail()).append("\n");
            }
            if (!existingDoctor.getGender().equals(doctorDetails.getGender())) {
                existingDoctor.setGender(doctorDetails.getGender());
                updatedFields.append("Gender: ").append(doctorDetails.getGender()).append("\n");
            }
            if (!existingDoctor.getSpecialty().equals(doctorDetails.getSpecialty())) {
                existingDoctor.setSpecialty(doctorDetails.getSpecialty());
                updatedFields.append("Specialty: ").append(doctorDetails.getSpecialty()).append("\n");
            }
            if (!existingDoctor.getEmploymentType().equals(doctorDetails.getEmploymentType())) {
                existingDoctor.setEmploymentType(doctorDetails.getEmploymentType());
                updatedFields.append("Employment Type: ").append(doctorDetails.getEmploymentType()).append("\n");
            }

            Doctor updatedDoctor = doctorRepository.save(existingDoctor);

            String emailBody = "Dear Dr. " + existingDoctor.getFirstName() + ",\n\n" +
                    "The following details of your profile have been updated:\n" + updatedFields.toString() +
                    "\nThank you,\n" +
                    "Hospital Administration";
            emailServiceImpl.sendEmail(existingDoctor.getEmail(), "Profile Update Notification", emailBody);

            return updatedDoctor;
        }).orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));
    }


    @Override
    public void deleteDoctor(UUID id) {
        doctorRepository.deleteById(id);
    }

    @Override
    public Optional<Doctor> getDoctorByEmail(String email) {
        return doctorRepository.findByEmail(email);
    }

    @Override
    public Optional<Doctor> getDoctorByNationalID(Integer nationalID) {
        return doctorRepository.findByNationalID(nationalID);
    }

    @Override
    public Optional<Doctor> getDoctorByRegNumber(String regNumber) {
        return doctorRepository.findByRegNumber(regNumber);
    }
}