package lv.ewdj.fifaworldcup.dto;

import lv.ewdj.fifaworldcup.model.Game;

import java.time.LocalDate;
import java.time.LocalTime;

public record OutputGameDto
        (
                String team1,
                String team2,

                LocalDate dateOfGame,
                LocalTime timeOfGame,

                String Location,
                String stadium
        ) {

    public static OutputGameDto objToDto(Game game) {
        return  new OutputGameDto(
                game.getLandA(),
                game.getLandB(),

                game.getDateOfGame(),
                game.getTimeOfGame(),

                game.getLocation(),
                game.getStadium()
        );
    }

    public static Game dtoToObj(InputGameDto dto) {
        return new Game(
                dto.landA(),
                dto.landB(),
                dto.dateOfGame(),
                dto.timeOfGame(),
                dto.location(),
                dto.stadium(),
                dto.stadiumCode() != null ? dto.stadiumCode() : -1
        );
    }
}
