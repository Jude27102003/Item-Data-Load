package com.item.security;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.item.exception.ItemServiceException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

//Provides methods to generate, validate, and parse JWT tokens.
@Component
public class JwtTokenProvider {

	@Value("${app.jwt.secret}")
	private String secretKey;
	@Value("${app.jwt.expiry-millis}")
	private long expiryMillis;

	//Generates a JWT token using the authenticated user's details.
	public String generateToken(Authentication authentication) {

		String username = authentication.getName();

		Date currentDate = new Date();

		Date expiryDate = new Date(currentDate.getTime() + expiryMillis);

		String token = Jwts.builder().subject(username).issuedAt(currentDate).expiration(expiryDate).signWith(key())
				.compact();

		return token;

	}

	//Returns the signing key derived from the secret key.
	private Key key() {

		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
	}

	//Extracts the username from the JWT token.
	public String getUsername(String token) {
		String username = Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(token).getPayload()
				.getSubject();

		return username;
	}

	//Validates the JWT token and throws custom exceptions for various errors.
	public boolean validateToken(String token) {

		try {

			Jwts.parser().verifyWith((SecretKey) key()).build().parse(token);

			return true;
		} catch (MalformedJwtException | SignatureException ex) {
			throw new ItemServiceException(HttpStatus.BAD_REQUEST, "Invalid token");

		} catch (ExpiredJwtException ex) {
			throw new ItemServiceException(HttpStatus.BAD_REQUEST, "Expired Token");

		} catch (UnsupportedJwtException ex) {
			throw new ItemServiceException(HttpStatus.BAD_REQUEST, "Unspported Token");

		} catch (IllegalArgumentException ex) {
			throw new ItemServiceException(HttpStatus.BAD_REQUEST, "Illegal String claim is null/empty");

		}
	}

}
