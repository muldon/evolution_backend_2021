package com.github.eduoliveiradev.evolution.dto;

import javax.validation.constraints.NotBlank;

public record LoginRequest2(
		@NotBlank
        String grant_type,
        @NotBlank
        String client_assertion_type,
        @NotBlank
        String client_assertion,   //the Consumer's generated JWT (tool)
        @NotBlank
        String scope        
		) {
}

 