import React, { useState } from "react";
import LoginPage from "./components/LoginPage";
import UserCrudPage from "./components/UserCrudPage";

function App() {
  const [token, setToken] = useState(localStorage.getItem("token") || "");

  const handleLogin = (jwt) => {
    setToken(jwt);
    localStorage.setItem("token", jwt);
  };

  const handleLogout = () => {
    setToken("");
    localStorage.removeItem("token");
  };

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
