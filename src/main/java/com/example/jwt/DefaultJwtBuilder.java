
package com.example.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class DefaultJwtBuilder implements JwtBuilder {

    private final Map<String, Object> claims = new HashMap<>();
    private Date issuedAt;
    private Date expiration;
    private SignatureAlgorithm algorithm;
    private Key signingKey;
  
    @Override
    public JwtBuilder setId(String id) {
        this.claims.put("jti", id);
        return this;
    }

    @Override
    public JwtBuilder setAudience(String audience) {
        this.claims.put("aud", audience);
        return this;
    }
  

    @Override
    public JwtBuilder setSubject(String sub) {
        this.claims.put("sub", sub);
        return this;
    }

   @Override
   public JwtBuilder setIssuer(String issuer) {
       this.claims.put("iss", issuer);
       return this;
    }

    @Override
    public JwtBuilder setIssuedAt(Date date) {
        this.issuedAt = date;
        return this;
    }

    @Override
    public JwtBuilder setExpiration(Date date) {
        this.expiration = date;
        return this;
    }

    @Override
    public JwtBuilder claim(String name, Object value) {
        this.claims.put(name, value);
        return this;
    }

    @Override
    public JwtBuilder signWith(SignatureAlgorithm alg, byte[] secretKey) {
        this.algorithm = alg;
        this.signingKey = new SecretKeySpec(secretKey, alg.getJcaName());
        return this;
    }

    @Override
    public String compact() {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(signingKey, algorithm)
                .compact();
    }
}
