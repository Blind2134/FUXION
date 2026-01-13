import React, { useEffect, useState } from "react";
import { Package, Clock, Loader2 } from "lucide-react"; // Importamos Loader2 para la carga
import { storeData } from "./config/storeData";
import StatusCard from "./components/UI/StatusCard"; // Ajusta la ruta si es necesario
import LocationCard from "./components/UI/LocationCard";
import ProfileCard from "./components/UI/ProfileCard";

// ⚠️ PEGA AQUÍ LA URL DE TU "GOOGLE APPS SCRIPT" (La que termina en /exec)
// Si aún no tienes el script, usa la del CSV, pero la del Script es mejor para el Admin Panel.
const API_URL =
  "https://script.google.com/macros/s/AKfycbxPpfDA0lJd6GGapvEk1Ok-W0t9eMp909EsJyapFGoIz-ihg945huKwYpuQLTnc8641/exec";

const App: React.FC = () => {
  // 1. Datos estáticos (Ubicación, Info, Responsable)
  const { info, location, responsible, schedule } = storeData;

  // 2. Estado dinámico para el Estatus (Inicia con el valor por defecto)
  const [currentStatus, setCurrentStatus] = useState(storeData.status);
  const [loading, setLoading] = useState(true);

  // 3. Efecto: Al cargar la página, preguntamos a Google el estado real
  useEffect(() => {
    const fetchStatus = async () => {
      try {
        const response = await fetch(API_URL);
        const data = await response.json(); // Leemos la respuesta JSON del script

        // Actualizamos el estado local con lo que nos dijo Google
        setCurrentStatus((prev) => ({
          ...prev,
          isOpen: data.isOpen === true, // Aseguramos que sea booleano
          // Si está cerrado, usamos el mensaje de Google. Si está abierto, mantenemos el default.
          messageClosed:
            data.isOpen === false ? data.message : prev.messageClosed,
          returnTime: data.isOpen === false ? data.message : "",
        }));
      } catch (error) {
        console.error("Error conectando con el almacén:", error);
        // Si falla, no pasa nada: se queda con la configuración por defecto (storeData)
      } finally {
        setLoading(false); // Terminó de cargar (con éxito o error)
      }
    };

    fetchStatus();
  }, []);

  // 4. Pantalla de Carga (Opcional, pero se ve pro)
  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <div className="text-blue-600 animate-spin">
          <Loader2 size={48} strokeWidth={1.5} />
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 flex justify-center font-sans text-gray-900">
      {/* Contenedor tipo móvil centrado */}
      <div className="w-full max-w-md bg-white min-h-screen shadow-2xl overflow-hidden flex flex-col">
        {/* Encabezado */}
        <header className="px-6 pt-10 pb-6 text-center bg-white">
          <div className="w-16 h-16 bg-blue-600 rounded-2xl mx-auto flex items-center justify-center text-white mb-5 shadow-lg shadow-blue-200 rotate-3 transition-transform hover:rotate-0">
            <Package size={32} strokeWidth={1.5} />
          </div>
          <h1 className="text-2xl font-bold text-gray-900 leading-tight mb-2">
            {info.title}
          </h1>
          <p className="text-gray-500 text-sm">{info.subtitle}</p>
        </header>

        {/* Contenido Principal con Scroll */}
        <main className="flex-1 px-4 pb-8">
          {/* 1. Estado (Dinámico desde Google) */}
          <StatusCard status={currentStatus} />

          {/* 2. Ubicación */}
          <LocationCard location={location} />

          {/* 3. Responsable */}
          <ProfileCard responsible={responsible} />

          {/* 4. Información Extra (Horario) */}
          <div className="bg-gray-50 rounded-2xl p-5 border border-gray-100 mt-6">
            <h3 className="text-gray-400 text-xs font-bold uppercase tracking-wider mb-3 flex items-center gap-2">
              <Clock size={14} /> Horario de Atención
            </h3>
            <p className="font-bold text-gray-800 text-lg mb-1">
              {schedule.days}
            </p>
            <p className="text-gray-600">{schedule.hours}</p>

            {schedule.note && (
              <div className="mt-3 pt-3 border-t border-gray-200">
                <p className="text-xs text-gray-400 italic">
                  * {schedule.note}
                </p>
              </div>
            )}
          </div>
        </main>

        {/* Footer */}
        <footer className="py-6 text-center border-t border-gray-100 bg-gray-50">
          <p className="text-xs text-gray-400 font-medium">
            © {new Date().getFullYear()} – Almacén Fuxion
          </p>
          <p className="text-[10px] text-gray-300 mt-1">
            Sistema informativo v1.0
          </p>
        </footer>
      </div>
    </div>
  );
};

export default App;
