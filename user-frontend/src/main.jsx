import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App";
import "./index.css";

ReactDOM.createRoot(document.getElementById("root")).render(
  // Uygulamanın giriş noktası. App bileşenini root'a render eder.
  // React uygulamasını root elementine render ediyoruz
  <React.StrictMode>
    <App />
  </React.StrictMode>
);
