import { useForm } from "react-hook-form";
import InputField from "../components/shared/InputField";
import useUserStore from "../../store/userStore";
import { useEffect, useState } from "react";
import MyCheckbox from "../components/shared/MyCheckbox";

const EditPersonalInformationForm = ({ defaultValues, onSubmit }) => {
  const { isLoading } = useUserStore((state) => state);
  const [checked, setChecked] = useState(false);

  const {
    register,
    handleSubmit,
    reset,
    watch,
    formState: { errors },
  } = useForm();

  useEffect(() => {
    if (defaultValues) reset(defaultValues);
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

      <div className="flex gap-2 items-center">
        <MyCheckbox checked={checked} onChange={setChecked} />
        <p className="my-para">Change password</p>
      </div>

      {checked && (
        <>
          <InputField
            id="oldPassword"
            label="Enter your old password"
            type="password"
            register={register}
            errors={errors}
            theme="my-input"
          />

          <InputField
            id="oldPassword2"
            label="Repeat your old password"
            type="password"
            register={register}
            errors={errors}
            theme="my-input"
            validation={{
              validate: (value) =>
                value === watch("oldPassword") || "Passwords do not match",
            }}
          />

          <InputField
            id="newPassword"
            label="New Password"
            type="password"
            register={register}
            errors={errors}
            theme="my-input"
            validation={{
              pattern: {
                value: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,64}$/,
                message:
                  "Password must be 8-64 characters and contain uppercase, lowercase and a number",
              },
            }}
          />
        </>
      )}

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

export default EditPersonalInformationForm;
