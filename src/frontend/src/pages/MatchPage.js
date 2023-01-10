import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { MatchDetailCard } from "../component/MatchDetailCard";
import { MatchSmallCard } from "../component/MatchSmallCard";
import { YearSelector } from "../component/YearSelector";

import "./MatchPage.scss";

export const MatchPage = () => {
	const [matches, setMatches] = useState([]);
	const { teamName, year } = useParams();
	const matchesUri = `/teams/${teamName}/matches/?year=${year}`;

	const matchesPresent = matches.length > 0;

	useEffect(() => {
		fetch(matchesUri)
			.then((response) => response.json())
			.then((data) => {
				setMatches(data);
			});
	}, [teamName, year]);

	// if (!teamName || !year) return null;

	return (
		<div className="MatchPage">
			<div className="year-selector">
				<div className="home-link">
					<h2>
						<Link to="/">Home</Link>
					</h2>
				</div>

				<h3> Select Year </h3>
				<YearSelector teamName={teamName} />
			</div>
			<div>
				<h1 className="page-heading">
					{teamName} matches in {year}
				</h1>

				<div
					className={
						matchesPresent ? "matches-present" : "matches-absent"
					}
				>
					<h1>{teamName} did not play any matches this year</h1>
				</div>

				{matches.map((match) => (
					<MatchDetailCard
						key={match.id}
						teamName={teamName}
						match={match}
					/>
				))}
			</div>
		</div>
	);
};
