import { useMemo, useState } from "react";
import { FaTimes } from "react-icons/fa";
import { formatDate } from "../../../utils/formatDateTime";

import { useEffect } from "react";

import useShiftStore from "../../../store/shiftStore";

const SwapRequestModal = ({
  isOpen,
  onClose,
  shiftId,
  // targetAssignment,
  myAssignments = [],
  onSubmit,
  // loading,
}) => {
  const [selectedAssignmentId, setSelectedAssignmentId] = useState(null);
  const [reason, setReason] = useState("");

  const { shift, fetchShiftById } = useShiftStore();

  useEffect(() => {
    if (isOpen) {
      fetchShiftById(shiftId);
    }
  }, [isOpen, shiftId, fetchShiftById]);

  if (!isOpen) return null;

  const formatTime = (t) => (t ? t.slice(0, 5) : "");

  const handleSubmit = () => {
    if (!selectedAssignmentId) return;

    console.log("Submitting swap request with", {
      requesterAssignmentId: selectedAssignmentId,
      reason,
    });

    onSubmit({
      requesterAssignmentId: selectedAssignmentId,
      // targetAssignmentId: targetAssignment?.id,
      reason,
    });
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 px-4">
      <div className="w-full max-w-3xl rounded-2xl bg-white shadow-2xl overflow-hidden">
        {/* HEADER */}
        <div className="flex items-center justify-between border-b border-ink-100 px-6 py-4">
          <div>
            <h2 className="text-[20px] font-semibold text-ink-900">
              Request Shift Swap
            </h2>

            <p className="text-[13px] text-ink-500 mt-0.5">
              Select one of your shifts to swap with:
            </p>
          </div>

          <button
            onClick={onClose}
            className="w-9 h-9 rounded-md flex items-center justify-center hover:bg-ink-50 transition-colors"
          >
            <FaTimes size={14} className="text-ink-500" />
          </button>
        </div>

        {/* TARGET SHIFT */}
        <div className="px-6 pt-5">
          <div className="rounded-xl border border-violet-200 bg-violet-50 px-4 py-3">
            <div className="text-[11px] uppercase tracking-wide text-violet-700 font-semibold mb-1">
              Target Shift
            </div>

            <div className="text-[15px] font-semibold text-ink-900">
              {shift?.title}
            </div>

            <div className="text-[12px] text-ink-600 mt-1 font-mono">
              {formatDate(shift?.shiftDate)} · {formatTime(shift?.startTime)} -{" "}
              {formatTime(shift?.endTime)}
            </div>
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
                {myAssignments.map((assignment) => {
                  const isSelected =
                    selectedAssignmentId === assignment.assigneeId;

                  return (
                    <tr
                      key={assignment.assigneeId}
                      onClick={() => {
                        setSelectedAssignmentId(assignment.assigneeId);
                      }}
                      className={`cursor-pointer transition-colors ${
                        isSelected ? "bg-violet-50" : "hover:bg-ink-50/70"
                      }`}
                    >
                      <td className="px-4 py-3 font-medium text-ink-900">
                        {assignment.title}
                      </td>

                      <td className="px-4 py-3 text-ink-600 font-mono">
                        {formatDate(assignment.shiftDate)}
                      </td>

                      <td className="px-4 py-3 text-ink-600 font-mono">
                        {formatTime(assignment.startTime)} -{" "}
                        {formatTime(assignment.endTime)}
                      </td>

                      <td className="px-4 py-3 text-center">
                        <div
                          className={`mx-auto w-4 h-4 rounded-full border-2 transition-colors ${
                            isSelected
                              ? "border-violet-600 bg-violet-600"
                              : "border-ink-300"
                          }`}
                        />
                      </td>
                    </tr>
                  );
                })}

                {myAssignments.length === 0 && (
                  <tr>
                    <td
                      colSpan={4}
                      className="px-4 py-8 text-center text-ink-400"
                    >
                      No available shifts
                    </td>
                  </tr>
                )}
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
            placeholder="Optional reason for swap request..."
            rows={4}
            className="w-full rounded-xl border border-ink-200 bg-white px-4 py-3 text-[13px] outline-none focus:border-violet-300 focus:ring-2 focus:ring-violet-100 resize-none"
          />
        </div>

        {/* FOOTER */}
        <div className="flex items-center justify-end gap-3 border-t border-ink-100 px-6 py-4 mt-6">
          <button
            onClick={onClose}
            className="px-4 py-2 rounded-lg border border-ink-200 text-ink-700 hover:bg-ink-50 transition-colors text-[13px]"
          >
            Cancel
          </button>

          <button
            onClick={handleSubmit}
            // disabled={!selectedAssignmentId || loading}
            className="px-4 py-2 rounded-lg bg-violet-600 text-white hover:bg-violet-700 disabled:opacity-50 disabled:cursor-not-allowed transition-colors text-[13px] font-medium"
          >
            Send Request
          </button>
        </div>
      </div>
    </div>
  );
};

export default SwapRequestModal;
