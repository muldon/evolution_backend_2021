//*********************************************************************
//
//                       Copyright (C) 2022-2022
//                       Carnegie Learning Inc.
//
//                       All Rights Reserved.
//
//This program is the subject of trade secrets and intellectual
//property rights owned by Carnegie Learning.
//
//This legend must continue to appear in the source code despite
//modifications or enhancements by any party.
//
//*********************************************************************

package cl.services.lti1p3.ag.receiver.utils;

import java.net.URL;
import java.security.Key;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nimbusds.jose.jwk.AsymmetricJWK;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SigningKeyResolverAdapter;

public class Lti13Utils {
	protected static final Logger log = LoggerFactory.getLogger(Lti13Utils.class);	
	 

	public static Jws<Claims> parseJWT(String JWKsURL, String jwt) {
		
		return Jwts.parser().setSigningKeyResolver(new SigningKeyResolverAdapter() {

			// This is done because each state is signed with a different key based on the issuer... so
			// we don't know the key and we need to check it pre-extracting the claims and finding the kid
			@Override
			public Key resolveSigningKey(JwsHeader header, Claims claims) {

				// If the platform has a JWK Set endpoint. We try that.
				if (!StringUtils.isBlank(JWKsURL)) {
					try {
						// local URLs does not work - needs to be public
						// e.g.: https://lti-public-devkit.dev.gcp-eu.taocloud.org/lti1p3/.well-known/jwks/toolSet.json
						URL url = new URL(JWKsURL);
						
						JWKSet publicKeys = JWKSet.load(url);
						
						// for local tests load the JWKs like this
						//JWKSet publicKeys = JWKSet.load(new File("/LOCAL_PATH/toolSet.json"));
						
						JWK jwk = publicKeys.getKeyByKeyId(header.getKeyId());
						return ((AsymmetricJWK) jwk).toPublicKey();
					} catch (Exception ex) {
						log.error("Error getting the iss public key", ex);
						return null;
					}
				}
				log.error("The platform configuration must contain a valid JWKS. Using our own public key... ");
				return null;

			}
		}).parseClaimsJws(jwt);
	}

	 
}
