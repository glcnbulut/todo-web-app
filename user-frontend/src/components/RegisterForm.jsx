// Kullanıcı kayıt formu - user_api'ye yeni kullanıcı ekler
import React, { useState } from 'react';
import axios from 'axios';

const RegisterForm = () => {
  const [form, setForm] = useState({
    name: '',
    surname: '',
    email: '',
    password: ''
  });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    try {
      // user_api'deki /api/users endpointine POST isteği
  await axios.post('http://localhost:8081/api/users/add', form);
      setSuccess('Kayıt başarılı! Giriş yapabilirsiniz.');
      setForm({ name: '', surname: '', email: '', password: '' });
    } catch (err) {
      setError('Kayıt başarısız: ' + (err.response?.data?.message || err.message));
    }
  };

  return (
    <form onSubmit={handleSubmit} style={{
      background: '#fff',
      padding: 40,
      borderRadius: 16,
      boxShadow: '0 4px 24px #0001',
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
      gap: 18,
      width: '100%',
      maxWidth: 400,
      margin: '0 auto'
    }}>
      <h2 style={{ fontSize: 32, fontWeight: 700, color: '#4f8cff', marginBottom: 12 }}>Kayıt Ol</h2>
      <input name="name" value={form.name} onChange={handleChange} placeholder="Ad" required style={{ width: '90%', fontSize: 20, padding: 12, borderRadius: 8, border: '1px solid #ddd', marginBottom: 4 }} />
      <input name="surname" value={form.surname} onChange={handleChange} placeholder="Soyad" required style={{ width: '90%', fontSize: 20, padding: 12, borderRadius: 8, border: '1px solid #ddd', marginBottom: 4 }} />
      <input name="email" type="email" value={form.email} onChange={handleChange} placeholder="E-posta" required style={{ width: '90%', fontSize: 20, padding: 12, borderRadius: 8, border: '1px solid #ddd', marginBottom: 4 }} />
      <input name="password" type="password" value={form.password} onChange={handleChange} placeholder="Şifre" required style={{ width: '90%', fontSize: 20, padding: 12, borderRadius: 8, border: '1px solid #ddd', marginBottom: 4 }} />
      <button type="submit" style={{ width: '90%', fontSize: 22, padding: '14px 0', borderRadius: 8, background: '#43e97b', color: '#fff', border: 'none', fontWeight: 600, cursor: 'pointer', boxShadow: '0 2px 8px #43e97b33', marginTop: 8 }}>Kayıt Ol</button>
      {error && <div style={{ color: 'red', marginTop: 8 }}>{error}</div>}
      {success && <div style={{ color: 'green', marginTop: 8 }}>{success}</div>}
    </form>
  );
};

export default RegisterForm;
