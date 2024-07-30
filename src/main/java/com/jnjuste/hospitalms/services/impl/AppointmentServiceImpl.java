package com.jnjuste.hospitalms.services.impl;

import com.jnjuste.hospitalms.models.Appointment;
import com.jnjuste.hospitalms.models.Doctor;
import com.jnjuste.hospitalms.models.Nurse;
import com.jnjuste.hospitalms.models.Patient;
import com.jnjuste.hospitalms.models.enums.AppointmentStatus;
import com.jnjuste.hospitalms.repositories.AppointmentRepository;
import com.jnjuste.hospitalms.repositories.NurseRepository;
import com.jnjuste.hospitalms.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentNumberServiceImpl appointmentNumberServiceImpl;
    private final NurseRepository nurseRepository;

    @Autowired
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, AppointmentNumberServiceImpl appointmentNumberServiceImpl, NurseRepository nurseRepository) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentNumberServiceImpl = appointmentNumberServiceImpl;
        this.nurseRepository = nurseRepository;
    }

    @Override
    public Appointment saveAppointment(Appointment appointment) {
        appointment.setAppointmentNumber(appointmentNumberServiceImpl.getNextAppointmentNumber());
        return appointmentRepository.save(appointment);
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
    public Optional<Appointment> createAppointment(Patient patient, Doctor doctor, LocalDateTime startTime, LocalDateTime endTime, String reason, UUID nurseId) {
        if (isScheduleConflict(doctor, startTime, endTime)) {
            return Optional.empty();
        }
        Nurse nurse = nurseRepository.findById(nurseId).orElseThrow(() -> new ResourceNotFoundException("Nurse not found with id: " + nurseId));
        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .startTime(startTime)
                .endTime(endTime)
                .durationMinutes((int) java.time.Duration.between(startTime, endTime).toMinutes())
                .reason(reason)
                .registeredBy(nurse)
                .status(AppointmentStatus.SCHEDULED)
                .build();
        return Optional.of(appointmentRepository.save(appointment));
    }

    @Override
    public boolean isScheduleConflict(Doctor doctor, LocalDateTime startTime, LocalDateTime endTime) {
        List<Appointment> appointments = appointmentRepository.findByDoctor_DoctorIDAndStatus(doctor.getDoctorID(), AppointmentStatus.SCHEDULED);
        return appointments.stream().anyMatch(appointment ->
                (appointment.getStartTime().isBefore(endTime) && appointment.getEndTime().isAfter(startTime))
        );
    }
}