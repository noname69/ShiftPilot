import { Link, useNavigate } from "react-router";
import Footer from "../components/shared/Footer";
import ShiftForm from "./ShiftForm";
import useShiftStore from "../../store/shiftStore";
import useAuthStore from "../../store/authStore";

const ShiftCreatePage = () => {
  const navigate = useNavigate();
  const { addShift } = useShiftStore();
  const role = useAuthStore((state) => state.user.role);

  const onSubmit = async (data) => {
    const success = await addShift(data);
    if (success) navigate("/shifts");
  };

  return (
    <div className="flex flex-col flex-1">
      <main className="flex-1 flex justify-center px-5 py-7">
        <div className="w-full max-w-130">
          <div className="mb-6">
            <p className="text-[12px] text-ink-500 mb-1">Shifts</p>
            <h1 className="font-serif text-[32px] leading-tight text-ink-900 tracking-tight">
              New Shift
            </h1>
            <p className="text-[13px] text-ink-500 mt-0.5">
              Fill in the details to create a shift.
            </p>
          </div>

          <div className="my-card">
            <ShiftForm onSubmit={onSubmit} submitLabel="Create Shift" />
          </div>

          <div className="mt-4">
            <Link
              to={`/${role}/shifts`}
              className="text-[13px] text-ink-500 hover:text-ink-900 transition-colors"
            >
              ← Back to shifts
            </Link>
          </div>
        </div>
      </main>
      <Footer />
    </div>
  );
};

export default ShiftCreatePage;
