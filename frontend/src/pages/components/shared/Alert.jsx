import { FaExclamationTriangle, FaCheckCircle, FaInfoCircle } from "react-icons/fa";

const variants = {
  error: {
    wrapper: "bg-red-50 text-red-700 border-red-200",
    icon: <FaExclamationTriangle className="text-red-500" />,
  },
  success: {
    wrapper: "bg-green-50 text-green-700 border-green-200",
    icon: <FaCheckCircle className="text-green-500" />,
  },
  info: {
    wrapper: "bg-blue-50 text-blue-700 border-blue-200",
    icon: <FaInfoCircle className="text-blue-500" />,
  },
};

const Alert = ({ type = "info", message }) => {
  if (!message) return null;

  const variant = variants[type] || variants.info;

  return (
    <div
      className={`w-full mb-3 flex items-start gap-2 rounded-md border px-3 py-2 text-sm ${variant.wrapper}`}
    >
      <div className="mt-0.5">{variant.icon}</div>
      <div>{message}</div>
    </div>
  );
};

export default Alert;