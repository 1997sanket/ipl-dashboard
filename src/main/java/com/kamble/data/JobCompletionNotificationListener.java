package com.kamble.data;

import com.kamble.model.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private final EntityManager entityManager;

    @Autowired
    public JobCompletionNotificationListener(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results");

            Map<String, Team> teamDataMap = new HashMap<>();

            // JPQL

            entityManager.createQuery("select m.team1, count(*) from Match m group by m.team1", Object[].class)
                    .getResultList()
                    .stream()
                    .map(o -> Team.builder().teamName((String)o[0]).totalMatches((Long)o[1]).build())
                    .forEach(team -> teamDataMap.put(team.getTeamName(), team));


            entityManager.createQuery("select m.team2, count(*) from Match m group by m.team2", Object[].class)
                    .getResultList()
                    .stream()
                    .forEach(o -> {
                                String teamName = (String)o[0];
                                Long teamMatches = (Long)o[1];

                                if(teamDataMap.containsKey(teamName)) {
                                    Team t = teamDataMap.get(teamName);
                                    t.setTotalMatches(t.getTotalMatches() + teamMatches);
                                }
                                else {
                                    if(teamName != null || !teamName.isEmpty())
                                        teamDataMap.put(teamName, Team.builder().teamName(teamName).totalMatches(teamMatches).build());
                                }
                            });


            entityManager.createQuery("select m.matchWinner, count(*) from Match m group by m.matchWinner", Object[].class)
                    .getResultList()
                    .stream()
                    .forEach(o -> {
                        String matchWinner = (String)o[0];
                        Long totalWins = (Long)o[1];

                        if(teamDataMap.containsKey(matchWinner)) {
                            Team t = teamDataMap.get(matchWinner);
                            t.setTotalWins(totalWins);
                        }
                    });

            teamDataMap.values().forEach(team -> entityManager.persist(team));
            teamDataMap.values().forEach(System.out::println);

        }
    }
}
