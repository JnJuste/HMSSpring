package com.jnjuste.hospitalms.repositories;

import com.jnjuste.hospitalms.models.RegNumberSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegNumberSequenceRepository extends JpaRepository<RegNumberSequence, Long> {
}
