import { useState, useEffect } from "react";
import {
  startOfWeek,
  endOfWeek,
  addWeeks,
  subWeeks,
  format,
  eachDayOfInterval,
  isToday,
} from "date-fns";
import { FaChevronLeft, FaChevronRight } from "react-icons/fa";
import { getUserWeeklyShifts } from "../../api/shift"; // ← adjust path if needed
import Footer from "../components/shared/Footer";      // ← adjust path if needed

const UserSchedule = () => {
  const [currentWeekStart, setCurrentWeekStart] = useState(
    startOfWeek(new Date(), { weekStartsOn: 1 })
  );
  const [shifts, setShifts] = useState([]);
  const [leaveRequests, setLeaveRequests] = useState([]);
  const [isLoading, setIsLoading] = useState(false);

  const weekEnd = endOfWeek(currentWeekStart, { weekStartsOn: 1 });
  const weekDays = eachDayOfInterval({ start: currentWeekStart, end: weekEnd });

  useEffect(() => {
    const formattedStart = format(currentWeekStart, "yyyy-MM-dd");
    const formattedEnd = format(weekEnd, "yyyy-MM-dd");

    const fetchSchedule = async (start, end) => {
      setIsLoading(true);
      try {
        const data = await getUserWeeklyShifts({
          weekStart: start,
          weekEnd: end,
        });
        setShifts(data.shifts ?? []);
        setLeaveRequests(data.leaveRequests ?? []);
      } catch (error) {
        console.error("Failed to fetch schedule: ", error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchSchedule(formattedStart, formattedEnd);
  }, [currentWeekStart]);

  const handlePrevWeek = () =>
    setCurrentWeekStart((prev) => subWeeks(prev, 1));

  const handleNextWeek = () =>
    setCurrentWeekStart((prev) => addWeeks(prev, 1));

  const handleToday = () =>
    setCurrentWeekStart(startOfWeek(new Date(), { weekStartsOn: 1 }));

  const getShiftsForDay = (day) =>
    shifts
      .filter((s) => s.shiftDate === format(day, "yyyy-MM-dd"))
      .sort((a, b) => a.shiftStartTime.localeCompare(b.shiftStartTime));

  const getLeavesForDay = (day) => {
    const dayStr = format(day, "yyyy-MM-dd");
    return leaveRequests.filter((lr) => {
      const from = lr.outFrom.slice(0, 10);
      const till = lr.outTill.slice(0, 10);
      return from <= dayStr && dayStr <= till;
    });
  };

  const LEAVE_CONFIG = {
    ILL:      { bg: "bg-rose-soft",   border: "border-rose-ink/20",   text: "text-rose-ink",   label: "Sick leave" },
    ABSENCE:  { bg: "bg-amber-soft",  border: "border-amber-ink/20",  text: "text-amber-ink",  label: "Absence"    },
    VACATION: { bg: "bg-violet-soft", border: "border-violet-ink/20", text: "text-violet-ink", label: "Vacation"   },
  };

  const weekLabel = `${format(currentWeekStart, "MMM d")} – ${format(weekEnd, "MMM d, yyyy")}`;

  return (
    <div className="flex flex-col flex-1">
      <main className="flex-1 px-5 lg:px-8 py-7 max-w-350 mx-auto w-full">

        {/* ── Page header ── */}
        <div className="flex flex-wrap items-end justify-between gap-4 mb-6">
          <div>
            <h1 className="font-serif text-[32px] leading-tight text-ink-900 tracking-tight">
              My Schedule
            </h1>
            <p className="text-[13px] text-ink-500 mt-0.5">
              Your assigned shifts for the week
            </p>
          </div>

          {/* ── Week navigator ── */}
          <div className="flex items-center gap-2">
            <button
              onClick={handleToday}
              className="h-7 px-3 text-[12px] font-medium rounded-md border border-ink-200 text-ink-600 hover:bg-ink-100 transition"
            >
              Today
            </button>
            <button
              onClick={handlePrevWeek}
              className="w-7 h-7 flex items-center justify-center rounded-md border border-ink-200 text-ink-500 hover:bg-ink-100 transition"
            >
              <FaChevronLeft size={10} />
            </button>
            <span className="text-[12px] text-ink-600 font-mono min-w-47.5 text-center">
              {weekLabel}
            </span>
            <button
              onClick={handleNextWeek}
              className="w-7 h-7 flex items-center justify-center rounded-md border border-ink-200 text-ink-500 hover:bg-ink-100 transition"
            >
              <FaChevronRight size={10} />
            </button>
          </div>
        </div>

        {/* ── Calendar ── */}
        {isLoading ? (
          <div className="flex items-center justify-center py-20 text-[13px] text-ink-400">
            Loading schedule…
          </div>
        ) : (
          <div className="bg-white rounded-xl2 border border-ink-200 shadow-soft overflow-hidden">

            {/* Day header row */}
            <div className="grid grid-cols-7 border-b border-ink-200">
              {weekDays.map((day) => (
                <div
                  key={day.toString()}
                  className={`px-3 py-2.5 text-center border-r last:border-r-0 border-ink-200 ${
                    isToday(day) ? "bg-ink-900" : "bg-ink-50"
                  }`}
                >
                  <p
                    className={`text-[10.5px] uppercase tracking-wider font-medium ${
                      isToday(day) ? "text-white/60" : "text-ink-400"
                    }`}
                  >
                    {format(day, "EEE")}
                  </p>
                  <p
                    className={`text-[15px] font-medium mt-0.5 ${
                      isToday(day) ? "text-white" : "text-ink-700"
                    }`}
                  >
                    {format(day, "d")}
                  </p>
                </div>
              ))}
            </div>

            {/* Day columns */}
            <div className="grid grid-cols-7 min-h-105">
              {weekDays.map((day) => {
                const dayShifts = getShiftsForDay(day);
                const dayLeaves = getLeavesForDay(day);
                const isEmpty = dayShifts.length === 0 && dayLeaves.length === 0;

                return (
                  <div
                    key={day.toString()}
                    className={`border-r last:border-r-0 border-ink-100 p-2 flex flex-col gap-1.5 ${
                      isToday(day) ? "bg-ink-50/40" : ""
                    }`}
                  >
                    {isEmpty ? (
                      <p className="text-[11px] text-ink-200 text-center mt-6 select-none">
                        —
                      </p>
                    ) : (
                      <>
                        {dayShifts.map((shift) => (
                          <div
                            key={shift.shiftId}
                            className="bg-mint-soft border border-mint-ink/20 rounded-md px-2 py-1.5"
                          >
                            <p className="text-[11.5px] font-medium text-mint-ink truncate">
                              {shift.shiftTitle}
                            </p>
                            <p className="text-[11px] text-mint-ink/70 mt-0.5 font-mono">
                              {shift.shiftStartTime?.slice(0, 5)} –{" "}
                              {shift.shiftEndTime?.slice(0, 5)}
                            </p>
                          </div>
                        ))}

                        {dayLeaves.map((lr) => {
                          const cfg = LEAVE_CONFIG[lr.type] ?? LEAVE_CONFIG.ABSENCE;
                          return (
                            <div
                              key={lr.leaveRequestId}
                              className={`${cfg.bg} border ${cfg.border} rounded-md px-2 py-1.5`}
                            >
                              <p className={`text-[11.5px] font-medium ${cfg.text} truncate`}>
                                {cfg.label}
                              </p>
                              <p className={`text-[11px] ${cfg.text} opacity-70 mt-0.5 font-mono`}>
                                {lr.outFrom.slice(11, 16)} – {lr.outTill.slice(11, 16)}
                              </p>
                            </div>
                          );
                        })}
                      </>
                    )}
                  </div>
                );
              })}
            </div>
          </div>
        )}
      </main>
      <Footer />
    </div>
  );
};

export default UserSchedule;