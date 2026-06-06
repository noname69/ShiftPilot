import { useState } from "react";
import { useForm } from "react-hook-form";
import InputField from "../components/shared/InputField";
import DraftEmployeesModal from "./DraftEmployeesModal";
import { useNavigate } from "react-router";
import { FiUserCheck } from "react-icons/fi";
import useShiftDraftsStore from "../../store/shiftDraftsStore";
import useAuthStore from "../../store/authStore";

const DraftForm = () => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm();

  const { createShiftDraft, isLoading } = useShiftDraftsStore((state) => state);
  const navigate = useNavigate();

  const [modal, setModal] = useState(null);
  const [addedUsers, setAddedUsers] = useState([]);
  const role = useAuthStore(state => state.user.role)

  const handleAddDraftEmployees = () => {
    setModal({
      title: "Add employees to shift draft",
      confirmButton: "Apply",
    });
  };

  const onSubmit = (formData) => {
    const userIds = addedUsers.map((user) => user.id);
    createShiftDraft({ ...formData, userIds }, navigate, role);
  };

  return (
    <form
      className="flex flex-col gap-2 w-full"
      onSubmit={handleSubmit(onSubmit)}
    >
      <InputField
        label="Title"
        id="title"
        type="text"
        required
        placeholder="e.g. Morning Barista"
        register={register}
        errors={errors}
        theme="my-input"
      />
      <InputField
        label="Description"
        id="description"
        type="text"
        required
        placeholder="e.g. Front counter, opening duties"
        register={register}
        errors={errors}
        theme="my-input"
      />
      <div className="grid grid-cols-2 gap-2">
        <InputField
          label="Start Time"
          id="startTime"
          type="time"
          required
          register={register}
          errors={errors}
          theme="my-input"
        />
        <InputField
          label="End Time"
          id="endTime"
          type="time"
          required
          register={register}
          errors={errors}
          theme="my-input"
        />
      </div>
      <div className="w-full">
        <div className="flex flex-col items-start gap-2 w-full">
          <label htmlFor="minEmployees" className="my-para">
            Min. Employees
          </label>
          <input
            type="number"
            id="minEmployees"
            className="m-1 rounded-lg p-2 w-full text-sm my-input no-spinner"
            min={1}
            placeholder="1"
            {...register("minEmployees", {
              required: { value: true, message: "This field is required" },
              min: { value: 1, message: "Minimum 1 employee required" },
              valueAsNumber: true,
            })}
          />
        </div>
        <p className="my-error text-start">{errors.minEmployees?.message}</p>
      </div>
      <div>
        <button
          type="button"
          onClick={handleAddDraftEmployees}
          className="flex gap-2 p-2 w-fit items-center rounded-lg border border-mint-ink/20 bg-mint-soft text-sm font-medium text-mint-ink transition-colors hover:bg-mint-soft/80"
        >
          <FiUserCheck />
          <p>Manage employees</p>
        </button>
      </div>

      <button
        type="submit"
        className="my-btn-primary mt-2"
        disabled={isLoading}
      >
        Create Draft
      </button>
      {modal && (
        <DraftEmployeesModal
          modal={modal}
          setModal={setModal}
          addedUsers={addedUsers}
          setAddedUsers={setAddedUsers}
        />
      )}
    </form>
  );
};

export default DraftForm;
