package lv.ewdj.fifaworldcup.repository;

import lv.ewdj.fifaworldcup.model.Game;
import lv.ewdj.fifaworldcup.model.Prognosis;
import lv.ewdj.fifaworldcup.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrognosisRepository extends JpaRepository<Prognosis, Integer> {

    List<Prognosis> findByGameIdAndUserUsername(int gameId, String userUsername);

    List<Prognosis> findAllByGameId(int gameId);

    List<Prognosis> getPrognosisByGameAndUser(Game game, User user);

    List<Prognosis> getPrognosisByGameIdAndUserId(int gameId, Long userId);
}
