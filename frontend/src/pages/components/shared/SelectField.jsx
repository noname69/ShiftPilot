const SelectField = ({
  label,
  id,
  required,
  errors,
  register,
  options = [],
  placeholder,
  theme,
}) => {
  return (
    <div className="w-full">
      <div className="flex flex-col items-start gap-2 w-full">
        {label && (
          <label htmlFor={id} className="my-para">
            {label}
          </label>
        )}

        <select
          id={id}
          className={`m-1 rounded-lg p-2 w-full text-sm ${theme}`}
          {...register(id, {
            required: required
              ? {
                  value: required,
                  message: "This field is required",
                }
              : null,
          })}
        >
          {placeholder && (
            <option value="">
              {placeholder}
            </option>
          )}

          {options.map((opt) => (
            <option
              key={opt.value}
              value={opt.value}
            >
              {opt.label}
            </option>
          ))}
        </select>
      </div>

      <p className="my-error text-start">
        {errors?.[id]?.message}
      </p>
    </div>
  );
};

export default SelectField;