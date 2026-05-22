package lv.ewdj.fifaworldcup.repository;

import lv.ewdj.fifaworldcup.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, String> {

}
