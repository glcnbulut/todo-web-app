// Uygulamanın ana bileşeni. Kullanıcı login ve CRUD işlemlerini yönetir.
import React, { useState } from "react";
import LoginPage from "./components/LoginPage";
import UserCrudPage from "./components/UserCrudPage";
import RegisterForm from "./components/RegisterForm";

// App bileşeni, login ve kullanıcı yönetimi arayüzünü içerir
function App() {
  // JWT token bilgisini state ve localStorage'da tutuyoruz
  const [token, setToken] = useState(localStorage.getItem("token") || "");
  const [authMode, setAuthMode] = useState("none"); // "login" | "register" | "none"

  // Kullanıcı login olduğunda çalışır
  const handleLogin = (jwt) => {
    setToken(jwt);
    localStorage.setItem("token", jwt);
  };

  // Kullanıcı çıkış yaptığında çalışır
  const handleLogout = () => {
    setToken("");
    localStorage.removeItem("token");
    setAuthMode("none");
  };

  // Arayüzü render ediyoruz
  return (
    <div style={{
      minHeight: '100vh',
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
      justifyContent: 'center',
      background: 'linear-gradient(135deg, #e0eafc 0%, #cfdef3 100%)'
    }}>
      <h1 style={{ fontSize: 40, fontWeight: 700, marginBottom: 32, color: '#2d3a4a', letterSpacing: 2 }}>Kullanıcı Yönetimi</h1>
      <div style={{ width: 400, maxWidth: '90vw' }}>
        {!token ? (
          authMode === "none" ? (
            <div style={{ display: 'flex', flexDirection: 'column', gap: 24, alignItems: 'center', justifyContent: 'center', background: '#fff', padding: 40, borderRadius: 16, boxShadow: '0 4px 24px #0001' }}>
              <button style={{ width: '80%', fontSize: 22, padding: '16px 0', borderRadius: 8, background: '#4f8cff', color: '#fff', border: 'none', fontWeight: 600, cursor: 'pointer', boxShadow: '0 2px 8px #4f8cff33' }} onClick={() => setAuthMode("login")}>Giriş Yap</button>
              <button style={{ width: '80%', fontSize: 22, padding: '16px 0', borderRadius: 8, background: '#43e97b', color: '#fff', border: 'none', fontWeight: 600, cursor: 'pointer', boxShadow: '0 2px 8px #43e97b33' }} onClick={() => setAuthMode("register")}>Kayıt Ol</button>
            </div>
          ) : authMode === "login" ? (
            <LoginPage onLogin={handleLogin} />
          ) : (
            <RegisterForm />
          )
        ) : (
          <UserCrudPage token={token} onLogout={handleLogout} />
        )}
      </div>
    </div>
  );
}

export default App;
