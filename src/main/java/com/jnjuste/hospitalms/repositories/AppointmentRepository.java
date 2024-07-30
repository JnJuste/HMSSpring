package com.jnjuste.hospitalms.repositories;

import com.jnjuste.hospitalms.models.Appointment;
import com.jnjuste.hospitalms.models.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    Optional<Appointment> findByAppointmentNumber(String appointmentNumber);
    List<Appointment> findByDoctor_DoctorIDAndStatus(UUID doctorId, AppointmentStatus status);
}
