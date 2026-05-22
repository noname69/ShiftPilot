import { IoClose } from "react-icons/io5";

const ConfirmationModal = ({ modal, setModal }) => {

  if (!modal) return null;

  const { title, message, rejectButton, confirmButton } = modal || {};

  const handleClose = () => {
    setModal(null);
  };

  const handleConfirm = () => {
     modal.onConfirm?.();
    setModal(null);
  };

  const handleReject = async () => {
    await modal.onReject?.();
    setModal(null);
  };

  return (
    <>
      <dialog open className="modal text-center">
        <div className="modal-box p-0">
          <div className="bg-slate-500">
            <div className="flex justify-between items-center p-4">
              <h3 className="font-bold ">{title}</h3>
              <IoClose
                className="text-2xl text-gray-200 cursor-pointer"
                onClick={handleClose}
              />
            </div>
            <hr className="text-gray-300" />
          </div>
          <div className="modal-action flex flex-col p-4 m-0">
            <h3 className="font-medium text-lg text-slate-800">
              {message}
            </h3>

            <div className="flex gap-2 font-bold text-white justify-end mt-6">
              <button
                type="button"
                className="my-btn-secondary"
                onClick={handleClose}
              >
                Cancel
              </button>

              {rejectButton && (
                <button
                  type="submit"
                  className="btn btn-primary w-20 bg-rose-400 border-none"
                  onClick={handleReject}
                >
                  {rejectButton}
                </button>
              )}

              {confirmButton && (
                <button
                  type="submit"
                  className="my-btn-primary w-fit bg-cyan-600 text-white "
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
