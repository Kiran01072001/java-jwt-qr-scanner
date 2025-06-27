package com.example.jwt;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import javax.crypto.SecretKey;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

public class JwtHttpServer {

    private static final Logger logger = Logger.getLogger(JwtHttpServer.class.getName());

    public static void main(String[] args) throws IOException {
        int port = 8080;
        String host = "192.168.1.10";
        HttpServer server = HttpServer.create(new InetSocketAddress(host, port), 0);
        server.createContext("/api/parse", new JwtParseHandler());
        server.setExecutor(null);
        if (logger.isLoggable(java.util.logging.Level.INFO)) {
            logger.info(String.format("Server started at http://%s:%d/api/parse", host, port));
        }
        server.start();
    }

    static class JwtParseHandler implements HttpHandler {
        private static final String BASE64_KEY = "4ICwz4mg5LYzpNFwNvx4BADqiceQM2jdgdubtWipXl0=";
        private static final SecretKey SIGNING_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(BASE64_KEY));
        private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
                return;
            }

            String query = exchange.getRequestURI().getQuery();
            String token = (query != null && query.startsWith("token=")) ? query.substring(6) : null;

            if (token == null || token.isEmpty()) {
                sendHtmlResponse(exchange, 400, "<h3>❌ Missing or invalid token</h3>");
                return;
            }

            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(SIGNING_KEY)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                String html = buildHtml(token, claims);
                sendHtmlResponse(exchange, 200, html);

            } catch (JwtException e) {
                sendHtmlResponse(exchange, 401, "<h3>❌ Invalid or expired JWT token</h3><p>" + e.getMessage() + "</p>");
            }
        }

        private void sendHtmlResponse(HttpExchange exchange, int statusCode, String body) throws IOException {
            byte[] response = body.getBytes();
            exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
            exchange.getResponseHeaders().add("Cache-Control", "no-store, no-cache, must-revalidate");
            exchange.getResponseHeaders().add("Pragma", "no-cache");
            exchange.getResponseHeaders().add("Expires", "0");

            exchange.sendResponseHeaders(statusCode, response.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response);
            }
        }

        private String buildHtml(String token, Claims claims) {
            boolean isExpired = claims.getExpiration() != null && claims.getExpiration().before(new Date());
            String expClass = isExpired ? "expiration-expired" : "expiration-valid";

            StringBuilder html = new StringBuilder();

            html.append("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>JWT Parser</title>\n")
                .append("<style>\n")
                .append("    body { font-family: 'Segoe UI', sans-serif; background:#f0f2f5; padding:40px; color:#333; }\n")
                .append("    .container { background:#fff; border-radius:8px; padding:30px; margin:20px auto; max-width:800px; box-shadow:0 2px 8px rgba(0,0,0,0.1); }\n")
                .append("    h2 { font-size:24px; margin-bottom:20px; }\n")
                .append("    textarea { width:100%; height:100px; padding:12px; font-family:monospace; font-size:14px; border-radius:4px; background:#fafafa; }\n")
                .append("    button { background:#007bff; color:#fff; padding:10px 16px; border:none; border-radius:4px; cursor:pointer; }\n")
                .append("    button:hover { background:#0056b3; }\n")
                .append("    ul { list-style:none; padding:0; }\n")
                .append("    li { display:flex; justify-content:space-between; padding:10px 0; border-bottom:1px solid #eee; }\n")
                .append("    .label { font-weight:bold; color:#555; }\n")
                .append("    .expiration-valid { color:green; }\n")
                .append("    .expiration-expired { color:red; }\n")
                .append("    .btn-group { margin-top:15px; }\n")
                .append("</style>\n")
                .append("<script>\n")
                .append("    function copyToken() {\n")
                .append("        navigator.clipboard.writeText('").append(token).append("').then(() => alert('Token copied to clipboard!'));\n")
                .append("    }\n")
                .append("    function downloadJson() {\n")
               

                .append("        const data = JSON.parse('").append(escapeJsonForJS(toJson(claims))).append("');\n")

                .append("        const blob = new Blob([JSON.stringify(data, null, 2)], {type: 'application/json'});\n")
                .append("        const url = URL.createObjectURL(blob);\n")
                .append("        const a = document.createElement('a');\n")
                .append("        a.href = url;\n")
                .append("        a.download = 'jwt-claims.json';\n")
                .append("        a.click();\n")
                .append("        URL.revokeObjectURL(url);\n")
                .append("    }\n")
                .append("</script>\n")
                .append("</head><body>\n");

            html.append("<div class='container'>")
                .append("<h2>🔐 Parsed JWT Details</h2>")
                .append("<p><strong>Raw JWT Token:</strong></p>")
                .append("<textarea readonly>").append(token).append("</textarea>")
                .append("<div class='btn-group'><button onclick='copyToken()'>📋 Copy Token</button></div>")
                .append("</div>");

            html.append("<div class='container'>")
                .append("<h2>📜 JWT Claims</h2><ul>")
                .append(claim("🆔 User ID (sub)", claims.getSubject()))
                .append(claim("👤 Name", claims.get("Name")))
                .append(claim("🔐 Role", claims.get("Role")))
                .append(claim("📢 Issuer (iss)", claims.getIssuer()))
                .append(claim("🎯 Audience (aud)", claims.getAudience()))
                .append(claim("🔖 ID (jti)", claims.getId()))
                .append(claim("⏰ Issued At (iat)", format(claims.getIssuedAt())))
                .append("<li><span class='label'>⏳ Expiration (exp):</span> <span class='")
                .append(expClass)
                .append("'>")
                .append(format(claims.getExpiration()))
                .append("</span></li>")
                .append("</ul><div class='btn-group'><button onclick='downloadJson()'>💾 Download Claims as JSON</button></div>")
                .append("</div></body></html>");

            return html.toString();
        }

        private String claim(String label, Object value) {
            return "<li><span class='label'>" + label + ":</span> <span>" + (value != null ? value : "null") + "</span></li>";
        }

        private String format(Date date) {
            return date != null ? sdf.format(date) : "null";
        }

        private String toJson(Claims claims) {
            // Return a valid JavaScript object literal as a string
            return "{"
                + "\"sub\": \"" + escapeJson(claims.getSubject()) + "\","
                + "\"Name\": \"" + escapeJson(String.valueOf(claims.get("Name"))) + "\","
                + "\"Role\": \"" + escapeJson(String.valueOf(claims.get("Role"))) + "\","
                + "\"iss\": \"" + escapeJson(claims.getIssuer()) + "\","
                + "\"aud\": \"" + escapeJson(claims.getAudience()) + "\","
                + "\"jti\": \"" + escapeJson(claims.getId()) + "\","
                + "\"iat\": \"" + escapeJson(format(claims.getIssuedAt())) + "\","
                + "\"exp\": \"" + escapeJson(format(claims.getExpiration())) + "\""
                + "}";
        }

                private String escapeJson(String value) {
                    if (value == null) return "";
                    return value.replace("\\", "\\\\").replace("\"", "\\\"");
                }

                private String escapeJsonForJS(String json) {
    return json.replace("\\", "\\\\").replace("'", "\\'").replace("\"", "\\\"");
}

            }
        }

