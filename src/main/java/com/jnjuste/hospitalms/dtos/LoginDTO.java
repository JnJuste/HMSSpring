package com.jnjuste.hospitalms.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginDTO {
    private String email;
    private String password;

    // Getters and Setters implemented using lombok
}