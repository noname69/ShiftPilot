import { useForm } from "react-hook-form";
import InputField from "../components/shared/InputField";
import SelectField from "../components/shared/SelectField";
import useUserStore from "../../store/userStore";
import { useEffect } from "react";

const EditUserFrom = ({ defaultValues, onSubmit }) => {

  const { isLoading } = useUserStore(state => state);

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm();

  useEffect(() => {
    if(defaultValues)
      reset(defaultValues)
  }, [defaultValues, reset])

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-3">
      <InputField
        id="firstName"
        label="First name"
        type="text"
        min={2}
        required
        register={register}
        errors={errors}
        theme="my-input"
      />

      <InputField
        id="lastName"
        label="Last name"
        type="text"
        min={2}
        required
        register={register}
        errors={errors}
        theme="my-input"
      />

      <InputField
        id="email"
        label="Email"
        type="email"
        required
        register={register}
        errors={errors}
        theme="my-input"
      />

      <SelectField
        id="role"
        label="Role"
        required
        register={register}
        errors={errors}
        theme="my-input"
        options={[
          { value: "USER", label: "User" },
          { value: "ADMIN", label: "Admin" },
          { value: "MANAGER", label: "Manager" },
        ]}
      />

      <button
        type="submit"
        className="my-btn-primary mt-2"
        disabled={isLoading}
      >
        {isLoading ? "Loading..." : "Update user"}
      </button>
    </form>
  );
};

export default EditUserFrom;
