package com.gateway.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gateway.models.AuthResponce;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private Logger logger =LoggerFactory.getLogger(AuthController.class);
	
	@GetMapping("/login")
	public ResponseEntity<AuthResponce>login( @RegisteredOAuth2AuthorizedClient("okta") OAuth2AuthorizedClient client,
			                                  @AuthenticationPrincipal OidcUser user, Model model )
	{
		
		logger.info("user email id :{}",user.getEmail());
		
		//creating auth response object----->>>>
		AuthResponce authResponce=new AuthResponce();
		
        //setting email to authresponse
	    authResponce.setUserId(user.getEmail());
	    
	    //setting token to auth response------>>>>
	    authResponce.setAccessToken(client.getAccessToken().getTokenValue());
	    
	    authResponce.setRefreshToken(client.getRefreshToken().getTokenValue());
	    
	    authResponce.setExpireAt(client.getAccessToken().getExpiresAt().getEpochSecond());
	    
	    List<String> authorities = user.getAuthorities().stream().map(grantedAuthority -> {return grantedAuthority.getAuthority();})
	    .collect(Collectors.toList());
		
	    authResponce.setAuthorites(authorities);
	    
	    return new ResponseEntity<>(authResponce,HttpStatus.OK);
	}
}
