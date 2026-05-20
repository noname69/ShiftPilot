import { NavLink } from "react-router";

const Footer = () => {

  const year = new Date().getFullYear();

  return (
    <footer className="text-xs flex justify-between p-8 text-gray-400">
      <p>&copy; {year} ShiftPilot</p>
      <nav>
        <ul className="flex gap-4">
          <li><NavLink>Privacy</NavLink></li>
          <li><NavLink>Terms</NavLink></li>
          <li><NavLink>Status</NavLink></li>
        </ul>
      </nav>
    </footer>
  )
}

export default Footer
