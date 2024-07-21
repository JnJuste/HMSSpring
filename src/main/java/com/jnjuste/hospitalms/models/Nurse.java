package com.jnjuste.hospitalms.models;

import com.jnjuste.hospitalms.models.enums.Gender;
import com.jnjuste.hospitalms.models.enums.Role;
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
public class Nurse{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userID;

    @Column(nullable = false, unique = true)
    private String regNumber;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToOne
    private Doctor doctor;
}