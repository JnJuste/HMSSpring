package com.jnjuste.hospitalms.repositories;

import com.jnjuste.hospitalms.models.Appointment;
import com.jnjuste.hospitalms.models.Doctor;
import com.jnjuste.hospitalms.models.Nurse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    Optional<Appointment> findByAppointmentNumber(String appointmentNumber);

    List<Appointment> findByRegisteredBy(Nurse nurse);

    List<Appointment> findByDoctor(Doctor doctor);
}
