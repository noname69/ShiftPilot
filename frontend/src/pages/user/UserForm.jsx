import { useEffect } from "react";
import { useForm } from "react-hook-form";
import SelectField from "../../components/shared/SelectField";
import InputField from "../../components/shared/InputField";

const generatePassword = () => {
  return Math.random().toString(36).slice(-10);
};

const UserForm = ({
  onSubmit,
  defaultValues,
  submitLabel = "Save",
  isLoading = false,
  isEdit = false
}) => {
  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm({
    defaultValues: {
      firstName: "",
      lastName: "",
      email: "",
      username: "",
      password: generatePassword(),
      role: "USER",
      status: "ACTIVE",
      ...defaultValues,
    },
  });

  useEffect(() => {
    if (defaultValues) {
      reset(defaultValues);
    }
  }, [defaultValues, reset]);

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
      />

      <InputField
        id="lastName"
        label="Last name"
        type="text"
        min={2}
        required
        register={register}
        errors={errors}
      />

      <InputField
        id="email"
        label="Email"
        type="email"
        required
        register={register}
        errors={errors}
      />

      {!isEdit && (
      <InputField
        id="username"
        label="Username"
        type="text"
        min={2}
        required
        register={register}
        errors={errors}
      />
      )}

      <InputField
        id="password"
        label="Password"
        type="text"
        required
        register={register}
        errors={errors}
      />

      <SelectField
        id="role"
        label="Role"
        required
        register={register}
        errors={errors}
        options={[
          { value: "USER", label: "User" },
          { value: "ADMIN", label: "Admin" },
          { value: "MANAGER", label: "Manager" },
        ]}
      />

      <SelectField
        id="status"
        label="Status"
        required
        register={register}
        errors={errors}
        options={[
          { value: "ACTIVE", label: "Active" },
          { value: "INACTIVE", label: "Inactive" },
        ]}
      />

      <button
        type="submit"
        className="my-btn-primary mt-2"
        disabled={isLoading}
      >
        {isLoading ? "Loading..." : submitLabel}
      </button>
    </form>
    
  );

};

export default UserForm;
