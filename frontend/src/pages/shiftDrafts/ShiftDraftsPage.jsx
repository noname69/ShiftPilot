import { useEffect, useState } from "react";
import { Link } from "react-router";
import Footer from "../components/shared/Footer";
import useAuthStore from "../../store/authStore";
import ConfirmationModal from "../components/shared/ConfirmationModal";
import useShiftDraftsStore from "../../store/shiftDraftsStore";

const ShiftDraftsPage = () => {

  const { user } = useAuthStore((state) => state);
  const { fetchShiftDrafts, drafts, isLoading } = useShiftDraftsStore(state => state);
  const role = user?.role.toLowerCase();
  const [modal, setModal] = useState(null);

  useEffect(() => {
    fetchShiftDrafts()
  }, [fetchShiftDrafts])

  return (
    <div className="flex flex-col flex-1">
      <main className="flex-1 px-5 lg:px-8 py-7 max-w-350 mx-auto w-full">
        <div className="flex flex-wrap items-end justify-between gap-4 mb-6">
          <div>
            <h1 className="font-serif text-[32px] leading-tight text-ink-900 tracking-tight">
              Shift drafts
            </h1>
            <p className="text-[13px] text-ink-500 mt-0.5">
              Manage all shift drafts
            </p>
          </div>
          <div className="flex gap-4">
            <Link to={`/${role}/shift-drafts/new`}>
              <button className="inline-flex items-center gap-1.5 text-[13px] font-medium text-white bg-ink-900 hover:bg-ink-800 px-3 py-1.5 rounded-md shadow-soft transition-colors">
                <svg
                  width="14"
                  height="14"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  strokeWidth="2.2"
                >
                  <path d="M12 5v14M5 12h14" />
                </svg>
                New draft
              </button>
            </Link>
          </div>
        </div>
        {isLoading ? (
          <div className="flex items-center justify-center py-20 text-[13px] text-ink-400">
            Loading drafts…
          </div>
        ) : (
          <div className="bg-white rounded-xl2 border border-ink-200 shadow-soft overflow-hidden">
            {drafts.length === 0 ? (
              <div className="flex flex-col items-center justify-center py-20 gap-3">
                <p className="text-[14px] text-ink-500">No drafts yet.</p>
                <Link
                  to={`/${role}/shifts/new`}
                  className="inline-flex items-center gap-1.5 text-[13px] font-medium text-white bg-ink-900 hover:bg-ink-800 px-3 py-1.5 rounded-md shadow-soft transition-colors cursor-pointer"
                >
                  <svg
                    width="13"
                    height="13"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    strokeWidth="2.2"
                  >
                    <path d="M12 5v14M5 12h14" />
                  </svg>
                  Create your first draft
                </Link>
              </div>
            ) : (
              <table className="w-full text-[13px]">
                <thead className="bg-ink-50 text-ink-500 text-[11.5px] uppercase tracking-wider border-b border-ink-200">
                  <tr>
                    <th className="text-left font-medium px-4 py-2.5">Title</th>
                    <th className="text-left font-medium px-4 py-2.5 hidden md:table-cell">
                      Description
                    </th>
                    <th className="text-left font-medium px-4 py-2.5">Date</th>
                    <th className="text-left font-medium px-4 py-2.5 hidden sm:table-cell">
                      Time
                    </th>
                    <th className="text-left font-medium px-4 py-2.5 hidden lg:table-cell">
                      Min. Staff
                    </th>
                    <th className="text-left font-medium px-4 py-2.5 hidden xl:table-cell">
                      Assigned employees
                    </th>
                    <th className="text-center font-medium px-4 py-2.5">
                      Actions
                    </th>
                  </tr>
                </thead>
              </table>
            )}
          </div>
        )}
      </main>
      <Footer />
      <ConfirmationModal modal={modal} setModal={setModal} />
    </div>
  );
};

export default ShiftDraftsPage;
