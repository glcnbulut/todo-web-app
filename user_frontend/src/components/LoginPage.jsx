// React ve useState hook'unu içe aktarıyoruz
import React, { useState } from "react";
import axios from "axios";

function LoginPage({ onLogin }) {
  // Kullanıcı adı, şifre ve hata mesajı için state tanımlıyoruz
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  // Form gönderildiğinde çalışır
  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    // Kullanıcı adı veya şifre boşsa hata göster
    if (!username || !password) {
      setError("Kullanıcı adı ve şifre gereklidir.");
      return;
    }
    // Giriş fonksiyonunu çağır
    onLogin(username, password);
  };

  // Arayüzü render ediyoruz
  return (
    <div className="login-container">
      <h2>Kullanıcı Girişi</h2>
      <form onSubmit={handleSubmit}>
        {/* Kullanıcı adı inputu */}
        <input
          type="text"
          placeholder="Kullanıcı Adı"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />
        {/* Şifre inputu */}
        <input
          type="password"
          placeholder="Şifre"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        {/* Hata mesajı gösterimi */}
        {error && <div className="error">{error}</div>}
        <button type="submit">Giriş Yap</button>
      </form>
    </div>
  );
}

// Bileşeni dışa aktarıyoruz
export default LoginPage;
