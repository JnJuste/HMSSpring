package com.jnjuste.hospitalms.services.impl;

import com.jnjuste.hospitalms.models.Appointment;
import com.jnjuste.hospitalms.models.enums.AppointmentStatus;
import com.jnjuste.hospitalms.repositories.AppointmentRepository;
import com.jnjuste.hospitalms.services.AppointmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    private static final Logger logger = LoggerFactory.getLogger(AppointmentServiceImpl.class);

    private final AppointmentRepository appointmentRepository;
    private final AppointmentNumberServiceImpl appointmentNumberServiceImpl;
    private final EmailServiceImpl emailServiceImpl;

    @Autowired
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, AppointmentNumberServiceImpl appointmentNumberServiceImpl, EmailServiceImpl emailServiceImpl) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentNumberServiceImpl = appointmentNumberServiceImpl;
        this.emailServiceImpl = emailServiceImpl;
    }

    @Override
    public Appointment saveAppointment(Appointment appointment) {
        appointment.setAppointmentNumber(appointmentNumberServiceImpl.getNextAppointmentNumber());
        Appointment savedAppointment = appointmentRepository.save(appointment);
        sendAppointmentEmails(savedAppointment);
        return savedAppointment;
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    public Optional<Appointment> getAppointmentById(UUID id) {
        return appointmentRepository.findById(id);
    }

    @Override
    public Appointment updateAppointment(UUID id, Appointment appointmentDetails) {
        return appointmentRepository.findById(id).map(existingAppointment -> {
            // Only update fields that are allowed to be changed
            existingAppointment.setDoctor(appointmentDetails.getDoctor());
            existingAppointment.setPatient(appointmentDetails.getPatient());
            existingAppointment.setStartTime(appointmentDetails.getStartTime());
            existingAppointment.setEndTime(appointmentDetails.getEndTime());
            existingAppointment.setDurationMinutes(appointmentDetails.getDurationMinutes());
            existingAppointment.setReason(appointmentDetails.getReason());
            existingAppointment.setRegisteredBy(appointmentDetails.getRegisteredBy());
            existingAppointment.setStatus(appointmentDetails.getStatus());
            // Don't update doctor or patient here. Changing these should be a separate operation
            return appointmentRepository.save(existingAppointment);
        }).orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));
    }

    @Override
    public void deleteAppointment(UUID id) {
        appointmentRepository.deleteById(id);
    }

    @Override
    public Optional<Appointment> getAppointmentByAppointmentNumber(String appointmentNumber) {
        return appointmentRepository.findByAppointmentNumber(appointmentNumber);
    }

    // Doctor Scheduled Appointments
    @Override
    public List<Appointment> getScheduledAppointmentsByDoctor(UUID doctorId) {
        return appointmentRepository.findByDoctor_DoctorIDAndStatus(doctorId, AppointmentStatus.SCHEDULED);
    }

    @Override
    public Optional<Appointment> updateAppointmentStatus(UUID appointmentId, AppointmentStatus status) {
        return appointmentRepository.findById(appointmentId).map(appointment -> {
            appointment.setStatus(status);
            return appointmentRepository.save(appointment);
        });
    }

    @Override
    public void sendAppointmentEmails(Appointment appointment) {
        // Email to Doctor
        String doctorEmail = appointment.getDoctor().getEmail();
        if (doctorEmail != null && !doctorEmail.isEmpty()) {
            String doctorSubject = "New Appointment Scheduled";
            String doctorBody = "Dear Dr. " + appointment.getDoctor().getFirstName() + ",\n\n" +
                    "A new appointment has been scheduled.\n\n" +
                    "Patient Details:\n" +
                    "First Name: " + appointment.getPatient().getFirstName() + "\n" +
                    "Last Name: " + appointment.getPatient().getLastName() + "\n" +
                    "Patient Number: " + appointment.getPatient().getPatientNumber() + "\n\n" +
                    "Appointment Details:\n" +
                    "Start Time: " + appointment.getStartTime() + "\n" +
                    "End Time: " + appointment.getEndTime() + "\n" +
                    "Reason: " + appointment.getReason() + "\n\n" +
                    "Best regards,\n" +
                    "Hospital Administration";

            logger.info("Sending appointment email to doctor: {}", doctorEmail);
            emailServiceImpl.sendEmail(doctorEmail, doctorSubject, doctorBody);
            logger.info("Appointment email sent to doctor: {}", doctorEmail);
        } else {
            logger.error("Doctor email is null or empty for appointment: {}", appointment.getAppointmentNumber());
        }

        // Email to Patient
        String patientEmail = appointment.getPatient().getEmail();
        if (patientEmail != null && !patientEmail.isEmpty()) {
            String patientSubject = "New Appointment Scheduled";
            String patientBody = "Dear " + appointment.getPatient().getFirstName() + ",\n\n" +
                    "A new appointment has been scheduled.\n\n" +
                    "Doctor Details:\n" +
                    "First Name: " + appointment.getDoctor().getFirstName() + "\n" +
                    "Last Name: " + appointment.getDoctor().getLastName() + "\n" +
                    "Registration Number: " + appointment.getDoctor().getRegNumber() + "\n\n" +
                    "Nurse Details:\n" +
                    "Registered By: Nurse " + appointment.getRegisteredBy().getFirstName() + " " + appointment.getRegisteredBy().getLastName() + "\n\n" +
                    "Appointment Details:\n" +
                    "Start Time: " + appointment.getStartTime() + "\n" +
                    "End Time: " + appointment.getEndTime() + "\n" +
                    "Reason: " + appointment.getReason() + "\n\n" +
                    "Best regards,\n" +
                    "Hospital Administration";

            logger.info("Sending appointment email to patient: {}", patientEmail);
            emailServiceImpl.sendEmail(patientEmail, patientSubject, patientBody);
            logger.info("Appointment email sent to patient: {}", patientEmail);
        } else {
            logger.error("Patient email is null or empty for appointment: {}", appointment.getAppointmentNumber());
        }
    }
}
