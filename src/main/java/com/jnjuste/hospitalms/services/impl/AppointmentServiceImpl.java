package com.jnjuste.hospitalms.services.impl;

import com.jnjuste.hospitalms.models.Appointment;
import com.jnjuste.hospitalms.models.Doctor;
import com.jnjuste.hospitalms.models.Nurse;
import com.jnjuste.hospitalms.repositories.AppointmentRepository;
import com.jnjuste.hospitalms.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentNumberServiceImpl appointmentNumberServiceImpl;

    @Autowired
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, AppointmentNumberServiceImpl appointmentNumberServiceImpl) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentNumberServiceImpl = appointmentNumberServiceImpl;
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

    @Override
    public List<Appointment> getAppointmentsByDoctor(Doctor doctor) {
        return appointmentRepository.findByDoctor(doctor);
    }

    @Override
    public List<Appointment> getAppointmentsByNurse(Nurse nurse) {
        return appointmentRepository.findByRegisteredBy(nurse);
    }

}