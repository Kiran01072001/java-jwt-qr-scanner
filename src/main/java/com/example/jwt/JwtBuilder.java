package com.example.jwt;

import java.util.Date;

import io.jsonwebtoken.SignatureAlgorithm;

public interface JwtBuilder {

	JwtBuilder setSubject(String sub);

	JwtBuilder claim(String name, Object value);
	
	JwtBuilder setIssuer(String issuer);

	JwtBuilder setIssuedAt(Date date);

	JwtBuilder setExpiration( Date date);

	JwtBuilder signWith( SignatureAlgorithm algorithm, byte[] keyBytes);

	JwtBuilder setAudience( String audience);

	JwtBuilder setId(String id);

	String compact();

}
