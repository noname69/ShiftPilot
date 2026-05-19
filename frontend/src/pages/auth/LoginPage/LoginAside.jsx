import LoginChart from "./LoginChart"

const LoginAside = () => {
    return (
        <aside className="hidden lg:flex flex-col w-1/2 h-full gap-4 items-center justify-center p-20">
            <div className="my-card w-full">
                <p className="my-para">THIS WEEK</p>
                <h1 className="font-bold">42 Shifts scheduled</h1>
                <LoginChart></LoginChart>
                <hr className="my-hr" />
                <div className="flex justify-end">
                    <p className="my-para">7 employees</p>
                </div>
            </div>
            <h1 className="text-2xl font-serif">
                "We cut our weekly scheduling time <span className="italic">from three hours to twenty minutes."</span>
            </h1>
            <div className="flex items-center gap-3 p-3 rounded-xl border border-gray-100 self-start">
                <div className="w-10 h-10 rounded-full bg-gray-200" />
                <div className="flex flex-col leading-tight">
                    <p className="text-sm font-semibold text-gray-900">
                        Priya N.
                    </p>
                    <p className="text-xs text-gray-500">
                        Owner, Northside Café
                    </p>
                </div>

            </div>
        </aside>
    )
}

export default LoginAside
