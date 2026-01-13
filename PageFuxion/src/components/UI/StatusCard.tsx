import React from "react";
import { CheckCircle, XCircle, Clock } from "lucide-react";
import type { WarehouseStatus } from "../../types"; // Importamos la interfaz

interface StatusCardProps {
  status: WarehouseStatus;
}

const StatusCard: React.FC<StatusCardProps> = ({ status }) => {
  const isOpen = status.isOpen;

  return (
    <div
      className={`relative overflow-hidden rounded-2xl p-6 mb-6 transition-all duration-300 ${
        isOpen
          ? "bg-gradient-to-br from-green-500 to-green-600 shadow-lg shadow-green-200"
          : "bg-gradient-to-br from-red-500 to-red-600 shadow-lg shadow-red-200"
      }`}
    >
      {/* Icono de fondo decorativo */}
      <div className="absolute -right-4 -top-4 opacity-20">
        {isOpen ? (
          <CheckCircle size={120} className="text-white" />
        ) : (
          <XCircle size={120} className="text-white" />
        )}
      </div>

      <div className="relative z-10 text-white">
        <div className="flex items-center gap-2 mb-2">
          <span
            className={`inline-flex items-center px-3 py-1 rounded-full text-xs font-bold uppercase tracking-wider bg-white/20 backdrop-blur-sm border border-white/30`}
          >
            {isOpen ? "Abierto" : "Cerrado"}
          </span>
          {isOpen && (
            <span className="flex h-3 w-3 relative">
              <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-white opacity-75"></span>
              <span className="relative inline-flex rounded-full h-3 w-3 bg-green-200"></span>
            </span>
          )}
        </div>

        <h2 className="text-2xl font-bold mb-1 leading-tight">
          {isOpen ? status.messageOpen : status.messageClosed}
        </h2>

        {!isOpen && status.returnTime && (
          <div className="mt-4 flex items-center gap-2 bg-white/10 p-3 rounded-lg backdrop-blur-sm">
            <Clock size={18} />
            <p className="font-medium text-sm">{status.returnTime}</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default StatusCard;
