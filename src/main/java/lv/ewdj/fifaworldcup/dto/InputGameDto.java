package lv.ewdj.fifaworldcup.dto;

import jakarta.validation.constraints.*;
import lv.ewdj.fifaworldcup.model.Game;
import lv.ewdj.fifaworldcup.validator.ValidChecksum;
import lv.ewdj.fifaworldcup.validator.ValidDatePeriod;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@ValidChecksum(modulo = 97)
public record InputGameDto(

//        @NotBlank(message = "{wedstrijd.create.validation.landsBlank}") // clutters the error messages
        @Pattern(regexp = "^[a-zA-Z- ]+$", message = "{wedstrijd.create.validation.landPattern}")
        @Size(min = 4, max = 30, message = "{wedstrijd.create.validation.landSize}")
        @NotNull
        String landA,
//        @NotBlank(message = "{wedstrijd.create.validation.landsBlank}") // clutters the error messages
        @Pattern(regexp = "^[a-zA-Z]+$", message = "{wedstrijd.create.validation.landPattern}")
        @Size(min = 4, max = 30, message = "{wedstrijd.create.validation.landSize}")
        @NotNull
        String landB,

        @NotNull(message = "{wedstrijd.create.validation.null}")
//        @DateTimeFormat(pattern = "dd-MM-yyyy")
        @ValidDatePeriod()
        LocalDate dateOfGame,
        @NotNull(message = "{wedstrijd.create.validation.null}")
//        @DateTimeFormat(pattern = "hh-mm")
        LocalTime timeOfGame,

        String location,
        String stadium,
        @Min(value = 1000, message = "{wedstrijd.create.validation.stadiumCode}")
        @Max(value = 9999, message = "{wedstrijd.create.validation.stadiumCode}")
        Integer stadiumCode,
        Integer checksum
) {
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
