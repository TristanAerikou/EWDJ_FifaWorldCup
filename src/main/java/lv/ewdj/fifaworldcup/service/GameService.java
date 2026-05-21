package lv.ewdj.fifaworldcup.service;

import lombok.AllArgsConstructor;
import lv.ewdj.fifaworldcup.dto.InputGameDto;
import lv.ewdj.fifaworldcup.dto.OutputGameDto;
import lv.ewdj.fifaworldcup.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class GameService {

    GameRepository gameRepository;

    public List<OutputGameDto> findAllGames() {
        return gameRepository.findAll().stream()
                .map(OutputGameDto::objToDto)
                .sorted(Comparator.comparing(OutputGameDto::dateOfGame).thenComparing(OutputGameDto::timeOfGame))
                .toList();
    }

    public void saveGame(InputGameDto inputGameDto) {
        gameRepository.save(
                OutputGameDto.dtoToObj(inputGameDto)
        );
    }

    // ######################
    // Helpers              #
    // ######################

}
