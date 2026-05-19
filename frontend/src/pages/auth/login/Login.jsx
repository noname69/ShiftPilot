import InputField from "../../../components/shared/InputField";
import Header from "../../../components/shared/Header";
import { useForm } from "react-hook-form";
import Footer from "../../../components/shared/Footer";
import LoginAside from "./LoginAside";
import useUsersStore from "../../../store/userStore";
import { useNavigate } from "react-router";

const Login = () => {

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm();

  const navigate = useNavigate();
  const { loginUser } = useUsersStore(state => state);

  const onSubmit = (formData) => {
    loginUser(formData, navigate);
  };

  return (
    <div className="flex min-h-full overflow-y-auto">
      <div className="flex flex-col flex-1 justify-between bg-[#F4F4F2]">
        <Header />
        <main className="flex flex-col items-center">
          <div className="flex flex-col items-start w-[50%] self-center">
            <div className="flex flex-col gap-2 items-start">
              <p className="my-para">Welcome back</p>
              <h1 className="text-4xl font-serif">
                Sign in to <span className="italic">ShiftPilot</span>
              </h1>
              <p className="my-para mb-6">The simplest way to schedule your team.</p>
            </div>

            <form className="w-full flex flex-col gap-2" onSubmit={handleSubmit(onSubmit)}>
              <div className="flex flex-col items-start ">
                <label htmlFor="username" className="my-para">Username</label>
                <InputField
                  register={register}
                  id="username"
                  type="text"
                  placeholder="John123"
                  required
                  min={3}
                  max={50}
                  errors={errors}
                  theme="my-input"
                />
              </div>
              <div className="flex flex-col items-start ">
                <div className="flex justify-between w-full my-para">
                  <label htmlFor="username">Password</label>
                  <p>Forgot?</p>
                </div>
                <InputField
                  register={register}
                  id="password"
                  type="password"
                  placeholder="●●●●●●●●"
                  required
                  min={6}
                  errors={errors}
                  theme="my-input"
                />
              </div>
              <div className="flex gap-2 items-center">
                <input type="checkbox" className="accent-black w-4 h-4" />
                <p className="my-para">Keep me signed in</p>
              </div>
              <button className="my-btn-primary">Sign in</button>
            </form>
          </div>
        </main>
        <Footer />
      </div>
      <LoginAside></LoginAside>
    </div>
  );
};

export default Login;
