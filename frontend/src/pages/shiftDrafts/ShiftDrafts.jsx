import { useForm } from "react-hook-form";
import SelectField from "../components/shared/SelectField";
import { useEffect } from "react";
import useShiftDraftsStore from "../../store/shiftDraftsStore";

export default function ShiftDrafts({ setDefaultValues }) {
  const { fetchShiftDrafts, drafts } = useShiftDraftsStore((state) => state);

  const {
    register,
    watch,
    formState: { errors },
  } = useForm();

  const availableDrafts = drafts.map((draft) => {
    return { label: draft.title, value: draft.id };
  });

  const pickedDraft = watch("draftId");

  useEffect(() => {
    fetchShiftDrafts();
  }, [fetchShiftDrafts]);

  useEffect(() => {
    const loadDraft = () => {
      if (!pickedDraft) {
        setDefaultValues({
          title: "",
          description: "",
          shiftDate: "",
          startTime: "",
          endTime: "",
          minEmployees: "",
          draftId: "",
          userIds: []
        });
        return;
      }

      const draft = drafts.find((d) => d.id === Number(pickedDraft));
      const { id, title, description, startTime, endTime, minEmployees, draftEmployees } = draft;
      const userIds = draftEmployees.map(empl => empl.id);

      setDefaultValues({
        title: title,
        description: description,
        shiftDate: "",
        startTime: startTime?.slice(0, 5) ?? "",
        endTime: endTime?.slice(0, 5) ?? "",
        minEmployees: minEmployees,
        draftId: id,
        userIds: userIds
      });
    };

    loadDraft();
  }, [pickedDraft, drafts, setDefaultValues]);


  return (
    <form className="space-y-4">
      <div className="flex flex-col gap-1">
        <SelectField
          label="Select a draft to prefill shift details and assignees"
          name="draftId"
          id="draftId"
          register={register}
          theme="my-input"
          options={[{ label: "Select a draft", value: "" }, ...availableDrafts]}
        />

        {errors.shiftType && (
          <p className="text-red-500 text-xs">{errors.shiftType.message}</p>
        )}
      </div>
    </form>
  );
}
