import { useState } from "react";
import { Link, useNavigate } from "react-router";
import Footer from "../components/shared/Footer";
import useUserStore from "../../store/userStore";
import useAuthStore from "../../store/authStore";
import ConfirmationModal from "../components/shared/ConfirmationModal"
import EditPersonalInformationForm from "./EditPersonalInformationForm"

const EditPersonalInformation = () => {

  const navigate = useNavigate();
  const { user, fetchCurrentUser } = useAuthStore((state) => state);
  const { role }= useAuthStore((state) => state.user);
  const [modal, setModal] = useState(null);

  const { editPersonalInformation } = useUserStore();

  const onSubmit = (data) => {
    console.log(data)
    setModal({
      title: "Edit personal information",
      message: `Are you sure you want to edit your personal information?`,
      confirmButton: "Edit",
      onConfirm: async () => {
        editPersonalInformation(data, navigate, role, fetchCurrentUser);
      },
    });
  };

  return (
    <div className="flex flex-col flex-1">
      <main className="flex-1 flex justify-center px-5 py-7">
        <div className="w-full max-w-130">
          <div className="mb-6">

            <h1 className="font-serif text-[32px]">Edit Personal Information</h1>
          </div>

          <div className="my-card">
            {user ? (
              <EditPersonalInformationForm onSubmit={onSubmit} defaultValues={user} />
            ) : (
              <p className="text-sm text-gray-500">Loading user...</p>
            )}
          </div>
          <div className="mt-4">
            <Link
              to={`/${role}`}
              className="text-[13px] text-ink-500 hover:text-ink-900 transition-colors"
            >
              ← Back
            </Link>
          </div>
        </div>
      </main>
      <ConfirmationModal modal={modal} setModal={setModal}/>

      <Footer />
    </div>
  );
};

export default EditPersonalInformation;
