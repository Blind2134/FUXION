import type { AppConfig } from "../types";

export const storeData: AppConfig = {
  info: {
    title: "Centro de Gestión Fuxion",
    subtitle: "Recepción, control y despacho de productos",
  },
  // ==========================================
  // 🟢 🔴 ESTADO DEL ALMACÉN (EDITAR AQUÍ)
  // ==========================================
  status: {
    isOpen: true, // <--- CAMBIAR A false PARA CERRAR
    messageOpen: "✅ Almacén Operativo. Recepción activa.",
    messageClosed: "⛔ Almacén Cerrado temporalmente.",
    returnTime: "Regreso estimado: 8:30 p.m.",
  },
  // ==========================================
  responsible: {
    name: "Bryan Quispe Palma",
    role: "Encargado de Almacén",
    phone: "913779891", // Reemplaza con tu número real
    whatsappMessage:
      "Hola Bryan, soy socio y necesito coordinar una entrega en el almacén.",
  },
  location: {
    addressText: "Victor Andres Belaunde Mz M Lt 12 Comite 10",
    googleMapsLink: "https://maps.app.goo.gl/DL3ufkk8Mydtf1wf6",
    addressToCopy: "VA Belaunde Y18-A",
    coordinates: {
      lat: -16.347307,
      lng: -71.560091,
    },
  },
  schedule: {
    days: "Lunes a Domingo",
    hours: "8:00 a.m. – 10:00 p.m.",
    note: "El horario puede variar por salidas o inconvenientes.",
  },
};
