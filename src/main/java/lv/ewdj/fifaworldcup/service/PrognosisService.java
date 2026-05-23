package lv.ewdj.fifaworldcup.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lv.ewdj.fifaworldcup.dto.InputPrognosisDto;
import lv.ewdj.fifaworldcup.dto.OutputPrognosisDto;
import lv.ewdj.fifaworldcup.dto.OutputUserDto;
import lv.ewdj.fifaworldcup.model.Game;
import lv.ewdj.fifaworldcup.model.Prognosis;
import lv.ewdj.fifaworldcup.model.User;
import lv.ewdj.fifaworldcup.repository.PrognosisRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PrognosisService {

    private final GameService gameService;
    private final UserService userService;
    PrognosisRepository prognosisRepository;

    public List<Prognosis> getPrognosisByGameAndUser(int gameId, String username) {
        List<Prognosis> prognosisList = prognosisRepository.findByGameIdAndUserUsername(gameId, username);
        if (prognosisList.size() > 1) throw new IllegalStateException("You have more than 1 prognosis for this game... That is not normal; please contact an admin.");
        return prognosisList;
    }

    public void createOrUpdatePrognosis(InputPrognosisDto prognosisDto, int gameId, String username) {
        Optional<Game> optionalGame = gameService.getGameById(gameId);
        if (optionalGame.isEmpty()) throw new EntityNotFoundException("Game not found");

        Optional<OutputUserDto> optionalUser = userService.getUserByUsername(username);
        if (optionalUser.isEmpty()) throw new EntityNotFoundException("User not found");

        OutputUserDto userDto = optionalUser.get();

        Prognosis prognosis = new Prognosis(
                prognosisDto.goalsTeamsA(),
                prognosisDto.goalsTeamsB(),
                optionalGame.get(),
                new User(
                        userDto.id()
                )
        );
        prognosisRepository.save(prognosis);
    }

}
