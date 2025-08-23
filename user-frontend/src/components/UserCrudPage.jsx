// React ve gerekli hook'ları ile axios'u içe aktarıyoruz
import React, { useEffect, useState } from "react";
import axios from "axios";

function UserCrudPage({ token, onLogout }) {
  const [users, setUsers] = useState([]);
  // Form verisi için state tanımlıyoruz
  const [form, setForm] = useState({ name: "", email: "", password: "", surname: "", role: "USER" });
  const [editId, setEditId] = useState(null);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  // JWT header
  const authHeader = { headers: { Authorization: `Bearer ${token}` } };

  // Kullanıcıları API'den çeker
  const fetchUsers = async () => {
    try {
      const res = await axios.get("/api", authHeader);
      setUsers(res.data);
    } catch (err) {
      setError("Kullanıcılar alınamadı.");
    }
  };

  // Sayfa yüklendiğinde kullanıcıları getir
  useEffect(() => {
    fetchUsers();
    // eslint-disable-next-line
  }, []);

  // Form inputları değiştiğinde çalışır
  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  // Kullanıcı ekleme işlemi
  const handleAdd = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");
    try {
      await axios.post("/api/add", form, authHeader);
      setSuccess("Kullanıcı eklendi.");
      setForm({ name: "", email: "", password: "", surname: "", role: "USER" });
      fetchUsers();
    } catch (err) {
      setError("Kullanıcı eklenemedi.");
    }
  };

  // Kullanıcıyı düzenlemek için formu doldur
  const handleEdit = (user) => {
    setEditId(user.id);
    setForm({ name: user.name, email: user.email, password: user.password, surname: user.surname, role: user.role });
  };

  // Kullanıcı güncelleme işlemi
  const handleUpdate = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");
    try {
      await axios.put(`/api/update/${editId}`, form, authHeader);
      setSuccess("Kullanıcı güncellendi.");
      setEditId(null);
      setForm({ name: "", email: "", password: "", surname: "", role: "USER" });
      fetchUsers();
    } catch (err) {
      setError("Kullanıcı güncellenemedi.");
    }
  };

  // Kullanıcı silme işlemi
  const handleDelete = async (id) => {
    setError("");
    setSuccess("");
    try {
      await axios.delete(`/api/delete/${id}`, authHeader);
      setSuccess("Kullanıcı silindi.");
      fetchUsers();
    } catch (err) {
      setError("Kullanıcı silinemedi.");
    }
  };

  // Arayüzü render ediyoruz
  return (
    <div>
      <button onClick={onLogout} style={{ float: "right" }}>Çıkış Yap</button>
      <h2>Kullanıcılar</h2>
      {error && <div className="error">{error}</div>}
      {success && <div className="success">{success}</div>}
      <ul>
        {users.map((user) => (
          <li key={user.id}>
            {user.name} {user.surname} ({user.email}) - {user.role}
            <button onClick={() => handleEdit(user)} style={{ marginLeft: 8 }}>Düzenle</button>
            <button onClick={() => handleDelete(user.id)} style={{ marginLeft: 8, background: "#d32f2f" }}>Sil</button>
          </li>
        ))}
      </ul>
      <form onSubmit={editId ? handleUpdate : handleAdd}>
        <h3>{editId ? "Kullanıcı Güncelle" : "Kullanıcı Ekle"}</h3>
        <input name="name" placeholder="Ad" value={form.name} onChange={handleChange} required />
        <input name="surname" placeholder="Soyad" value={form.surname} onChange={handleChange} required />
        <input name="email" type="email" placeholder="E-posta" value={form.email} onChange={handleChange} required />
        <input name="password" type="password" placeholder="Şifre" value={form.password} onChange={handleChange} required />
        <select name="role" value={form.role} onChange={handleChange}>
          <option value="USER">USER</option>
          <option value="ADMIN">ADMIN</option>
        </select>
        <button type="submit">{editId ? "Güncelle" : "Ekle"}</button>
        {editId && <button type="button" onClick={() => { setEditId(null); setForm({ name: "", email: "", password: "", surname: "", role: "USER" }); }}>İptal</button>}
      </form>
    </div>
  );
}

export default UserCrudPage;
