import Requests from "./Requests"

const MyRequests = () => {
	return (
		<div className="px-5 lg:px-8 py-7 max-w-325 mx-auto">
			<h1 className="text-[32px] font-serif text-ink-900 mb-6">My Requests</h1>
			<Requests isUser />
		</div>
	)
}

export default MyRequests
