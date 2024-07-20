package com.jnjuste.hospitalms.services;

import com.jnjuste.hospitalms.models.Scheduler;

import java.util.List;
import java.util.UUID;

public interface SchedulerService {
    Scheduler scheduleAppointment(Scheduler scheduler);
    Scheduler getAppointmentById(UUID id);
    List<Scheduler> getAllAppointments();
    Scheduler updateAppointment(UUID id, Scheduler scheduler);
    void cancelAppointment(UUID id);
}
