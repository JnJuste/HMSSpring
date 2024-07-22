package com.jnjuste.hospitalms.services.impl;

import com.jnjuste.hospitalms.models.AppointmentNumberSequence;
import com.jnjuste.hospitalms.repositories.AppointmentNumberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppointmentNumberServiceImpl {
    private final AppointmentNumberRepository appointmentNumberRepository;
    private static final String PREFIX = "APP";
    @Autowired
    public AppointmentNumberServiceImpl(AppointmentNumberRepository appointmentNumberRepository) {
        this.appointmentNumberRepository = appointmentNumberRepository;
    }
    @Transactional
    public String getNextAppointmentNumber(){
        AppointmentNumberSequence sequence = appointmentNumberRepository.findAll().stream().findFirst().orElse(null);

        if (sequence == null) {
            sequence = new AppointmentNumberSequence();
            sequence.setLastAppointNumber("0000-00");
        }

        String lastAppointNumber = sequence.getLastAppointNumber();
        String[] parts = lastAppointNumber.split("-");
        int part1 = Integer.parseInt(parts[0]);
        int part2 = Integer.parseInt(parts[1]);

        if (part2 < 99) {
            part2++;
        } else {
            part2 = 0;
            part1++;
        }

        String nextAppointmentNumber = String.format("%04d-%02d", part1, part2);
        sequence.setLastAppointNumber(nextAppointmentNumber);
        appointmentNumberRepository.save(sequence);

        return PREFIX + nextAppointmentNumber;
    }
}
