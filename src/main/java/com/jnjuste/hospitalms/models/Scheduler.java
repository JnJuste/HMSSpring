package com.jnjuste.hospitalms.models;

import com.jnjuste.hospitalms.models.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Scheduler {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID scheduleID;

    @ManyToOne
    @JoinColumn(name = "doctorID", nullable = false)
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patientID", nullable = false)
    private Patient patient;

    @Column(nullable = false)
    private LocalDateTime appointmentTime;

    @Column(nullable = false)
    private int consultationDuration; // in minutes

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;

    private String reason;

    @ManyToOne
    @JoinColumn(name = "nurseID")
    private Nurse scheduledBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}