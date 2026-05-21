package lv.ewdj.fifaworldcup.dto;

import jakarta.validation.constraints.*;
import lv.ewdj.fifaworldcup.validator.ValidChecksum;
import lv.ewdj.fifaworldcup.validator.ValidDatePeriod;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@ValidChecksum(modulo = 97)
public record InputGameDto(

        @Pattern(regexp = "^[a-zA-Z- ]+$", message = "must consist of letters only")
        @Size(min = 4, max = 30)
        @NotNull
        String landA,
        @Pattern(regexp = "^[a-zA-Z]+$", message = "must consist of letters only")
        @Size(min = 4, max = 30)
        @NotNull
        String landB,

        @NotNull(message = "must not be empty")
        @DateTimeFormat(pattern = "dd-MM-yyyy")
        @ValidDatePeriod()
        LocalDate dateOfGame,
        @NotNull(message = "must not be empty")
        @DateTimeFormat(pattern = "hh-mm")
        LocalTime timeOfGame,

        String location,
        String stadium,
        @Min(value = 1000, message = "must be exactlly 4 characters")
        @Max(value = 9999, message = "must be exactlly 4 characters")
        Integer stadiumCode,
        Integer checksum
) {
}
