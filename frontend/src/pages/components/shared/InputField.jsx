const InputField = ({
  label,
  id,
  type,
  required,
  errors,
  register,
  min,
  max,
  placeholder,
  theme,
  validation = {},
}) => {
  return (
    <div className="w-full">
      <div className="flex flex-col items-start gap-2 w-full">
        {label && <label htmlFor={id} className="my-para">{label}</label>}
        <input
          type={type}
          id={id}
          className={`m-1 rounded-lg p-2 w-full text-sm ${theme}`}
          placeholder={placeholder}
          {...register(id, {
            required: required
              ? { value: required, message: `This field is required` }
              : null,
            minLength: min
              ? { value: min, message: `Minimum ${min} characters is required` }
              : null,
            maxLength: max
              ? { value: max, message: `Maximum ${max} characters is required` }
              : null,
            pattern:
              type === "email"
                ? {
                    value: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
                    message: "Invalid email",
                  }
                : null,
            ...validation,
          })}
        />
      </div>
      <p className="my-error text-start">{errors[id]?.message}</p>
    </div>
  );
};

export default InputField;