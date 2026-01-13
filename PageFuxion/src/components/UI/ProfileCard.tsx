import React from "react";
import { MessageCircle } from "lucide-react";
import type { ContactInfo } from "../../types";
import ActionButton from "../UI/ActionButton";

interface ProfileCardProps {
  responsible: ContactInfo;
}

const ProfileCard: React.FC<ProfileCardProps> = ({ responsible }) => {
  const waLink = `https://wa.me/${responsible.phone}?text=${encodeURIComponent(
    responsible.whatsappMessage
  )}`;

  return (
    <div className="bg-white rounded-2xl p-5 shadow-sm border border-gray-100 mb-4">
      <div className="flex items-center gap-4 mb-5">
        <div className="w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center text-blue-600 text-xl font-bold shrink-0">
          {responsible.name.charAt(0)}
        </div>
        <div>
          <h3 className="font-bold text-gray-900">{responsible.name}</h3>
          <p className="text-sm text-gray-500">{responsible.role}</p>
        </div>
      </div>

      <ActionButton
        icon={MessageCircle}
        label="Escribir al WhatsApp"
        href={waLink}
        variant="whatsapp"
      />
    </div>
  );
};

export default ProfileCard;
