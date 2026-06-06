import { Link } from "react-router";
import Footer from "../components/shared/Footer";
import DraftForm from "./DraftForm";
import useAuthStore from "../../store/authStore";

const DraftCreatePage = () => {
  const role = useAuthStore((state) => state.user.role);

  return (
    <div className="flex flex-col flex-1">
      <main className="flex-1 flex justify-center px-5 py-7">
        <div className="w-full max-w-130">
          <div className="mb-6">
            <h1 className="font-serif text-[32px] leading-tight text-ink-900 tracking-tight">
              Create shift draft
            </h1>
            <p className="text-[13px] text-ink-500 mt-0.5">
              Fill in the details to create a shift draft.
            </p>
          </div>

          <div className="my-card">
            <DraftForm
              submitLabel="Create Shift"
            />
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

export default DraftCreatePage;
