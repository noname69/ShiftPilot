import { Oval } from 'react-loader-spinner'

export function ButtonSpinner() {
  return (
    <Oval
      height="1.5em"
      width="1.5em"
      color="#2E2E30"
      wrapperStyle={{}}
      wrapperClass=""
      visible={true}
      ariaLabel='oval-loading'
      secondaryColor="#9C9C95"
      strokeWidth={6}
      strokeWidthSecondary={2}
    />
  )
}