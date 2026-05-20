import InputField from "../components/shared/InputField";
import { useForm } from "react-hook-form";
import useAuthStore from "../../store/authStore";
import { useNavigate } from "react-router";

const Register = () => {

  const { registerUser } = useAuthStore(state => state);
  const navigate = useNavigate();

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm();

  const onSubmit = (formData) => {
    registerUser(formData, navigate);
  };

  return (
    <div>
      <form className="bg-red-300 w-100" onSubmit={handleSubmit(onSubmit)}>
        <h1>Register new User</h1>
        <InputField
          register={register}
          id="firstName"
          type="text"
          placeholder="First name"
          required
          min={3}
          max={50}
          errors={errors}
          theme="bg-white"
        />
        <InputField
          register={register}
          id="lastName"
          type="text"
          placeholder="Last name"
          required
          min={3}
          max={50}
          errors={errors}
          theme="bg-white"
        />
        <InputField
          register={register}
          id="email"
          type="email"
          placeholder="Email"
          required
          min={3}
          max={50}
          errors={errors}
          theme="bg-white"
        />
        <InputField
          register={register}
          id="password"
          type="password"
          placeholder="Password"
          required
          min={6}
          errors={errors}
          theme="bg-white"
        />
        <div className="flex gap-4 w-full justify-center">
          <label className="flex gap-2">
            <input type="radio" value="USER" {...register("role")} />
            USER
          </label>

          <label className="flex gap-2">
            <input type="radio" value="HR" {...register("role")} />
            HR
          </label>
          <label className="flex gap-2">
            <input type="radio" value="SHIFTMANAGER" {...register("role")} />
            SHIFTMANAGER
          </label>
        </div>
        <button className="btn btn-primary">Submit</button>
      </form>
    </div>
  );
};

export default Register;
