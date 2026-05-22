package lv.ewdj.fifaworldcup.controller;

import lv.ewdj.fifaworldcup.dto.InputGameDto;
import lv.ewdj.fifaworldcup.dto.OutputGameDto;
import lv.ewdj.fifaworldcup.helpers.helperVariables;
import lv.ewdj.fifaworldcup.service.GameService;
import lv.ewdj.fifaworldcup.validator.GameValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.Errors;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(GameController.class)
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GameService gameService;

    @MockitoBean
    private GameValidator gameValidator;

    @BeforeEach
    void setUp() {
        Mockito.when(gameValidator.supports(InputGameDto.class)).thenReturn(true);
    }

    // ######################
    // game/allGames        #
    // ######################

    @Test
//    @WithMockUser
    void getAllGames() throws Exception {

        List<OutputGameDto> expectedGames = helperVariables.provideExpectedGames();

        Mockito.when(gameService.findAllGames()).thenReturn(expectedGames);

        mockMvc.perform(get("/game/allGames"))
                .andExpect(status().isOk())
                .andExpect(view().name("homeScreen"))
                .andExpect(model().attributeExists("allGames"))
                .andExpect(model().attribute("allGames", expectedGames));
    }

    // ######################
    // game/create          #
    // ######################

    @Test
    void showCreateForm() throws Exception {
        mockMvc.perform(get("/game/create"))
                .andExpect(view().name("gameCreate"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("inputGameDto"));
    }

    private static InputGameDto provideMinimalValidInputGameDto() {
        return new InputGameDto(
                "France",
                "Netherlands",
                LocalDate.of(2026, 5, 23),
                LocalTime.of(12, 0),
                null,
                null,
                null,
                null
        );
    }

    @Test
    void postCreateValidRequest() throws Exception {
        Mockito.doNothing().when(gameService).saveGame(Mockito.any(InputGameDto.class));

        InputGameDto validRequest = provideMinimalValidInputGameDto();

        mockMvc.perform(post("/game/create")
                        .flashAttr("inputGameDto", validRequest)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/game/allgames")) //TODO CHANAGE
                .andExpect(redirectedUrl("/game/allgames")); //TODO CHANAGE

        Mockito.verify(gameService, Mockito.times(1)).saveGame(validRequest);

    }

    public static Stream<Arguments> ProvideInvalidGame() {
        return Stream.of(
                Arguments.of("", "", null, null, null, null, new String[] {"landA", "landB", "dateOfGame", "timeOfGame"}),
                Arguments.of("   ", "   ", LocalDate.of(2026, 5, 25), LocalTime.of(12, 0), null, null, new String[] {"landA", "landB"}),
                Arguments.of("\t   ", "\t   ", LocalDate.of(2026, 5, 25), LocalTime.of(12, 0), null, null, new String[] {"landA", "landB"}),
                Arguments.of("\t", "\t", LocalDate.of(2026, 5, 25), LocalTime.of(12, 0), null, null, new String[] {"landA", "landB"}),
                Arguments.of("aaa", "aaa", null, null, null, null, new String[] {"landA", "landB"}),
                Arguments.of("aaaa", "zzzz", null, null, null, null, new String[] {"dateOfGame", "timeOfGame"}),
                Arguments.of("aaaa", "zzzz", LocalDate.of(2025, 5, 25), null, null, null, new String[] {"dateOfGame", "timeOfGame"}),
                Arguments.of("aaaa", "zzzz", LocalDate.of(2027, 5, 25), LocalTime.of(12, 0), null, null, new String[] {"dateOfGame"}),

                Arguments.of("aaaa", "zzzz", LocalDate.of(2025, 5, 25), LocalTime.of(12, 0), 0, 1, new String[] {"checksum"}),
                Arguments.of("aaaa", "zzzz", LocalDate.of(2025, 5, 25), LocalTime.of(12, 0), 97, 0, new String[] {"stadiumCode"}),
                Arguments.of("aaaa", "zzzz", LocalDate.of(2025, 5, 25), LocalTime.of(12, 0), 972, 0, new String[] {"stadiumCode", "checksum"}),
                Arguments.of("aaaa", "zzzz", LocalDate.of(2025, 5, 25), LocalTime.of(12, 0), 9797, 25, new String[] {"checksum"}),
                Arguments.of("aaaa", "zzzz", LocalDate.of(2025, 5, 25), LocalTime.of(12, 0), 45454, 25, new String[] {"checksum"}),
                Arguments.of("aaaa", "zzzz", LocalDate.of(2025, 5, 25), LocalTime.of(12, 0), 9797, null, new String[] {"checksum"})
        );
    }

    @ParameterizedTest
    @MethodSource("ProvideInvalidGame")
    void postCreateInvalidRequest(String landA, String landB, LocalDate dateOfGame, LocalTime timeOfGame, Integer stadiumCode, Integer checksum, String[] expectedErrors) throws Exception {

        trainGameValidator(expectedErrors);

        InputGameDto invalidRequest = new InputGameDto(
                landA,
                landB,
                dateOfGame,
                timeOfGame,
                null,
                null,
                stadiumCode,
                checksum
        );

        mockMvc.perform(post("/game/create")
                .flashAttr("inputGameDto", invalidRequest)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("gameCreate"))
                .andExpect(model().attributeHasFieldErrors("inputGameDto", expectedErrors));

        Mockito.verify(gameService, Mockito.never()).saveGame(Mockito.any());

    }

    private void trainGameValidator(String[] expectedErrors) {
        Mockito.doAnswer(invocation -> {
            Errors errors = invocation.getArgument(1);
            for (String field : expectedErrors) {
                errors.rejectValue(field, "error.code", "Invalid field value");
            }
            return null;
        }).when(gameValidator).validate(Mockito.any(InputGameDto.class), Mockito.any(Errors.class));
    }


}