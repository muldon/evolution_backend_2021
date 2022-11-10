package com.github.eduoliveiradev.evolution.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.eduoliveiradev.evolution.dto.AccessToken;
import com.github.eduoliveiradev.evolution.dto.LoginRequest;
import com.github.eduoliveiradev.evolution.dto.LoginRequest2;
import com.github.eduoliveiradev.evolution.service.ClienteService;

@RestController
public class LoginController {
    private final ClienteService clienteService;

    public LoginController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    @RequestMapping("/login")
    public String login(@Validated @RequestBody LoginRequest loginRequest) {
    	String token = clienteService.getToken(loginRequest); 
        return token;
    }
    
    
    @RequestMapping(
    		path= "/auth/test/{target}",
    		method = RequestMethod.POST,
    		consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE},
    		produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public String loginTest(@PathVariable String target, LoginRequest2 loginRequest2) throws GeneralSecurityException, IOException {
    	
    	System.out.println(loginRequest2+" - "+target);
    	String token = clienteService.getToken3(); 
        return token;
    }
    
    @RequestMapping(
    		path= "/auth/platform/token/{target}",
    		method = RequestMethod.POST,
    		consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE},
    		produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public AccessToken login2(@PathVariable String target, LoginRequest2 loginRequest2) throws GeneralSecurityException, IOException {
    	
    	System.out.println(loginRequest2+" - "+target);
    	//LoginRequest lr = new LoginRequest("aaaa@dadfdf", "stridddng");
    	AccessToken token = clienteService.getToken2(loginRequest2); 
        return token;
    }
}
