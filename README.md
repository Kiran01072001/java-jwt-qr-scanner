

# 🔐✨ JWT meets QR Code – in Java! 🚀

Ever wished you could **generate a JWT**, wrap it in a QR code, and have it parsed directly in your browser — *without worrying about CSRF or "funny business"*? 😅

This project brings together:

- A 🔧 **Java-powered JWT Generator**
- A 🖥️ Localhost HTTP Server that parses JWTs from QR-encoded URLs
- A clean, self-contained workflow — no frontend frameworks required

---

## 🧠 Features

- ✅ Generate JWTs with standard claims
- ✅ Sign tokens securely using HMAC-SHA256
- ✅ Encode the token in a URL and convert it into a QR code
- ✅ Host a local HTTP server that parses and displays JWT claims
- ✅ Copy the raw JWT or export its payload as JSON
- ✅ 100% built in **Java**

---

## 🔍 Use Cases

- 🧪 Local testing
- 🎤 Developer demos or presentations
- 📚 JWT debugging, learning, and education

---

## 🚀 Getting Started

1. Clone the repo
2. Run the project (`main` method starts server + generates token + QR)
3. Open the local URL in your browser to view parsed JWT details

> ℹ️ Note: The QR code points to `localhost`. It's not accessible once the server is stopped.

---

## 🛡️ Security

This tool **does not use any CSRF, remote fetches, or network tricks.** Everything runs on `localhost`. Tokens are generated and verified in memory.

---

## 📂 Tech Stack

- Java 17+
- JWT (io.jsonwebtoken)
- ZXing (QR Code generator)
- NanoHTTPD (lightweight HTTP server)

---

## ⭐️ Feedback

If you find this useful, drop a ⭐️ — it helps visibility! Contributions and suggestions welcome.

---

## 📜 License

MIT – feel free to use, adapt, and improve!

