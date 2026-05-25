package lv.ewdj.fifaworldcup.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lv.ewdj.fifaworldcup.dto.InputEditGameDto;
import lv.ewdj.fifaworldcup.dto.InputGameDto;
import lv.ewdj.fifaworldcup.dto.OutputGameDto;
import lv.ewdj.fifaworldcup.exceptions.GameNotFoundException;
import lv.ewdj.fifaworldcup.exceptions.StadiumNotFoundException;
import lv.ewdj.fifaworldcup.model.Game;
import lv.ewdj.fifaworldcup.model.Prognosis;
import lv.ewdj.fifaworldcup.model.User;
import lv.ewdj.fifaworldcup.repository.GameRepository;
import lv.ewdj.fifaworldcup.repository.PrognosisRepository;
import lv.ewdj.fifaworldcup.repository.TeamRepository;
import lv.ewdj.fifaworldcup.repository.UserRepository;
import lv.ewdj.fifaworldcup.util.PrognosisResult;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GameService {

    private final PrognosisRepository prognosisRepository;
    private final TeamRepository teamRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    public List<OutputGameDto> findAllGamesDtos() {
        return gameRepository.findAll().stream()
                .map(OutputGameDto::objToDto)
                .sorted(Comparator.comparing(OutputGameDto::dateOfGame).thenComparing(OutputGameDto::timeOfGame))
                .toList();
    }
    public List<Game> findAllGames() {
        return gameRepository.findAll();
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

        Optional<Game> optionalGame = getGameById(gameId);
        if (optionalGame.isEmpty()) throw new EntityNotFoundException("Game not found");

        Game game = optionalGame.get();
        if (game.getScoreA() == scoreA && game.getScoreB() == scoreB)
            return;

        /*
            Deze methode spreekt direct de repositories aan omdat hier dure & ingewikkelde bewerkingen gebeuren.
         */


        // punten per gebruiker

        char winnaar = scoreA.equals(scoreB) ? 'D'
                : scoreA > scoreB ? 'A'
                : 'B';

        rewardPoints(pointsX, pointsY, scoreA, scoreB, gameId, winnaar);

        // bonuspunten binnen team
        rewardBonusPoints(pointsB, pointsC, scoreA, scoreB, gameId, winnaar);

    }

    private void rewardBonusPoints(int pointsB, int pointsC, Integer scoreA, Integer scoreB, int gameId, char winnaar) {
        teamRepository.findAll().forEach(team -> {
            List<User> usersInTeam = userRepository.findAllByTeam(team);
            List<User> UsersWithExactlyCorrectPrognosis = new ArrayList<>();
            List<User> UsersWithCorrectPrognosis = new ArrayList<>();
            for (User u : usersInTeam) {
                List<Prognosis> prognosisList = prognosisRepository.getPrognosisByGameIdAndUserId(gameId, u.getId());
                if (!prognosisList.isEmpty()) {
                    Prognosis prognosis = prognosisList.getFirst();
                    switch (evaluatePrognosis(winnaar, scoreA, scoreB, prognosis)) {
                        case Exactly_Correct -> {
                            UsersWithExactlyCorrectPrognosis.add(u);
                            UsersWithCorrectPrognosis.add(u);
                        }
                        case Correct -> UsersWithCorrectPrognosis.add(u);
                    }
                }
            }

            if (UsersWithExactlyCorrectPrognosis.size() == 1) {
                User winner = UsersWithExactlyCorrectPrognosis.getFirst();
                winner.addPoints(pointsB);
                userRepository.save(winner);
            }
            if (UsersWithCorrectPrognosis.size() == 1) {
                User winner = UsersWithCorrectPrognosis.getFirst();
                winner.addPoints(pointsC);
                userRepository.save(winner);
            }

        });
    }

    private void rewardPoints(int pointsX, int pointsY, Integer scoreA, Integer scoreB, int gameId, char winnaar) {
        List<Prognosis> prognoses = prognosisRepository.findAllByGameId(gameId);

        prognoses.forEach(prognosis -> {
            User user = prognosis.getUser();

            switch (evaluatePrognosis(winnaar, scoreA, scoreB, prognosis)) {
                case Exactly_Correct -> {
                    user.addPoints(pointsX);

                }
                case Correct -> {
                    user.addPoints(pointsY);

                }
            }
            userRepository.save(user);
        });
    }

    // ######################
    // Helpers              #
    // ######################

    private PrognosisResult evaluatePrognosis(char winnaar, int scoreA, int scoreB, Prognosis prognosis) {
        int prA = prognosis.getGoalsTeamA();
        int prB = prognosis.getGoalsTeamB();

        char prWinnaar = prA == prB ? 'D'
                : prA > prB ? 'A'
                : 'B';

        if (prWinnaar == winnaar) { // juiste winnaar of gelijkspel voorspeld
            if (scoreA == prA && scoreB == prB)  // exacte uitlag voorpeld
                return PrognosisResult.Exactly_Correct;
//                user.addPoints(pointsX);
            else
                return PrognosisResult.Correct;
//                user.addPoints(pointsY);
        } else
            return PrognosisResult.Incorrect;
    }

    // REST
    public List<Game> findAllGamesByDate(LocalDate date) {
        List<Game> games = gameRepository.findAllByDateOfGame(date);
        if (games.isEmpty()) throw new GameNotFoundException("No game was found with this date");
        return games;
    }

    public List<String> getAllStadiums() {
        List<Game> games = findAllGames();
        List<String> stadiums = games.stream()
                .map(Game::getStadium)
                .distinct()
                .toList();
        return stadiums;
    }

    public String getCapacitiesByStadium(String stadium) {
        List<Game> games = gameRepository.findAllByStadium(stadium);

        if (games.isEmpty()) throw new StadiumNotFoundException("No StadiumNotFound was found with this name");

        String str =  games.stream()
                .map(game -> String.valueOf(game.getCapacity()))
                .collect(Collectors.joining(", "));
        return "This stadium has capacities of: %s".formatted(str);
    }
}
