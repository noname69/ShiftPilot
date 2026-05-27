import { useState, useEffect } from "react";
import { FaTimes } from "react-icons/fa";
import { formatDate } from "../../../utils/formatDateTime";
import useShiftStore from "../../../store/shiftStore";

const SwapRequestModal = ({
  isOpen,
  onClose,
  shiftId,
  myAssignments = [],
  assignedUsers = [],
  onSubmit,
}) => {
  const [selectedUserIds, setSelectedUserIds] = useState([]);
  const [reason, setReason] = useState("");

  const { shift, fetchShiftById } = useShiftStore();

  useEffect(() => {
    if (isOpen && shiftId) {
      fetchShiftById(shiftId);
    }
  }, [isOpen, shiftId, fetchShiftById]);

  if (!isOpen) return null;

  const formatTime = (t) => (t ? t.slice(0, 5) : "");

  // ✅ toggle user selection
  const toggleUser = (userId) => {
    setSelectedUserIds((prev) =>
      prev.includes(userId)
        ? prev.filter((id) => id !== userId)
        : [...prev, userId]
    );
  };

  const handleSubmit = () => {
    if (selectedUserIds.length === 0) return;

    onSubmit({
      targetUserIds: selectedUserIds,
      reason,
    });
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 px-4">
      <div className="w-full max-w-4xl rounded-2xl bg-white shadow-2xl overflow-hidden">

        {/* HEADER */}
        <div className="flex items-center justify-between border-b border-ink-100 px-6 py-4">
          <div>
            <h2 className="text-[20px] font-semibold text-ink-900">
              Request Shift Swap
            </h2>
            <p className="text-[13px] text-ink-500 mt-0.5">
              Select one or more users
            </p>
          </div>

          <button
            onClick={onClose}
            className="w-9 h-9 rounded-md flex items-center justify-center hover:bg-ink-50"
          >
            <FaTimes size={14} className="text-ink-500" />
          </button>
        </div>

        {/* TARGET SHIFT */}
        <div className="px-6 pt-5">
          <div className="rounded-xl border border-violet-200 bg-violet-50 px-4 py-3">
            <div className="text-[11px] uppercase text-violet-700 font-semibold mb-1">
              Target Shift
            </div>

            <div className="text-[15px] font-semibold text-ink-900">
              {shift?.title}
            </div>

            <div className="text-[12px] text-ink-600 font-mono">
              {formatDate(shift?.shiftDate)} ·{" "}
              {formatTime(shift?.startTime)} - {formatTime(shift?.endTime)}
            </div>
          </div>
        </div>

        {/* 👥 ASSIGNED USERS (MULTI SELECT) */}
        <div className="px-6 pt-5">
          <div className="text-[13px] font-medium text-ink-800 mb-2">
            Select users from this shift
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-2">
            {(assignedUsers ?? []).map((u) => {
              const isSelected = selectedUserIds.includes(u.id);

              return (
                <div
                  key={u.id}
                  onClick={() => toggleUser(u.id)}
                  className={`cursor-pointer border rounded-lg px-3 py-2 transition ${
                    isSelected
                      ? "bg-violet-50 border-violet-400"
                      : "bg-ink-50 border-ink-200 hover:bg-ink-100"
                  }`}
                >
                  <div className="text-[13px] font-medium text-ink-900">
                    {u.firstName} {u.lastName}
                  </div>

                  <div className="text-[11px] text-ink-500">
                    {u.email || "no email"}
                  </div>
                </div>
              );
            })}
          </div>
        </div>

        {/* MY SHIFTS */}
        <div className="px-6 pt-5">
          <div className="text-[13px] font-medium text-ink-800 mb-2">
            Your shifts
          </div>

          <div className="rounded-xl border border-ink-200 overflow-hidden">
            <table className="w-full text-[13px]">
              <thead className="bg-ink-50 text-ink-500 uppercase text-[11px]">
                <tr>
                  <th className="px-4 py-2 text-left">Shift</th>
                  <th className="px-4 py-2 text-left">Date</th>
                  <th className="px-4 py-2 text-left">Time</th>
                  <th className="px-4 py-2 text-center">Select</th>
                </tr>
              </thead>

              <tbody className="divide-y divide-ink-100">
                {myAssignments.map((a) => {
                  const isSelected =
                    selectedUserIds.includes(a.assigneeId);

                  return (
                    <tr
                      key={a.assigneeId}
                      onClick={() => toggleUser(a.assigneeId)}
                      className={`cursor-pointer transition ${
                        isSelected
                          ? "bg-violet-50"
                          : "hover:bg-ink-50/70"
                      }`}
                    >
                      <td className="px-4 py-3 font-medium text-ink-900">
                        {a.title}
                      </td>

                      <td className="px-4 py-3 text-ink-600 font-mono">
                        {formatDate(a.shiftDate)}
                      </td>

                      <td className="px-4 py-3 text-ink-600 font-mono">
                        {formatTime(a.startTime)} - {formatTime(a.endTime)}
                      </td>

                      <td className="px-4 py-3 text-center">
                        <div
                          className={`w-4 h-4 mx-auto rounded border-2 transition ${
                            isSelected
                              ? "bg-violet-600 border-violet-600"
                              : "border-ink-300"
                          }`}
                        />
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        </div>

        {/* REASON */}
        <div className="px-6 pt-5">
          <label className="block text-[13px] font-medium text-ink-800 mb-2">
            Reason
          </label>

          <textarea
            value={reason}
            onChange={(e) => setReason(e.target.value)}
            rows={4}
            className="w-full rounded-xl border border-ink-200 px-4 py-3 text-[13px]"
            placeholder="Optional reason..."
          />
        </div>

        {/* FOOTER */}
        <div className="flex justify-end gap-3 border-t border-ink-100 px-6 py-4">
          <button
            onClick={onClose}
            className="px-4 py-2 rounded-lg border border-ink-200"
          >
            Cancel
          </button>

          <button
            onClick={handleSubmit}
            disabled={selectedUserIds.length === 0}
            className="px-4 py-2 rounded-lg bg-violet-600 text-white disabled:opacity-50"
          >
            Send Request ({selectedUserIds.length})
          </button>
        </div>
      </div>
    </div>
  );
};

export default SwapRequestModal;