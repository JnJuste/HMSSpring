package com.jnjuste.hospitalms.services;

import com.jnjuste.hospitalms.models.Appointment;
import com.jnjuste.hospitalms.models.enums.AppointmentStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentService {
    Appointment saveAppointment(Appointment appointment);
    List<Appointment> getAllAppointments();
    Optional<Appointment> getAppointmentById(UUID id);
    Appointment updateAppointment(UUID id, Appointment appointmentDetails);
    void deleteAppointment(UUID id);
    Optional<Appointment> getAppointmentByAppointmentNumber (String appointmentNumber);

    // For Doctor Login Only
    List<Appointment> getScheduledAppointmentsByDoctor(UUID doctorId);
    Optional<Appointment> updateAppointmentStatus(UUID appointmentId, AppointmentStatus status);
}