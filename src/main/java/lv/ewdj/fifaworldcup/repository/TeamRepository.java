package lv.ewdj.fifaworldcup.repository;

import lv.ewdj.fifaworldcup.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, String> {

    boolean existsByInviteCode(String inviteCode);

    Team getTeamsByName(String name);

    Optional<Team> getTeamByInviteCode(String inviteCode);
}
