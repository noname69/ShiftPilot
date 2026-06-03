import { useForm } from "react-hook-form";
import SelectField from "../components/shared/SelectField";
import { useEffect } from "react";
import useShiftStore from "../../store/shiftStore";

export default function ShiftDrafts({ setDefaultValues }) {
  const { fetchDraftedShifts, draftedShifts, fetchShiftById } = useShiftStore();

  const {
    register,
    watch,
    formState: { errors },
  } = useForm();

  const drafts = draftedShifts.map((shift) => {
    if (shift.draftName !== null) {
      return { label: shift.draftName, value: shift.id };
    }
  });

  const pickedDraft = watch("shiftId");

  useEffect(() => {
    fetchDraftedShifts();
  }, [fetchDraftedShifts]);

  useEffect(() => {
    const loadShift = async () => {
      if (!pickedDraft) {
        setDefaultValues({
          title: "",
          description: "",
          shiftDate: "",
          startTime: "",
          endTime: "",
          minEmployees: "",
          draftedShiftId: "",
        });
        return;
      }

      const shift = await fetchShiftById(pickedDraft);
      const {
        id,
        title,
        description,
        startTime,
        endTime,
        minEmployees,
      } = shift;

      setDefaultValues({
        title: title,
        description: description,
        shiftDate: "",
        startTime: startTime?.slice(0, 5) ?? "",
        endTime: endTime?.slice(0, 5) ?? "",
        minEmployees: minEmployees,
        draftedShiftId: id
      });
    };

    loadShift();
  }, [pickedDraft, fetchShiftById, setDefaultValues]);

  return (
    <form className="space-y-4">
      <div className="flex flex-col gap-1">
        <SelectField
          label="Select a draft to prefill shift details and assignees"
          name="shiftId"
          id="shiftId"
          register={register}
          theme="my-input"
          options={[{ label: "Select a draft", value: "" }, ...drafts]}
        />

        {errors.shiftType && (
          <p className="text-red-500 text-xs">{errors.shiftType.message}</p>
        )}
      </div>
    </form>
  );
}
