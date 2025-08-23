// Uygulamanın ana bileşeni. Kullanıcı login ve CRUD işlemlerini yönetir.
import React, { useState } from "react";
import LoginPage from "./components/LoginPage";
import UserCrudPage from "./components/UserCrudPage";

// App bileşeni, login ve kullanıcı yönetimi arayüzünü içerir
function App() {
  // JWT token bilgisini state ve localStorage'da tutuyoruz
  const [token, setToken] = useState(localStorage.getItem("token") || "");

  // Kullanıcı login olduğunda çalışır
  const handleLogin = (jwt) => {
    setToken(jwt);
    localStorage.setItem("token", jwt);
  };

  // Kullanıcı çıkış yaptığında çalışır
  const handleLogout = () => {
    setToken("");
    localStorage.removeItem("token");
  };

  // Arayüzü render ediyoruz
  return (
    <div className="container">
      <h1>User Management</h1>
      {!token ? (
        <LoginPage onLogin={handleLogin} />
      ) : (
        <UserCrudPage token={token} onLogout={handleLogout} />
      )}
    </div>
  );
}

export default App;
