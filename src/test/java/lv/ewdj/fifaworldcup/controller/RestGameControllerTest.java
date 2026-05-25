package lv.ewdj.fifaworldcup.controller;

import lv.ewdj.fifaworldcup.exceptions.GameNotFoundException;
import lv.ewdj.fifaworldcup.exceptions.StadiumNotFoundException;
import lv.ewdj.fifaworldcup.helpers.HelperVariables;
import lv.ewdj.fifaworldcup.model.Game;
import lv.ewdj.fifaworldcup.service.GameService;
import lv.ewdj.fifaworldcup.util.DateTimeFormats;
import lv.ewdj.fifaworldcup.validator.GameValidator;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(RestGameController.class)
class RestGameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GameService gameService;

    @MockitoBean
    private GameValidator gameValidator;

    private Game provideGame(
            String landA, String landB, LocalDate dateOfGame, LocalTime timeOfGame, String location, String stadium, int stadiumCode, int scoreA, int scoreB, int capacity
    ) {
        return new Game(
                landA, landB,
                dateOfGame,
                timeOfGame,
                location,
                stadium,
                stadiumCode,
                scoreA, scoreB,
                capacity
        );
    }

    private Game provideGame() {
        return new Game(
                "France", "Italy",
                LocalDate.of(2026, 6, 25),
                LocalTime.of(12, 0),
                "Barcelona",
                "Barcelona Stadium",
                1234,
                3, 2,
                255
        );
    }

    @Test
    void getAllGames_returnsAllGames() throws Exception {
        List<Game> games = HelperVariables.provideGames().toList();
        Mockito.when(gameService.findAllGames()).thenReturn(games);

        mockMvc.perform(get("/rest/games"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$", Matchers.hasSize(games.size())))
                .andExpect(jsonPath("$[0].landA").value(games.get(0).getLandA()))
                .andExpect(jsonPath("$[0].landB").value(games.get(0).getLandB()))
                .andExpect(jsonPath("$[0].dateOfGame").value(games.get(0).getDateOfGame().format(DateTimeFormats.DATE_FORMATTER)))
                .andExpect(jsonPath("$[0].timeOfGame").value(games.get(0).getTimeOfGame().format(DateTimeFormats.TIME_FORMATTER)))
        ;

        Mockito.verify(gameService).findAllGames();
    }

    @Test
    void getAllGames_emptyList() throws Exception {
        Mockito.when(gameService.findAllGames()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/rest/games"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty())
        ;

        Mockito.verify(gameService).findAllGames();
    }

    @Test
    void getGamesByDate_returnsGames() throws Exception {
        List<Game> games = HelperVariables.provideGames()
                .filter(game ->
                        Objects.equals(game.getStadium(), "Jan Breydelstadion")
                )
                .toList();

        Mockito.when(gameService.findAllGamesByDate(LocalDate.of(2026, 5, 28))).thenReturn(games);

        mockMvc.perform(get("/rest/games/28-05-2026"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", Matchers.hasSize(games.size())))
                .andExpect(jsonPath("$", Matchers.hasSize(games.size())))
                .andExpect(jsonPath("$[-1].landA").value(games.getLast().getLandA()))
                .andExpect(jsonPath("$[-1].landB").value(games.getLast().getLandB()))
                .andExpect(jsonPath("$[-1].dateOfGame").value(games.getLast().getDateOfGame().format(DateTimeFormats.DATE_FORMATTER)))
                .andExpect(jsonPath("$[-1].timeOfGame").value(games.getLast().getTimeOfGame().format(DateTimeFormats.TIME_FORMATTER)))
        ;

        Mockito.verify(gameService).findAllGamesByDate(LocalDate.of(2026, 5, 28));
    }

    @Test
    void getAllGamesByDate_emptyList_throwsGameNotFoundException() throws Exception {

        Mockito.doThrow(new GameNotFoundException("game not found"))
                .when(gameService).findAllGamesByDate(LocalDate.of(2026, 5, 28));

        mockMvc.perform(get("/rest/games/28-05-2026"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.timestamp").exists());

        Mockito.verify(gameService).findAllGamesByDate(LocalDate.of(2026, 5, 28));
    }

    @Test
    void getAllGamesByDate_badDateFormat_ThrowsDateTimeParseException() throws Exception {

//        Mockito.doThrow(DateTimeParseException.class)
//                .when(gameService).findAllGamesByDate(LocalDate.of(2026, 5, 28));

        mockMvc.perform(get("/rest/games/28-05-202"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.timestamp").exists());

        Mockito.verify(gameService, Mockito.never()).findAllGamesByDate(Mockito.any(LocalDate.class));
    }

    @Test
    void getStadiums_returnsAllStadiums() throws Exception {
        List<String> stadiums = HelperVariables.provideGames()
                .map(Game::getStadium)
                .distinct()
                .toList();

        Mockito.when(gameService.getAllStadiums()).thenReturn(
                stadiums
        );

        mockMvc.perform(get("/rest/stadiums"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$", Matchers.hasSize(stadiums.size())))
                .andExpect(jsonPath("$[0]").value(stadiums.getFirst()))
                .andExpect(jsonPath("$[1]").value(stadiums.get(1)))
        ;

        Mockito.verify(gameService).getAllStadiums();
    }

    @Test
    void getStadiums_returnsEmptyList() throws Exception {
        Mockito.when(gameService.getAllStadiums()).thenReturn(
                Collections.emptyList()
        );

        mockMvc.perform(get("/rest/stadiums"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
        ;

        Mockito.verify(gameService).getAllStadiums();
    }

    @Test
    void getCapacitiesByStadium_returnMultipleCapacities() throws Exception {
        String stadiumName = "Jan Breydelstadion";
        String expectedCapacities = "75, 13, 1001";
        Mockito.when(gameService.getCapacitiesByStadium(stadiumName)).thenReturn(
                "This stadium has capacities of: " + expectedCapacities
        );

        mockMvc.perform(get("/rest/stadiums/capacity/" + stadiumName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isString())
                .andExpect(jsonPath("$", Matchers.containsStringIgnoringCase(expectedCapacities)));
        ;

        Mockito.verify(gameService).getCapacitiesByStadium(stadiumName);
    }

    @Test
    void getCapacitiesByStadium_returnOneCapacity() throws Exception {
        String stadiumName = "Jan Breydelstadion";
        String expectedCapacities = "75";
        Mockito.when(gameService.getCapacitiesByStadium(stadiumName)).thenReturn(
                "This stadium has capacities of: " + expectedCapacities
        );

        mockMvc.perform(get("/rest/stadiums/capacity/" + stadiumName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isString())
                .andExpect(jsonPath("$", Matchers.containsStringIgnoringCase(expectedCapacities)));
        ;

        Mockito.verify(gameService).getCapacitiesByStadium(stadiumName);
    }

    @Test
    void getCapacitiesByStadium_NoStadium_throwStadiumNotFoundException() throws Exception {
        String stadiumName = "Jan Breydelstadion";
        Mockito.when(gameService.getCapacitiesByStadium(stadiumName))
                .thenThrow(new StadiumNotFoundException("Stadium not found"));

        mockMvc.perform(get("/rest/stadiums/capacity/" + stadiumName))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.timestamp").exists());
        ;

        Mockito.verify(gameService).getCapacitiesByStadium(stadiumName);
    }



}