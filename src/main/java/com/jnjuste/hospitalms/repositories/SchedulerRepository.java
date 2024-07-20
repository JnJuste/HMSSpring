package com.jnjuste.hospitalms.repositories;

import com.jnjuste.hospitalms.models.Scheduler;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SchedulerRepository extends JpaRepository<Scheduler, UUID> {
}
