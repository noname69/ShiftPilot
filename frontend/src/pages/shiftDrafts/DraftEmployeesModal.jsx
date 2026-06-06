import { IoClose } from "react-icons/io5";
import AddRemoveEmployeesContainers from "./AddRemoveEmployeesContainers";

const ConfirmationModal = ({ modal, setModal, addedUsers, setAddedUsers }) => {
  if (!modal) return null;

  const { title, message, confirmButton, onConfirm } =
    modal || {};

  const handleClose = () => {
    setAddedUsers([]);
    setModal(null);
  };

  const handleConfirm = () => {
    onConfirm?.();
    setModal(null);
  };


  return (
    <>
      <dialog open className="modal text-center">
        <div className="modal-box p-0 font-sans">
          <div className="bg-ink-50 rounded-xl2 border border-ink-200">
            <div className="flex justify-between items-center p-4">
              <h3 className="font-bold ">{title}</h3>
              <IoClose
                className="text-2xl text-ink-500 cursor-pointer"
                onClick={handleClose}
              />
            </div>
            <hr className="text-gray-300" />
          </div>
          <div className="modal-action flex flex-col p-4 m-0">
            <AddRemoveEmployeesContainers addedUsers={addedUsers} setAddedUsers={setAddedUsers}/>
            <h3 className="font-medium text-lg text-slate-800">{message}</h3>

            <div className="flex gap-2 font-bold text-white justify-end mt-6">
              <button
                type="button"
                className="my-btn-secondary"
                onClick={handleClose}
              >
                Cancel
              </button>

              {confirmButton && (
                <button
                  type="submit"
                  className="my-btn-primary w-fit text-white "
                  onClick={handleConfirm}
                >
                  {confirmButton}
                </button>
              )}
            </div>
          </div>
        </div>
      </dialog>
    </>
  );
};

export default ConfirmationModal;
