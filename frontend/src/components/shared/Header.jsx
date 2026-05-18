import { Link } from "react-router"

const Header = () => {
  return (
    <div className="flex gap-4 text-black">
      <Link to="/">HOMEPAGE</Link>
      <Link to="/login">LOGIN</Link>
      <Link to="/register">REGISTER</Link>
    </div>
  )
}

export default Header