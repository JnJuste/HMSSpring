package com.jnjuste.hospitalms.services.impl;

import com.jnjuste.hospitalms.models.RegNumberSequence;
import com.jnjuste.hospitalms.repositories.RegNumberSequenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegNumberServiceImpl {

    private final RegNumberSequenceRepository regNumberSequenceRepository;
    @Autowired
    public RegNumberServiceImpl(RegNumberSequenceRepository regNumberSequenceRepository) {
        this.regNumberSequenceRepository = regNumberSequenceRepository;
    }

    @Transactional
    public String getNextRegNumber() {
        RegNumberSequence sequence = regNumberSequenceRepository.findAll().stream().findFirst().orElse(null);

        if (sequence == null) {
            sequence = new RegNumberSequence();
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
        regNumberSequenceRepository.save(sequence);

        return nextRegNumber;
    }
}
