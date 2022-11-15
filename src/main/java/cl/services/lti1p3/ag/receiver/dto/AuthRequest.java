package cl.services.lti1p3.ag.receiver.dto;

import javax.validation.constraints.NotBlank;

public record AuthRequest(
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

 