package lv.ewdj.fifaworldcup.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lv.ewdj.fifaworldcup.dto.InputEditGameDto;
import lv.ewdj.fifaworldcup.dto.InputGameDto;
import lv.ewdj.fifaworldcup.dto.OutputGameDto;
import lv.ewdj.fifaworldcup.model.Game;
import lv.ewdj.fifaworldcup.model.Prognosis;
import lv.ewdj.fifaworldcup.model.User;
import lv.ewdj.fifaworldcup.repository.GameRepository;
import lv.ewdj.fifaworldcup.repository.PrognosisRepository;
import lv.ewdj.fifaworldcup.repository.TeamRepository;
import lv.ewdj.fifaworldcup.repository.UserRepository;
import lv.ewdj.fifaworldcup.util.PrognosisResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GameService {

    private final PrognosisRepository prognosisRepository;
    private final TeamRepository teamRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    public List<OutputGameDto> findAllGames() {
        return gameRepository.findAll().stream()
                .map(OutputGameDto::objToDto)
                .sorted(Comparator.comparing(OutputGameDto::dateOfGame).thenComparing(OutputGameDto::timeOfGame))
                .toList();
    }

    public void saveGame(InputGameDto inputGameDto) {
        gameRepository.save(
                InputGameDto.dtoToObj(inputGameDto)
        );
    }

    public Optional<Game> getGameById(Integer id) {
        return gameRepository.findById(Long.valueOf(id));
    }

    public void updateGame(@Valid InputEditGameDto inputEditGameDto, int gameId) {
        Optional<Game> optionalGame = gameRepository.findById((long) gameId);
        if (optionalGame.isEmpty()) throw new EntityNotFoundException("Game not found");

        Game game = InputEditGameDto.dtoToObj(inputEditGameDto);
        game.setId(gameId);

        gameRepository.save(game);
    }

    public void updatePoints(
            int pointsX, int pointsY, int pointsB, int pointsC,
            Integer scoreA, Integer scoreB, int gameId) {
        /*
            Deze methode spreekt direct de repositories aan omdat hier dure & ingewikkelde bewerkingen gebeuren.
         */


        // punten per gebruiker
        List<Prognosis> prognoses = prognosisRepository.findAllByGameId(gameId);

        char winnaar = scoreA.equals(scoreB) ? 'D'
                : scoreA > scoreB ? 'A'
                : 'B';

        prognoses.forEach(prognosis -> {
//            int prA = prognosis.getGoalsTeamA();
//            int prB = prognosis.getGoalsTeamB();
//            User user = prognosis.getUser();
//
//            char prWinnaar = prA == prB ? 'D'
//                    : prA > prB ? 'A'
//                    : 'B';
//
//            if (prWinnaar == winnaar) { // juiste winnaar of gelijkspel voorspeld
//                if (scoreA.equals(prA) && scoreB.equals(prB))  // exacte uitlag voorpeld
//                    user.addPoints(pointsX);
//                else
//                    user.addPoints(pointsY);
//
//            }
        });

        // bonuspunten binnen team
        teamRepository.findAll().forEach(team -> {
            List<User> usersInTeam = userRepository.findAllByTeam(team);
            List<User> UsersWithExactlyCorrectPrognosis = new ArrayList<>();
            List<User> UsersWithCorrectPrognosis = new ArrayList<>();
            for (User u : usersInTeam) {
                List<Prognosis> prognosisList = prognosisRepository.getPrognosisByGameIdAndUserId(gameId, u.getId());
                if (!prognosisList.isEmpty()) {
                    Prognosis prognosis = prognosisList.getFirst();

                }
            }
        });

    }

    // ######################
    // Helpers              #
    // ######################

    private PrognosisResult evaluatePrognosis(int scoreA, int scoreB, Prognosis prognosis) {
        int prA = prognosis.getGoalsTeamA();
        int prB = prognosis.getGoalsTeamB();
        User user = prognosis.getUser();

        char prWinnaar = prA == prB ? 'D'
                : prA > prB ? 'A'
                : 'B';

        if (prWinnaar == winnaar) { // juiste winnaar of gelijkspel voorspeld
            if (scoreA.equals(prA) && scoreB.equals(prB))  // exacte uitlag voorpeld
                user.addPoints(pointsX);
            else
                user.addPoints(pointsY);

        }
    }
}
