

package com.example.jwt;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

public class ParseTokenExample {

    private static final Logger logger = Logger.getLogger(ParseTokenExample.class.getName());

    public static void main(String[] args) {
        // Base64 key (must match signing key used during creation)
        String base64Key = "4ICwz4mg5LYzpNFwNvx4BADqiceQM2jdgdubtWipXl0=";

        // JWT token to parse (should be fresh & signed with above key)
        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJraXJhTkAzMjEiLCJOYW1lIjoiS2lyYW4gTWFyYWRhbmEiLCJSb2xlIjoiQWRtaW4iLCJpc3MiOiJLaXJhbiBNYXJhZGFuYSIsImF1ZCI6IktpcmFuIE1hcmFkYW5hIEF1ZGllbmNlIiwianRpIjoiMTIxMTIxMTIxIiwiaWF0IjoxNzUxMDE5ODIwLCJleHAiOjE3NTE4ODM4MjB9.H3b4XPJfIDgCzpGNlEW4bVNEUA3GoOp5V3kSUM2z9Zg";
        byte[] keyBytes = Decoders.BASE64.decode(base64Key);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);

        Jws<Claims> jws = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt);

        Claims claims = jws.getBody();
        JwsHeader<?> header = jws.getHeader();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        logger.info("✅ Parsed JWT Claims:");
        if (logger.isLoggable(java.util.logging.Level.INFO)) {
            logger.info(String.format("Subject (sub): %s", claims.getSubject()));
        }
        if (logger.isLoggable(java.util.logging.Level.INFO)) {
            logger.info(String.format("Name: %s", claims.get("Name")));
        }
        if (logger.isLoggable(java.util.logging.Level.INFO)) {
            logger.info(String.format("Role: %s", claims.get("Role")));
        }
        if (logger.isLoggable(java.util.logging.Level.INFO)) {
            logger.info(String.format("Issuer (iss): %s", claims.getIssuer()));
        }
        if (logger.isLoggable(java.util.logging.Level.INFO)) {
            logger.info(String.format("Audience (aud): %s", claims.getAudience()));
        }
        if (logger.isLoggable(java.util.logging.Level.INFO)) {
            logger.info(String.format("JWT ID (jti): %s", claims.getId()));
        }

        Date issuedAt = claims.getIssuedAt();
        Date expiration = claims.getExpiration();

        if (logger.isLoggable(java.util.logging.Level.INFO)) {
            logger.info(String.format("Issued At (iat): %s", issuedAt != null ? sdf.format(issuedAt) : "null"));
        }
        if (logger.isLoggable(java.util.logging.Level.INFO)) {
            logger.info(String.format("Expiration (exp): %s", expiration != null ? sdf.format(expiration) : "null"));
        }

        if (logger.isLoggable(java.util.logging.Level.INFO)) {
            logger.info(String.format("Header Algorithm (alg): %s", header.getAlgorithm()));
        }
        if (logger.isLoggable(java.util.logging.Level.INFO)) {
            logger.info(String.format("Base64 Key Used: %s", base64Key));
        }
        if (logger.isLoggable(java.util.logging.Level.INFO)) {
            logger.info(String.format("Full JWT Token: %s", jwt));
        }
    }
}
