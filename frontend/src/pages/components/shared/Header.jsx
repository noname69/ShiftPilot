import { Link } from "react-router";
import { ImCheckboxChecked } from "react-icons/im";

const Header = () => {
  return (
    <header className="flex p-8 gap-2 items-center font-bold">
      <ImCheckboxChecked className="text-3xl" />
      <Link to="/"><h1>ShiftPilot</h1></Link>
    </header>
  )
}

export default Header