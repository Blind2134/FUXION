// src/types/index.ts

export interface WarehouseStatus {
  isOpen: boolean;
  messageOpen: string;
  messageClosed: string;
  returnTime?: string; // Opcional, solo si está cerrado
}

export interface ContactInfo {
  name: string;
  role: string;
  phone: string; // Formato internacional para link de WhatsApp (ej: 51999...)
  whatsappMessage: string;
  avatarUrl?: string; // Por si quieres poner foto después
}

export interface LocationInfo {
  addressText: string;
  addressToCopy?: string;
  googleMapsLink: string;
  coordinates: {
    lat: number;
    lng: number;
  };
}

export interface ScheduleInfo {
  days: string;
  hours: string;
  note: string;
}

export interface AppConfig {
  info: {
    title: string;
    subtitle: string;
  };
  status: WarehouseStatus;
  responsible: ContactInfo;
  location: LocationInfo;
  schedule: ScheduleInfo;
}
