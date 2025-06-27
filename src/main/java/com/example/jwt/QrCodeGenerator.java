package com.example.jwt;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.logging.Logger;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QrCodeGenerator {

    private static final Logger logger = Logger.getLogger(QrCodeGenerator.class.getName());

    private QrCodeGenerator() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void generate(String text, String filePath) throws Exception {
        Objects.requireNonNull(text, "QR content text must not be null");
        Objects.requireNonNull(filePath, "File path must not be null");

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix matrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 300, 300);

        Path path = Path.of(filePath);
        Files.createDirectories(path.getParent()); // Ensure folder exists
        MatrixToImageWriter.writeToPath(matrix, "PNG", path);

        logger.log(java.util.logging.Level.INFO, "QR code generated at: {0}", path.toAbsolutePath());
    }
}

