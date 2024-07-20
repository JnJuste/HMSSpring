package com.jnjuste.hospitalms.models;


import jakarta.persistence.*;
import lombok.*;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "nurses")
@PrimaryKeyJoinColumn(name = "user_ID")
public class Nurse extends User {
    // No additional fields for now
}