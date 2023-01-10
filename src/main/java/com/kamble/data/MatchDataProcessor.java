package com.kamble.data;

import com.kamble.model.Match;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDate;

public class MatchDataProcessor implements ItemProcessor<MatchInput, Match> {

    @Override
    public Match process(final MatchInput matchInput) throws Exception {

        // First innings = team1
        String firstInningsTeam = null;
        String secondInningsTeam = null;

        if("bat".equals(matchInput.getToss_decision())) {
            firstInningsTeam = matchInput.getToss_winner();

            if(firstInningsTeam.equals(matchInput.getTeam1()))
                secondInningsTeam = matchInput.getTeam2();
            else
                secondInningsTeam = matchInput.getTeam1();
        }
        else {
            secondInningsTeam = matchInput.getToss_winner();

            if(secondInningsTeam.equals(matchInput.getTeam1()))
                firstInningsTeam = matchInput.getTeam2();
            else
                firstInningsTeam = matchInput.getTeam1();
        }

        Match match = Match.builder()
                .id(Long.parseLong(matchInput.getId()))
                .city(matchInput.getCity())
                .date(LocalDate.parse(matchInput.getDate()))
                .playerOfMatch(matchInput.getPlayer_of_match())
                .venue(matchInput.getVenue())
                .team1(firstInningsTeam)
                .team2(secondInningsTeam)
                .tossWinner(matchInput.getToss_winner())
                .tossDecision(matchInput.getToss_decision())
                .matchWinner(matchInput.getWinner())
                .result(matchInput.getResult())
                .resultMargin(matchInput.getResult_margin())
                .eliminator(matchInput.getEliminator())
                .umpire1(matchInput.getUmpire1())
                .umpire2(matchInput.getUmpire2())
                .build();

        return match;
    }
}
