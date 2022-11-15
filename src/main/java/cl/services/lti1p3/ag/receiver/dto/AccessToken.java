package cl.services.lti1p3.ag.receiver.dto;

import javax.validation.constraints.NotBlank;

public record AccessToken(
		@NotBlank
        String access_token,
        @NotBlank
        String token_type,
        @NotBlank
        Long expires_in,
        @NotBlank
        String scope        
		) {
}

 