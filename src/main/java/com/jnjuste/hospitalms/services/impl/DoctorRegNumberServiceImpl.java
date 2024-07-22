package com.jnjuste.hospitalms.services.impl;

import com.jnjuste.hospitalms.models.DoctorRegNumberSequence;
import com.jnjuste.hospitalms.repositories.DoctorRegNumberSequenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DoctorRegNumberServiceImpl {

    private final DoctorRegNumberSequenceRepository doctorRegNumberSequenceRepository;
    @Autowired
    public DoctorRegNumberServiceImpl(DoctorRegNumberSequenceRepository doctorRegNumberSequenceRepository) {
        this.doctorRegNumberSequenceRepository = doctorRegNumberSequenceRepository;
    }

    @Transactional
    public String getNextRegNumber() {
        DoctorRegNumberSequence sequence = doctorRegNumberSequenceRepository.findAll().stream().findFirst().orElse(null);

        if (sequence == null) {
            sequence = new DoctorRegNumberSequence();
            sequence.setLastRegNumber("0000-00");
        }

        String lastRegNumber = sequence.getLastRegNumber();
        String[] parts = lastRegNumber.split("-");
        int part1 = Integer.parseInt(parts[0]);
        int part2 = Integer.parseInt(parts[1]);

        if (part2 < 99) {
            part2++;
        } else {
            part2 = 0;
            part1++;
        }

        String nextRegNumber = String.format("%04d-%02d", part1, part2);
        sequence.setLastRegNumber(nextRegNumber);
        doctorRegNumberSequenceRepository.save(sequence);

        return nextRegNumber;
    }
}
