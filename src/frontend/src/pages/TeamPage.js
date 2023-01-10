import { React, useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { MatchDetailCard } from "../component/MatchDetailCard";
import { MatchSmallCard } from "../component/MatchSmallCard";
import { PieChart } from "react-minimal-pie-chart";

import "./TeamPage.scss";

export const TeamPage = () => {
	const [team, setTeam] = useState({ teamName: "", recentMatches: [] });
	const { teamName } = useParams();

	const getTeamUri = `http://localhost:8080/teams/${teamName}`;

	useEffect(() => {
		fetch(getTeamUri)
			.then((response) => response.json())
			.then((data) => {
				setTeam(data);
			});
	}, [teamName]); // dependent on teamName, useEffect will run when teamName is changed.

	if (!team || !team.teamName) return <h1>Team not found !</h1>;

	return (
		<div className="TeamPage">
			<div className="home-link">
				<h2 className="home">
					<Link to="/">Home </Link>
				</h2>
			</div>
			<div className="team-name-section">
				<h1 className="team-name">{team.teamName}</h1>
			</div>
			<div className="win-loss-section">
				Wins / Losses
				<PieChart
					data={[
						{
							title: "Losses",
							value: team.totalMatches - team.totalWins,
							color: "#a34d5d",
						},
						{
							title: "Wins",
							value: team.totalWins,
							color: "#4da375",
						},
					]}
				/>
			</div>
			<div className="match-detail-section">
				<h3>Latest Matches</h3>
				<MatchDetailCard
					teamName={team.teamName}
					match={team.recentMatches[0]}
				/>
			</div>
			{team.recentMatches.slice(1).map((match) => (
				<MatchSmallCard
					key={match.id}
					teamName={team.teamName}
					match={match}
				/>
			))}
			<div className="more-link">
				<Link
					to={`/teams/${teamName}/matches/${process.env.REACT_APP_DATA_END_YEAR}`}
				>
					More >
				</Link>
			</div>
		</div>
	);
};
