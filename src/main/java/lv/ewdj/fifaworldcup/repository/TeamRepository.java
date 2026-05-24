package lv.ewdj.fifaworldcup.repository;

import jakarta.persistence.NamedQuery;
import lv.ewdj.fifaworldcup.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, String> {

    boolean existsByInviteCode(String inviteCode);

    Team getTeamsByName(String name);

    Optional<Team> getTeamByInviteCode(String inviteCode);

    @Query("""
            select t
            from Team t
            join User u on u.team = t
            group by t.id
            order by sum(u.points) desc
            limit 10
            """)
    List<Team> findTopTenTeams();
}
