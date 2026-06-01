import { IoClose } from "react-icons/io5";
import SelectField from "../components/shared/SelectField";
import { useForm } from "react-hook-form";
import InputField from "../components/shared/InputField";
import useUserStore from "../../store/userStore";
import { useEffect } from "react";

const LeaveRequestModal = ({ modal, setModal }) => {

  const {
    register,
    handleSubmit,
    formState: { errors },
    reset,
  } = useForm();

  const { fetchUsers, users } = useUserStore(state => state);

  useEffect(() => {
    fetchUsers();
  }, [fetchUsers])

  if (!modal) return null;

  const { title, message, confirmButton, onConfirm } = modal || {};
  const managers = users.filter(user => user.role === "MANAGER");

  const handleClose = () => {
    setModal(null);
  };

  const onSubmit = (formData) => {
    const sendData = {
      reason: formData.reason,
      type: formData.type,
      outFrom: formData.outFrom,
      outTill: formData.outTill,
      managerId: formData.managerId

    }
    onConfirm?.(sendData);
    reset();
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
            <h3 className="font-medium text-lg text-slate-800">
              {message}
            </h3>
            <form id="leave-form" onSubmit={handleSubmit(onSubmit)}>
              <SelectField
                name="type"
                id="type"
                label="Leave reason"
                register={register}
                theme="my-input"
                required={true}
                options={[
                  { label: "Absence", value: "ABSENCE" },
                  { label: "Vacation", value: "VACATION" },
                  { label: "ILL", value: "ILL" },
                ]}
              />
              <SelectField
                name="managerId"
                id="managerId"
                label="Pick responsible manager"
                register={register}
                theme="my-input w-fit"
                required={true}
                options={managers.map(manager => ({
                  label: `${manager.firstName} ${manager.lastName}`,
                  value: manager.id,
                }))}
              />
              <div className="flex gap-2">
                <InputField
                  label="Leave Date"
                  id="outFrom"
                  type="date"
                  required
                  register={register}
                  errors={errors}
                  theme="my-input"
                />
              </div>


              <div className="flex gap-2">
                <InputField
                  label="Return Date"
                  id="outTill"
                  type="date"
                  required
                  register={register}
                  errors={errors}
                  theme="my-input"
                />
              </div>

              <label
                htmlFor="reason"
                className="my-para flex"
              >
                Comment (Optional)
              </label>

              <textarea
                id="reason"
                rows={5}
                placeholder="Enter leave reason..."
                className="w-full rounded-xl border border-ink-200 bg-white px-4 py-3 text-[13px] outline-none focus:border-violet-300 focus:ring-2 focus:ring-violet-100 resize-none"
                {...register("reason")}
              />

            </form>

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
                  form="leave-form"
                  className="my-btn-primary w-fit text-white "
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

export default LeaveRequestModal;
