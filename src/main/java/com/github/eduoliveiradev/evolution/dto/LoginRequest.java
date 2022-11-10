package com.github.eduoliveiradev.evolution.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public record LoginRequest(
        @Email
        String email,
        @NotBlank
        String password) {
}


 