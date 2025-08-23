import React, { useState } from "react";
import axios from "axios";

function LoginPage({ onLogin }) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    try {
      const res = await axios.post("/api/login", { email, password });
      if (res.data && res.data.token) {
        onLogin(res.data.token);
      } else {
        setError("Giriş başarısız. JWT alınamadı.");
      }
    } catch (err) {
      setError("Giriş başarısız. Bilgileri kontrol edin.");
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <h2>Giriş Yap</h2>
      {error && <div className="error">{error}</div>}
      <input
        type="email"
        placeholder="E-posta"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        required
      />
      <input
        type="password"
        placeholder="Şifre"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        required
      />
      <button type="submit">Giriş</button>
    </form>
  );
}

export default LoginPage;
