package com.jnjuste.hospitalms.models;

import com.jnjuste.hospitalms.models.enums.EmploymentType;
import com.jnjuste.hospitalms.models.enums.Specialty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@Entity
public class Doctor extends User {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmploymentType employmentType;

    @ElementCollection(targetClass = Specialty.class)
    @CollectionTable(name = "doctor_specialties", joinColumns = @JoinColumn(name = "doctor_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "specialty", nullable = false)
    private Set<Specialty> specialties;

    @Column(nullable = false)
    private String licenseNumber;

    @Column(nullable = false)
    private int yearsOfExperience;

    @Column(length = 1000)
    private String biography;

    @Column(nullable = false)
    private boolean isAvailable;
}