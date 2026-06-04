import { useEffect } from "react";
import { useForm } from "react-hook-form";
import InputField from "../components/shared/InputField";

const ShiftForm = ({
  onSubmit,
  defaultValues,
  submitLabel = "Save",
  isLoading = false,
}) => {
  const {
    register,
    handleSubmit,
    formState: { errors },
    reset,
  } = useForm();


  useEffect(() => {
    if (defaultValues) reset(defaultValues);

  }, [defaultValues, reset]);

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
      <InputField
        label="Shift Date"
        id="shiftDate"
        type="date"
        required
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
      <button
        type="submit"
        className="my-btn-primary mt-2"
        disabled={isLoading}
      >
        {submitLabel}
      </button>
    </form>
  );
};

export default ShiftForm;
