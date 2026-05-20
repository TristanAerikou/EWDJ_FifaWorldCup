package lv.ewdj.fifaworldcup.dto;

import lv.ewdj.fifaworldcup.model.Game;

import java.time.LocalDate;
import java.time.LocalTime;

public record GameOutputDto
        (
                String team1,
                String team2,

                LocalDate dateOfGame,
                LocalTime timeOfGame,

                String Location,
                String stadium
        ) {

    public static GameOutputDto objToDto(Game game) {
        return  new GameOutputDto(
                game.getLandA(),
                game.getLandB(),

                game.getDateOfGame(),
                game.getTimeOfGame(),

                game.getLocation(),
                game.getStadium()
        );
    }

    public static Game dtoToObj(GameInputDto dto) {
        return new Game(
                dto.landA(),
                dto.landB(),
                dto.dateOfGame(),
                dto.timeOfGame(),
                dto.location(),
                dto.stadium(),
                dto.stadiumCode()
        );
    }
}
