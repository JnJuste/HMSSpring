package com.jnjuste.hospitalms.models;

import com.jnjuste.hospitalms.models.enums.EmploymentType;
import com.jnjuste.hospitalms.models.enums.NurseSpecialty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@Entity
public class Nurse extends User {
    @Column(nullable = false)
    private String licenseNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NurseSpecialty specialty;

    @Column(nullable = false)
    private int yearsOfExperience;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmploymentType employmentType;
}