package com.jnjuste.hospitalms.repositories;

import com.jnjuste.hospitalms.models.NurseRegNumberSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NurseRegNumberSequenceRepository extends JpaRepository<NurseRegNumberSequence, Long> {
}
