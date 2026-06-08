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

export const formatDateTimeForBackend = (d, t) => {
  if(d === null || t === null) {
    return "";
  }
  return `${d}T${t}:00`;
}

export const formatDateTimeForFrontend = (dateTime) => {
  if (!dateTime) return "";

  return dateTime.replace("T", " ").slice(0, 16);
};

export const getCurrentWeekRange = () => {
  const now = new Date();

  // get Monday of current week
  const start = new Date(now);
  const day = start.getDay(); // 0 = Sunday
  const diff = day === 0 ? -6 : 1 - day;
  start.setDate(start.getDate() + diff);

  // get Sunday of current week
  const end = new Date(start);
  end.setDate(start.getDate() + 6);

  const format = (date) =>
    date.toLocaleDateString("en-US", {
      month: "short",
      day: "numeric",
    });

  return `${format(start)} – ${format(end)}`;
};


