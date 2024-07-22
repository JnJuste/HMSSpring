package com.jnjuste.hospitalms.services.impl;

import com.jnjuste.hospitalms.models.PatientNumberSequence;
import com.jnjuste.hospitalms.repositories.PatientNumberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PatientNumberServiceImpl {
    private final PatientNumberRepository patientNumberRepository;
    private static final String PREFIX = "PA";
    @Autowired
    public PatientNumberServiceImpl(PatientNumberRepository patientNumberRepository) {
        this.patientNumberRepository = patientNumberRepository;
    }
    @Transactional
    public String getNextPatientNumber(){
        PatientNumberSequence sequence = patientNumberRepository.findAll().stream().findFirst().orElse(null);

        if (sequence == null) {
            sequence = new PatientNumberSequence();
            sequence.setLastPatientNumber("0000-00");
        }

        String lastPatientNumber = sequence.getLastPatientNumber();
        String[] parts = lastPatientNumber.split("-");
        int part1 = Integer.parseInt(parts[0]);
        int part2 = Integer.parseInt(parts[1]);

        if (part2 < 99) {
            part2++;
        } else {
            part2 = 0;
            part1++;
        }

        String nextPatientNumber = String.format("%04d-%02d", part1, part2);
        sequence.setLastPatientNumber(nextPatientNumber);
        patientNumberRepository.save(sequence);

        return PREFIX + nextPatientNumber;
    }
}
