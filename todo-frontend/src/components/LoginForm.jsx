import React, { useState } from 'react';
import api from '../services/api';

const LoginForm = ({ onLoginSuccess }) => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

 const handleLogin = async (e) => {
    e.preventDefault();
    try {
  const response = await api.post('/login', { email, password });
      const { token } = response.data;
      
      localStorage.setItem('token', token);
      
      onLoginSuccess();
    } catch (error) { 
  if (error.response) {
    // Sunucudan gelen hata yanıtı
    setError(error.response.data); // Hata mesajını doğrudan göster
  } else if (error.request) {
    // İstek yapıldı ancak yanıt alınamadı
    setError('Sunucuya ulaşılamıyor.');
  } else {
    // İstek ayarlanırken hata oluştu
    setError('Bir hata oluştu: ' + error.message);
  }
  console.error(error); // Geliştirme için hatayı konsola yazdır
}
};

  return (
    <form onSubmit={handleLogin}>
      <h2>Giriş Yap</h2>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <div>
        <label>E-posta:</label>
        <input
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />
      </div>
      <div>
        <label>Şifre:</label>
        <input
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
      </div>
      <button type="submit">Giriş Yap</button>
    </form>
  );
};

export default LoginForm;
