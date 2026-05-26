import Requests from "./Requests"

const ManagerRequests = () => {
	return (
		<div className="px-5 lg:px-8 py-7 max-w-325 mx-auto">
			<h1 className="text-[32px] font-serif text-ink-900 mb-6">Requests</h1>
			<Requests isManager />
		</div>
	)
}

export default ManagerRequests

