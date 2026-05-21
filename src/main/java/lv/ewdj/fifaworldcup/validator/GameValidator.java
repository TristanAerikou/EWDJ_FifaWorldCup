package lv.ewdj.fifaworldcup.validator;

import lv.ewdj.fifaworldcup.dto.InputGameDto;
import lv.ewdj.fifaworldcup.repository.GameRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

@Component
public class GameValidator implements Validator {

    private static GameRepository gameRepository;

    public GameValidator(GameRepository gameService) {
        GameValidator.gameRepository = gameService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return InputGameDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        InputGameDto dto = (InputGameDto) target;

        validateLands(errors, dto);
        validateTimeAndPlace(errors, dto);

    }

    private static void validateLands(Errors errors, InputGameDto dto) {
        if ( dto.landA() == null || dto.landB() == null || dto.landB().isBlank() || dto.landA().isBlank()) return;
        if (Objects.equals(dto.landA().toLowerCase(), dto.landB().toLowerCase())) {
//            errors.rejectValue("landA",
//                    "wedstrijd.create.validation.lands",
//                    "lands must not be the same");
            errors.rejectValue("landB",
                    "wedstrijd.create.validation.lands",
                    "lands must not be the same");
        }
    }

    private static void validateTimeAndPlace(Errors errors, InputGameDto dto) {
        if (dto.location() == null || dto.location().isBlank()) return;

        boolean isValid = gameRepository.existsByDateOfGameAndLocation(dto.dateOfGame(), dto.location());

        if (isValid) {
            errors.rejectValue("location",
                    "wedstrijd.create.validation.timeAndPlace",
                    "lands must not be the same");
//            errors.rejectValue("dateOfGame",
//                    "wedstrijd.create.validation.timeAndPlace",
//                    "lands must not be the same");

        }
    }
}
