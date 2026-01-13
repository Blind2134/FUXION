import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import App from "./App"; // Tu Landing Page Pública
import AdminPanel from "./components/admin/AdminPanel"; // Tu Panel Privado
import "./index.css";

ReactDOM.createRoot(document.getElementById("root")!).render(
  <React.StrictMode>
    <BrowserRouter>
      <Routes>
        {/* Ruta Pública (Lo que ve todo el mundo) */}
        <Route path="/" element={<App />} />

        {/* Ruta Secreta (Solo para ti) */}
        {/* Puedes ponerle un nombre difícil de adivinar */}
        <Route path="/admin-bryan-secret" element={<AdminPanel />} />
      </Routes>
    </BrowserRouter>
  </React.StrictMode>
);
