package com.example.jwt;

import java.util.Date;
import java.util.logging.Logger;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

public class JwtTokenExample {

    private static final Logger logger = Logger.getLogger(JwtTokenExample.class.getName());

    public static void main(String[] args) {
        // Use same key as JwtHttpServer
        String base64Key = "4ICwz4mg5LYzpNFwNvx4BADqiceQM2jdgdubtWipXl0=";
        byte[] keyBytes = Decoders.BASE64.decode(base64Key);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);

        // Log Base64 key (optional, already known)
        logger.log(java.util.logging.Level.INFO, "Base64 Encoded Signing Key: {0}", base64Key);

        // Build the JWT
        String jwt = Jwts.builder()
                .setSubject("kiraN@321")
                .claim("Name", "Kiran Maradana")
                .claim("Role", "Admin")
                .setIssuer("Kiran Maradana")
                .setAudience("Kiran Maradana Audience")
                .setId("121121121")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 10)) // 10-day expiry
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

       // Replace with your actual PC IP address
String pcIp = "192.168.1.10"; 

// Compose the URL that will be embedded in the QR code
String url = "http://" + pcIp + ":8080/api/parse?token=" + jwt;

// Define the path where the QR code image will be saved
String filePath = "target/jwt_qrcode.png";

try {
    // Generate the QR code and save it to the specified file path
    QrCodeGenerator.generate(url, filePath);
    logger.log(java.util.logging.Level.INFO, "QR Code saved at: {0}", filePath);
} catch (Exception e) {
    logger.log(java.util.logging.Level.SEVERE, "Failed to generate QR Code: {0}", e.getMessage());
}

// Log the JWT token for reference
logger.log(java.util.logging.Level.INFO, "Generated JWT Token: {0}", jwt);
    }
}