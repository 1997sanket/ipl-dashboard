import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import "./App.scss";
import { HomePage } from "./pages/HomePage";
import { MatchPage } from "./pages/MatchPage";
import { TeamPage } from "./pages/TeamPage";

function App() {
	return (
		<div className="">
			<Router>
				<Routes>
					<Route path="/" element={<HomePage />}></Route>
					<Route
						path="/teams/:teamName"
						element={<TeamPage />}
					></Route>
					<Route
						path="/teams/:teamName/matches/:year"
						element={<MatchPage />}
					></Route>
				</Routes>
			</Router>
		</div>
	);
}

export default App;
