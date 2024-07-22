package com.jnjuste.hospitalms.repositories;

import com.jnjuste.hospitalms.models.AppointmentNumberSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentNumberRepository extends JpaRepository<AppointmentNumberSequence, Long> {
}
