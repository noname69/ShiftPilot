import { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router";
import Header from "../components/shared/Header";
import Footer from "../components/shared/Footer";
import ShiftForm from "./ShiftForm";
import useShiftStore from "../../store/shiftStore";
import { getShiftById } from "../../api/shift";
import useAuthStore from "../../store/authStore";

const ShiftEditPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { editShift } = useShiftStore();
  const [defaultValues, setDefaultValues] = useState(null);
  const [loadError, setLoadError] = useState(null);
  const role = useAuthStore(state => state.user.role);

  useEffect(() => {
    getShiftById(id)
      .then((data) => {
        setDefaultValues({
          title: data.title,
          description: data.description,
          shiftDate: data.shiftDate,
          startTime: data.startTime?.slice(0, 5) ?? "",
          endTime: data.endTime?.slice(0, 5) ?? "",
          minEmployees: data.minEmployees,
        });
      })
      .catch((err) => setLoadError(err.message ?? "Failed to load shift."));
  }, [id]);

  const onSubmit = async (data) => {
    const success = await editShift(id, data);
    if (success) navigate("/shifts");
  };
  return (
    <div className="flex flex-col flex-1">
      <Header />
      <main className="flex-1 flex justify-center px-5 py-7">
        <div className="w-full max-w-130">
          <div className="mb-6">
            <p className="text-[12px] text-ink-500 mb-1">Shifts</p>
            <h1 className="font-serif text-[32px] leading-tight text-ink-900 tracking-tight">
              Edit Shift
            </h1>
            <p className="text-[13px] text-ink-500 mt-0.5">
              Update the shift details below.
            </p>
          </div>

          {loadError ? (
            <div className="my-card text-[13px] text-rose-ink">{loadError}</div>
          ) : !defaultValues ? (
            <div className="my-card text-[13px] text-ink-400">Loading…</div>
          ) : (
            <div className="my-card">
              <ShiftForm
                onSubmit={onSubmit}
                defaultValues={defaultValues}
                submitLabel="Save Changes"
              />
            </div>
          )}

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

export default ShiftEditPage;
