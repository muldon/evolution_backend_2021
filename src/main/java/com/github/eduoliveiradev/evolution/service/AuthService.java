package com.github.eduoliveiradev.evolution.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.github.eduoliveiradev.evolution.dto.AccessToken;
import com.github.eduoliveiradev.evolution.dto.AuthRequest;
import com.nimbusds.jose.JOSEException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthService {
    private final JwtEncoder encoder;
    
    @Value("${authorizationserver.auth.token.url}")
    private String authorizationtTokenURL; //the aud
    
    private @Autowired 
    Environment environment;

    public String getPropertyValue(String runTimeFlower) {
        return environment.resolvePlaceholders("${" + runTimeFlower + "}");
    }
   
    public AuthService(JwtEncoder encoder) {
       this.encoder = encoder;
    }
 
    public AccessToken getToken(AuthRequest authRequest, String target) throws GeneralSecurityException, IOException, ParseException, JOSEException, LTI1p3Exception {
       
    	//get the tool JWKs URL. 
    	String JWKsUrl = getPropertyValue(target+".tool.jwks.url");
    	
    	//TODO reuse Lti13Utils of the launcher project instead of having a copy of this method
    	//The tool signs the JWT with the private key.
    	//The tool’s JWKS URL exposes all the public keys, including the one pairing the private key it just used.
    	//Our platform, when receiving the JWT, checks its kid, extracts the public key from the tool’s JWKS URL, and verifies the signature.
    	Jws<Claims> jwtClaims = Lti13Utils.parseJWT(JWKsUrl, authRequest.client_assertion());
    	 
    	//validation rules
    	validateRequest(jwtClaims,target);
    	
    	
    	return createToken(authRequest);

     }
    
   
    //IMS specification: https://www.imsglobal.org/spec/security/v1p1#using-json-web-tokens-with-oauth-2-0-client-credentials-grant
    private void validateRequest(Jws<Claims> jwtClaims, String target) throws LTI1p3Exception {
    	Claims claims = jwtClaims.getBody();
    	  
    	/* 
    	 * 1-Aud
    	 * The aud Claim MUST contain a value that identifies the authorization server as an intended audience. 
    	 * The Consumer MAY use the token endpoint URL of the authorization server as a value for an aud element to identify the authorization server as an intended audience of the JWT. 
    	 * The authorization server MUST reject any JWT that does not contain its own identity as the intended audience. 
    	 * This information MUST be sent as an array even when there is only one value.
    	 */    	
    	List<String> audList = (ArrayList)claims.get("aud");
    	boolean validAud = audList.stream().anyMatch(authorizationtTokenURL::equals);
    	if(!validAud) {
    		//TODO - reuse or maintain a copy ? 
    		throw new LTI1p3Exception("aud ("+audList+") does not contain the token endpoint: "+authorizationtTokenURL).log(2);
    	}
    	
    	System.out.println();
		//String aud = jwtClaims.get
		
	}

	private AccessToken createToken(AuthRequest authRequest) throws GeneralSecurityException, IOException {
        Instant now = Instant.now();
        //long expiry = 36000L; // 10 horas
        long expiry = 300L; // 5 min
        String scope = "https://purl.imsglobal.org/spec/lti-ags/scope/score";
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject("AAAA")
                .claim("scope", scope)
                .build();
        String accessToken = encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        String tokenType = "bearer";
        AccessToken at = new AccessToken(accessToken, tokenType,expiry, scope);
        log.debug("createToken2: "+accessToken);
        return at;
    }
    
    
     
    
    private String getOwnKey(String keyName) throws IOException {
		String key = "";
		try (InputStream lpis = getClass().getResourceAsStream(keyName)) {
			if (lpis != null)
				key = new BufferedReader(new InputStreamReader(lpis, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
		} catch (IOException e1) {
			throw new FileNotFoundException(keyName);
		}
		if(StringUtils.isBlank(key)) {
			throw new IOException("Key "+keyName+" could not be read from class path");
		}
		return key;
	}
}
