import InputField from "../../components/shared/InputField";
import { useForm } from "react-hook-form";

const Login = () => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm();

  const onSubmit = (formData) => {
    console.log(formData);
  };

  return (
    <div>
      <form className="bg-red-300 w-100" onSubmit={handleSubmit(onSubmit)}>
        <h1>Login</h1>
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
        <button className="btn btn-primary">Submit</button>
      </form>
    </div>
  );
};

export default Login;
