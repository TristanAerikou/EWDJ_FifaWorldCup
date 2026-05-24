package lv.ewdj.fifaworldcup.dto;

import lv.ewdj.fifaworldcup.model.Game;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record OutputGameDto
        (
                int id,

                String team1,
                String team2,

                LocalDate dateOfGame,
                LocalTime timeOfGame,

                String Location,
                String stadium,
                boolean finished,

                int scoreA,
                int scoreB
        ) {

    public static OutputGameDto objToDto(Game game) {
        return  new OutputGameDto(
                game.getId(),

                game.getLandA(),
                game.getLandB(),

                game.getDateOfGame(),
                game.getTimeOfGame(),

                game.getLocation(),
                game.getStadium(),

                LocalDateTime.of(
                        game.getDateOfGame(),
                        game.getTimeOfGame()
                ).isBefore(LocalDateTime.now()),

                game.getScoreA(),
                game.getScoreB()
        );
    }
}
