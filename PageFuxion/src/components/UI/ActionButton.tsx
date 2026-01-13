import React from "react";
import type { LucideIcon } from "lucide-react"; // Tipo para los iconos de Lucide

interface ActionButtonProps {
  icon?: LucideIcon; // Usamos el tipo correcto de Lucide
  label: string;
  onClick?: () => void;
  href?: string;
  variant?: "primary" | "secondary" | "whatsapp" | "maps" | "copy";
  className?: string;
}

const ActionButton: React.FC<ActionButtonProps> = ({
  icon: Icon,
  label,
  onClick,
  href,
  variant = "primary",
  className = "",
}) => {
  const baseStyles =
    "w-full flex items-center justify-center gap-3 py-3.5 px-6 rounded-xl font-bold transition-all active:scale-95 shadow-sm border";

  const variants = {
    primary:
      "bg-blue-600 text-white border-transparent hover:bg-blue-700 shadow-blue-200",
    secondary: "bg-white text-gray-700 border-gray-200 hover:bg-gray-50",
    whatsapp:
      "bg-green-500 text-white border-transparent hover:bg-green-600 shadow-green-200",
    maps: "bg-orange-500 text-white border-transparent hover:bg-orange-600 shadow-orange-200",
    copy: "bg-slate-800 text-white border-transparent hover:bg-slate-900",
  };

  const content = (
    <>
      {Icon && <Icon size={20} />} {/* size={20} es estándar en Lucide */}
      <span>{label}</span>
    </>
  );

  if (href) {
    return (
      <a
        href={href}
        target="_blank"
        rel="noopener noreferrer"
        className={`${baseStyles} ${variants[variant]} ${className}`}
      >
        {content}
      </a>
    );
  }

  return (
    <button
      onClick={onClick}
      className={`${baseStyles} ${variants[variant]} ${className}`}
    >
      {content}
    </button>
  );
};

export default ActionButton;
