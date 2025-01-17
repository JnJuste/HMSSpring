package com.jnjuste.hospitalms.models;

import com.jnjuste.hospitalms.models.enums.EmploymentType;
import com.jnjuste.hospitalms.models.enums.Gender;
import com.jnjuste.hospitalms.models.enums.Role;
import com.jnjuste.hospitalms.models.enums.Specialty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Doctor{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID doctorID;

    @Column(nullable = false, unique = true)
    private String regNumber;

    @Column(nullable = false, unique = true)
    private Integer nationalID;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role = Role.DOCTOR;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Specialty specialty;

    @Enumerated(EnumType.STRING)
    private EmploymentType employmentType;

    @Column(nullable = false)
    private boolean firstLogin; // Add firstLogin flag
}