import React, { useState } from "react";
import { MapPin, Copy, Map, Check } from "lucide-react";
import type { LocationInfo } from "../../types";
import ActionButton from "../UI/ActionButton";

interface LocationCardProps {
  location: LocationInfo;
}

const LocationCard: React.FC<LocationCardProps> = ({ location }) => {
  const [copied, setCopied] = useState(false);

  const handleCopy = () => {
    const textToCopy = location.addressToCopy || location.addressText;
    navigator.clipboard.writeText(textToCopy);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  return (
    <div className="bg-white rounded-2xl p-5 shadow-sm border border-gray-100 mb-4">
      <h3 className="text-gray-500 text-xs font-bold uppercase tracking-wider mb-4 flex items-center gap-2">
        <MapPin size={16} /> Ubicación
      </h3>

      <p className="text-gray-800 font-medium text-lg leading-snug mb-6">
        {location.addressText}
      </p>

      <div className="space-y-3">
        <ActionButton
          icon={Map}
          label="Abrir en Google Maps"
          href={location.googleMapsLink}
          variant="maps"
        />

        <ActionButton
          icon={copied ? Check : Copy}
          label={copied ? "¡Dirección Copiada!" : "Copiar para inDrive"}
          onClick={handleCopy}
          variant="copy"
        />
      </div>
    </div>
  );
};

export default LocationCard;
