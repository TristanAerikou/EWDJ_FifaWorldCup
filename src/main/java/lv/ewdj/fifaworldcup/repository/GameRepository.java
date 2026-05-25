package lv.ewdj.fifaworldcup.repository;

import lv.ewdj.fifaworldcup.model.Game;
import lv.ewdj.fifaworldcup.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    Boolean existsByDateOfGameAndLocation(LocalDate dateOfGame, String location);

    List<Game> findAllByDateOfGame(LocalDate dateOfGame);
}
