import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { Lock, Unlock, Loader2, CheckCircle, ArrowLeft } from "lucide-react";

// PEGA AQUÍ TU URL
const API_URL =
  "https://script.google.com/macros/s/AKfycbxPpfDA0lJd6GGapvEk1Ok-W0t9eMp909EsJyapFGoIz-ihg945huKwYpuQLTnc8641/exec";

const AdminPanel: React.FC = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");

  // Estado para saber cómo está la tienda AHORA
  const [currentDbStatus, setCurrentDbStatus] = useState<boolean | null>(null);
  const [fetchingStatus, setFetchingStatus] = useState(true);

  // Estados para el Popup
  const [showModal, setShowModal] = useState(false);
  const [lastAction, setLastAction] = useState<"opened" | "closed">("opened");

  // 1. Efecto para leer el estado real al cargar la página
  useEffect(() => {
    fetchCurrentStatus();
  }, []);

  const fetchCurrentStatus = async () => {
    setFetchingStatus(true);
    try {
      const response = await fetch(API_URL);
      const data = await response.json();
      setCurrentDbStatus(data.isOpen === true);
    } catch (error) {
      console.error("Error leyendo estado:", error);
    } finally {
      setFetchingStatus(false);
    }
  };

  const updateStatus = async (isOpen: boolean) => {
    setLoading(true);
    try {
      await fetch(
        `${API_URL}?action=update&isOpen=${isOpen}&message=${encodeURIComponent(
          message
        )}`,
        {
          method: "POST",
          mode: "no-cors",
        }
      );

      setLastAction(isOpen ? "opened" : "closed");
      setCurrentDbStatus(isOpen); // Actualizamos el semáforo visualmente también
      setShowModal(true);
    } catch (error) {
      alert("❌ Error al actualizar");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-900 text-white flex flex-col items-center justify-center p-4 relative">
      {/* Botón Volver */}
      <button
        onClick={() => navigate("/")}
        className="absolute top-6 left-6 flex items-center gap-2 text-gray-400 hover:text-white transition-colors py-2 pr-4 rounded-lg"
      >
        <ArrowLeft size={20} />
        <span className="text-sm font-medium">Volver a la web</span>
      </button>

      <h1 className="text-2xl font-bold mb-6 mt-12">Panel de Control Fuxion</h1>

      <div className="w-full max-w-md space-y-6">
        {/* --- NUEVO: SEMÁFORO DE ESTADO ACTUAL --- */}
        <div className="bg-gray-800 rounded-xl p-4 border border-gray-700 flex items-center justify-between">
          <span className="text-gray-400 text-sm font-medium">
            Estado en la web:
          </span>

          {fetchingStatus ? (
            <div className="flex items-center gap-2 text-blue-400 text-sm">
              <Loader2 size={16} className="animate-spin" /> Cargando...
            </div>
          ) : (
            <div
              className={`flex items-center gap-2 px-3 py-1 rounded-full text-sm font-bold border ${
                currentDbStatus
                  ? "bg-green-500/10 text-green-400 border-green-500/30"
                  : "bg-red-500/10 text-red-400 border-red-500/30"
              }`}
            >
              {currentDbStatus ? (
                <>
                  <span className="relative flex h-2 w-2">
                    <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-green-400 opacity-75"></span>
                    <span className="relative inline-flex rounded-full h-2 w-2 bg-green-500"></span>
                  </span>
                  ABIERTO
                </>
              ) : (
                <>
                  <Lock size={12} /> CERRADO
                </>
              )}
            </div>
          )}
        </div>

        {/* Input Mensaje */}
        <div>
          <label className="block text-sm text-gray-400 mb-2">
            Mensaje (Opcional)
          </label>
          <input
            type="text"
            value={message}
            onChange={(e) => setMessage(e.target.value)}
            placeholder="Ej: Vuelvo en 20 min..."
            className="w-full p-4 rounded-xl bg-gray-800 border border-gray-700 focus:border-blue-500 outline-none text-white transition-colors"
          />
        </div>

        {/* Botones de Acción */}
        <div className="grid grid-cols-2 gap-4">
          <button
            onClick={() => updateStatus(true)}
            disabled={loading}
            className={`flex flex-col items-center justify-center gap-2 p-6 rounded-2xl transition-all disabled:opacity-50 disabled:scale-100 ${
              currentDbStatus
                ? "bg-gray-700 opacity-50 cursor-not-allowed"
                : "bg-green-600 hover:bg-green-700 active:scale-95"
            }`}
          >
            {loading ? (
              <Loader2 className="animate-spin" />
            ) : (
              <Unlock size={32} />
            )}
            <span className="font-bold text-lg">ABRIR</span>
          </button>

          <button
            onClick={() => updateStatus(false)}
            disabled={loading}
            className={`flex flex-col items-center justify-center gap-2 p-6 rounded-2xl transition-all disabled:opacity-50 disabled:scale-100 ${
              currentDbStatus === false
                ? "bg-gray-700 opacity-50 cursor-not-allowed"
                : "bg-red-600 hover:bg-red-700 active:scale-95"
            }`}
          >
            {loading ? (
              <Loader2 className="animate-spin" />
            ) : (
              <Lock size={32} />
            )}
            <span className="font-bold text-lg">CERRAR</span>
          </button>
        </div>

        <p className="text-center text-xs text-gray-500 mt-8">
          Modo Administrador - Bryan Guispe
        </p>
      </div>

      {/* --- POPUP (MODAL) --- */}
      {showModal && (
        <div className="fixed inset-0 bg-black/80 backdrop-blur-sm flex items-center justify-center z-50 p-4 animate-in fade-in duration-200">
          <div className="bg-gray-800 border border-gray-700 w-full max-w-sm rounded-2xl p-6 shadow-2xl transform transition-all scale-100 animate-in zoom-in-95 duration-200">
            <div className="flex flex-col items-center text-center">
              <div
                className={`mb-4 p-4 rounded-full ${
                  lastAction === "opened"
                    ? "bg-green-500/20 text-green-500"
                    : "bg-red-500/20 text-red-500"
                }`}
              >
                <CheckCircle size={48} />
              </div>
              <h3 className="text-xl font-bold text-white mb-2">
                {lastAction === "opened"
                  ? "¡Almacén Abierto!"
                  : "¡Almacén Cerrado!"}
              </h3>
              <p className="text-gray-400 mb-6 text-sm">
                {lastAction === "opened"
                  ? "La web ahora muestra que estás atendiendo."
                  : "La web ahora muestra el mensaje de cerrado."}
              </p>
              <button
                onClick={() => setShowModal(false)}
                className="w-full py-3 bg-blue-600 hover:bg-blue-700 text-white font-semibold rounded-xl transition-colors"
              >
                Entendido
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default AdminPanel;
