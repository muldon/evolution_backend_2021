package com.github.eduoliveiradev.evolution.dto;

import javax.validation.constraints.NotBlank;

public record Score(
        @NotBlank
        String userId,   //MUST be the same as the resource link LTI parameter 'user_id'.
         
        @NotBlank
        String activityProgress,
         
        @NotBlank
        String gradingProgress,
       
        @NotBlank
        Float scoreGiven,
        
        @NotBlank
        Float scoreMaximum,
        
        @NotBlank
        String comment        
                
         ) {
}
