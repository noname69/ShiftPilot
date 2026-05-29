export const formatTime = (t) => t?.slice(0, 5) ?? "";

export const formatDate = (d) => {
  if (!d) return "";

  const date = new Date(d);

  if (isNaN(date.getTime())) return "";

  return date.toLocaleDateString("en-US", {
    month: "short",
    day: "numeric",
    year: "numeric",
  });
};

export const formatDateTimeForBackend = (d, t) => `${d}T${t}:00`;
