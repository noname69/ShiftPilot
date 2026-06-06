import { Checkbox } from '@headlessui/react'

export default function Example({ checked, onChange, disabled }) {

  return (
    <Checkbox
      checked={checked}
      onChange={onChange}
      disabled={disabled}
      className="group block size-4 rounded border bg-white data-checked:bg-ink-700 data-disabled:bg-gray-200 data-disabled:border-gray-300"
    >
      <svg className="stroke-white opacity-0 group-data-checked:opacity-100" viewBox="0 0 14 14" fill="none">
        <path d="M3 8L6 11L11 3.5" strokeWidth={2} strokeLinecap="round" strokeLinejoin="round" />
      </svg>
    </Checkbox>
  )
}