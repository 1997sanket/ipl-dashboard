package com.kamble.repository;

import com.kamble.model.Match;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MatchRepository extends CrudRepository<Match, Long> {

    List<Match> getByTeam1OrTeam2OrderByDateDesc(String teamName1, String teamName2, Pageable pageable);

    // This is like service layer
    default List<Match> findRecentMatches(String teamName, int count) {

        // 0 --> 1st page and count --> number of data in the page
        return getByTeam1OrTeam2OrderByDateDesc(teamName, teamName, PageRequest.of(0, count));
    }

    @Query(value = "select * from match where (team1 = ?1 or team2 = ?1) and date like ?2% order by date desc", nativeQuery = true)
    List<Match> getMatchesForTeam(String teamName, int year);
}
