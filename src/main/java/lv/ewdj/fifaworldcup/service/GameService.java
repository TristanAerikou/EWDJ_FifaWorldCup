package lv.ewdj.fifaworldcup.service;

import lombok.AllArgsConstructor;
import lv.ewdj.fifaworldcup.dto.GameInputDto;
import lv.ewdj.fifaworldcup.dto.GameOutputDto;
import lv.ewdj.fifaworldcup.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GameService {

    GameRepository gameRepository;

    public List<GameOutputDto> findAllGames() {
        return gameRepository.findAll().stream().map(GameOutputDto::objToDto).toList();
    }

    public void saveGame(GameInputDto gameInputDto) {
        gameRepository.save(
                GameOutputDto.dtoToObj(gameInputDto)
        );
    }

    // ######################
    // Helpers              #
    // ######################

}
