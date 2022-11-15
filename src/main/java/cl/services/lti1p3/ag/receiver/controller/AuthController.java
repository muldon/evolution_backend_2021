package cl.services.lti1p3.ag.receiver.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.JOSEException;

import cl.services.lti1p3.ag.receiver.dto.AccessToken;
import cl.services.lti1p3.ag.receiver.dto.AuthRequest;
import cl.services.lti1p3.ag.receiver.exception.LTI1p3Exception;
import cl.services.lti1p3.ag.receiver.service.AuthService;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class AuthController {
	
	@Autowired
    private AuthService authService;

    
    @RequestMapping(
    		path= "/auth/platform/token/{target}",
    		method = RequestMethod.POST,
    		consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE},
    		produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public AccessToken authRequest(@PathVariable String target, AuthRequest authRequest) throws GeneralSecurityException, IOException, ParseException, JOSEException, LTI1p3Exception {
    	
    	log.info("authRequest: "+authRequest+" - for "+target);
    	AccessToken token = authService.getToken(authRequest,target); 
        return token;
    }
    
   
}
