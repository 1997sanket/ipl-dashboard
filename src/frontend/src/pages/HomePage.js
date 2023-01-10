import { React, useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { TeamTile } from "../component/TeamTile";

import "./HomePage.scss";

export const HomePage = () => {
	const [teams, setTeams] = useState([]);

	useEffect(() => {
		fetch("http://localhost:8080/teams")
			.then((response) => response.json())
			.then((data) => {
				setTeams(data);
			});
	}, []);

	return (
		<div className="HomePage">
			<div className="header-section">
				<h1 className="app-name">Java Brains IPL Dashboard</h1>
			</div>
			<div className="team-grid">
				{teams.map((team) => (
					<TeamTile key={team.id} teamName={team.teamName} />
				))}
			</div>
		</div>
	);
};
