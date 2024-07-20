package com.jnjuste.hospitalms.services.impl;

import com.jnjuste.hospitalms.models.Scheduler;
import com.jnjuste.hospitalms.models.enums.AppointmentStatus;
import com.jnjuste.hospitalms.repositories.SchedulerRepository;
import com.jnjuste.hospitalms.services.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SchedulerServiceImpl implements SchedulerService {

    private final SchedulerRepository schedulerRepository;

    @Autowired
    public SchedulerServiceImpl(SchedulerRepository schedulerRepository) {
        this.schedulerRepository = schedulerRepository;
    }
    @Override
    public Scheduler scheduleAppointment(Scheduler scheduler) {
        // Add logic to check doctor availability, working hours, etc.
        return schedulerRepository.save(scheduler);
    }

    @Override
    public Scheduler getAppointmentById(UUID id) {
        return schedulerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
    }

    @Override
    public List<Scheduler> getAllAppointments() {
        return schedulerRepository.findAll();
    }

    @Override
    public Scheduler updateAppointment(UUID id, Scheduler scheduler) {
        Scheduler existingAppointment = getAppointmentById(id);
        existingAppointment.setDoctor(scheduler.getDoctor());
        existingAppointment.setPatient(scheduler.getPatient());
        existingAppointment.setAppointmentTime(scheduler.getAppointmentTime());
        existingAppointment.setConsultationDuration(scheduler.getConsultationDuration());
        existingAppointment.setStatus(scheduler.getStatus());
        existingAppointment.setReason(scheduler.getReason());
        existingAppointment.setScheduledBy(scheduler.getScheduledBy());
        return schedulerRepository.save(existingAppointment);
    }

    @Override
    public void cancelAppointment(UUID id) {
        Scheduler appointment = getAppointmentById(id);
        appointment.setStatus(AppointmentStatus.CANCELLED);
        schedulerRepository.save(appointment);
    }
}