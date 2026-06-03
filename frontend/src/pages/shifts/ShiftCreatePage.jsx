import { Link, useNavigate } from "react-router";
import Footer from "../components/shared/Footer";
import ShiftForm from "./ShiftForm";
import useShiftStore from "../../store/shiftStore";
import useAuthStore from "../../store/authStore";
import { useState } from "react";
import ShiftDrafts from "./ShiftDrafts";
import toast from "react-hot-toast";

const ShiftCreatePage = () => {
  const navigate = useNavigate();
  const { addShift } = useShiftStore();
  const role = useAuthStore((state) => state.user.role);
  const [defaultValues, setDefaultValues] = useState(null);

  const onSubmit = async (data) => {
    const draftedShiftId = defaultValues?.draftedShiftId || "";
    const success = await addShift({...data, draftedShiftId: draftedShiftId });
    if (success) {
      navigate("/shifts");
      toast.success("Shift created successfully")
    }
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

          <div className="flex flex-col gap-6">
            <div className="my-card bg-ink-100">
              <ShiftDrafts setDefaultValues={setDefaultValues}/>
            </div>

            <div className="my-card">
              <ShiftForm onSubmit={onSubmit} defaultValues={defaultValues} submitLabel="Create Shift" />
            </div>
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
