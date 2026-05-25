package lv.ewdj.fifaworldcup.controller;

import lv.ewdj.fifaworldcup.dto.InputEditGameDto;
import lv.ewdj.fifaworldcup.dto.InputGameDto;
import lv.ewdj.fifaworldcup.dto.InputPrognosisDto;
import lv.ewdj.fifaworldcup.dto.OutputGameDto;
import lv.ewdj.fifaworldcup.helpers.HelperVariables;
import lv.ewdj.fifaworldcup.model.Game;
import lv.ewdj.fifaworldcup.service.GameService;
import lv.ewdj.fifaworldcup.service.PrognosisService;
import lv.ewdj.fifaworldcup.validator.GameValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.Errors;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(GameController.class)
@AutoConfigureMockMvc(addFilters = true)
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GameService gameService;

    @MockitoBean
    private PrognosisService prognosisService;

    @MockitoBean
    private GameValidator gameValidator;

    TestingAuthenticationToken testPrincipal = new TestingAuthenticationToken("testUser", "password", "ROLE_USER");

    @BeforeEach
    void setUp() {
        Mockito.when(gameValidator.supports(InputGameDto.class)).thenReturn(true);

//        mockGame = Mockito.mock(Game.class);
//        Mockito.lenient().when(mockGame.getId()).thenReturn(1);
//        Mockito.lenient().when(mockGame.getLandA()).thenReturn("france");
//        Mockito.lenient().when(mockGame.getLandB()).thenReturn("italy");
//        Mockito.lenient().when(mockGame.getScoreA()).thenReturn(1);
//        Mockito.lenient().when(mockGame.getScoreA()).thenReturn(2);
//        Mockito.lenient().when(mockGame.getDateOfGame()).then(2);
    }

    // ######################
    // game/allGames        #
    // ######################

    @Test
//    @WithMockUser
    void getAllGames() throws Exception {

        List<OutputGameDto> expectedGames = HelperVariables.provideExpectedGamesAsDtos();

        Mockito.when(gameService.findAllGamesDtos()).thenReturn(expectedGames);

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
                null,
                255
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
                .andExpect(view().name("redirect:/game/allGames"))
                .andExpect(redirectedUrl("/game/allGames"));

        Mockito.verify(gameService, Mockito.times(1)).saveGame(validRequest);

    }

    //hier staan fouten in ivm "2025" vs "2026" en zo... nie al te erg voor deze testen
    public static Stream<Arguments> ProvideInvalidGame() {
        return Stream.of(
                Arguments.of("", "", null, null, null, null, null, new String[]{"landA", "landB", "dateOfGame", "timeOfGame"}),
                Arguments.of("   ", "   ", LocalDate.of(2026, 5, 25), LocalTime.of(12, 0), null, null,  255, new String[]{"landA", "landB"}),
                Arguments.of("\t   ", "\t   ", LocalDate.of(2026, 5, 25), LocalTime.of(12, 0), null, null, 255,  new String[]{"landA", "landB"}),
                Arguments.of("\t", "\t", LocalDate.of(2026, 5, 25), LocalTime.of(12, 0), null, null, 255,  new String[]{"landA", "landB"}),
                Arguments.of("aaa", "aaa", null, null, null, null, null, new String[]{"landA", "landB"}),
                Arguments.of("aaaa", "zzzz", null, null, null, null, null, new String[]{"dateOfGame", "timeOfGame"}),
                Arguments.of("aaaa", "zzzz", LocalDate.of(2025, 5, 25), null, null, null, 255,  new String[]{"dateOfGame", "timeOfGame"}),
                Arguments.of("aaaa", "zzzz", LocalDate.of(2027, 5, 25), LocalTime.of(12, 0), null, null, 255,  new String[]{"dateOfGame"}),

                Arguments.of("aaaa", "zzzz", LocalDate.of(2025, 5, 25), LocalTime.of(12, 0), 0, 1, 255,  new String[]{"checksum"}),
                Arguments.of("aaaa", "zzzz", LocalDate.of(2025, 5, 25), LocalTime.of(12, 0), 97, 0, 255,  new String[]{"stadiumCode"}),
                Arguments.of("aaaa", "zzzz", LocalDate.of(2025, 5, 25), LocalTime.of(12, 0), 972, 0, 255,  new String[]{"stadiumCode", "checksum"}),
                Arguments.of("aaaa", "zzzz", LocalDate.of(2025, 5, 25), LocalTime.of(12, 0), 9797, 25, 255,  new String[]{"checksum"}),
                Arguments.of("aaaa", "zzzz", LocalDate.of(2025, 5, 25), LocalTime.of(12, 0), 45454, 25, 255,  new String[]{"checksum"}),
                Arguments.of("aaaa", "zzzz", LocalDate.of(2025, 5, 25), LocalTime.of(12, 0), 9797, null, 255,  new String[]{"checksum"}),
                Arguments.of("aaaa", "zzzz", LocalDate.of(2025, 5, 25), LocalTime.of(12, 0), 9797, 0, -1,  new String[]{"capacity"}),
                Arguments.of("aaaa", "zzzz", LocalDate.of(2025, 5, 25), LocalTime.of(12, 0), 9797, 0, -15,  new String[]{"capacity"})
        );
    }

    @ParameterizedTest
    @MethodSource("ProvideInvalidGame")
    void postCreateInvalidRequest(String landA, String landB, LocalDate dateOfGame, LocalTime timeOfGame, Integer stadiumCode, Integer checksum, Integer capacity, String[] expectedErrors) throws Exception {

        trainGameValidator(expectedErrors);

        InputGameDto invalidRequest = new InputGameDto(
                landA,
                landB,
                dateOfGame,
                timeOfGame,
                null,
                null,
                stadiumCode,
                checksum,
                capacity
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

    // ######################
    // prongosis/{id}       #
    // ######################

    @Test
    void showPrognosisViewNoPrognosis() throws Exception {
        int gameId = 1;
        Game game = HelperVariables.provideGame();

        Mockito.when(gameService.getGameById(gameId)).thenReturn(Optional.of(game));
        Mockito.when(prognosisService.getPrognosisByGameAndUser(Mockito.anyInt(), Mockito.anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/game/prognosis/1")
                        .principal(testPrincipal)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("prognosisView"))
                .andExpect(model().attributeExists("game"));
    }

    @Test
    void showPrognosisViewWithPrognosis() throws Exception {
        int gameId = 1;
        Game game = HelperVariables.provideGame();

        Mockito.when(gameService.getGameById(gameId)).thenReturn(Optional.of(game));
        Mockito.when(prognosisService.getPrognosisByGameAndUser(Mockito.anyInt(), Mockito.anyString())).thenReturn(Collections.singletonList(HelperVariables.providePrognosis()));

        mockMvc.perform(get("/game/prognosis/1")
                        .principal(testPrincipal)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("prognosisView"))
                .andExpect(model().attributeExists("game"))
                .andExpect(model().attributeExists("prognosis"))
                .andExpect(model().attributeExists("inputPrognosisDto"));

        Mockito.verify(gameService).getGameById(gameId);
        Mockito.verify(prognosisService).getPrognosisByGameAndUser(Mockito.anyInt(), Mockito.anyString());
    }

    @Test
    void showPrognosisNoGameFound() throws Exception {
        int gameId = 1;
        Game game = HelperVariables.provideGame();

        Mockito.when(gameService.getGameById(gameId)).thenReturn(Optional.empty());
//        Mockito.when(prognosisService.getPrognosisByGameAndUser(Mockito.anyInt(), Mockito.anyString())).thenReturn(Collections.singletonList(helperVariables.providePrognosis()));

        mockMvc.perform(get("/game/prognosis/1")
                        .principal(testPrincipal)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("error/error"));

        Mockito.verify(gameService).getGameById(gameId);
    }

    // ############################################
    // POST /game/prognosis/{gameId}              #
    // ############################################

    @Test
    void postPrognosisValidRequest() throws Exception {
        int gameId = 1;
        InputPrognosisDto validPrognosis = new InputPrognosisDto(2, 1); // adjust constructor to match your record/class

        Mockito.doNothing()
                .when(prognosisService)
                .createOrUpdatePrognosis(Mockito.any(InputPrognosisDto.class), Mockito.eq(gameId), Mockito.anyString());

        mockMvc.perform(post("/game/prognosis/{gameId}", gameId)
                        .flashAttr("inputPrognosisDto", validPrognosis)
                        .principal(testPrincipal)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/game/prognosis/" + gameId));

        Mockito.verify(prognosisService, Mockito.times(1))
                .createOrUpdatePrognosis(Mockito.any(), Mockito.eq(gameId), Mockito.eq("testUser"));
    }

    @Test
    void postPrognosisInvalidRequest() throws Exception {
        int gameId = 1;

        Game game = HelperVariables.provideGame();
        Mockito.when(gameService.getGameById(gameId)).thenReturn(Optional.of(game));

        Mockito.doAnswer(invocation -> {
            Errors errors = invocation.getArgument(1);
            errors.rejectValue("goalsTeamsA", "error.code", "Score A is required");
            errors.rejectValue("goalsTeamsB", "error.code", "Score B is required");
            return null;
        }).when(gameValidator).validate(Mockito.any(InputPrognosisDto.class), Mockito.any(Errors.class));

        InputPrognosisDto invalidPrognosis = new InputPrognosisDto(null, null);

        mockMvc.perform(post("/game/prognosis/{gameId}", gameId)
                        .flashAttr("inputPrognosisDto", invalidPrognosis)
                        .principal(testPrincipal)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("prognosisView"))
                .andExpect(model().attributeHasFieldErrors("inputPrognosisDto", "goalsTeamsA", "goalsTeamsB"));

        Mockito.verify(prognosisService, Mockito.never())
                .createOrUpdatePrognosis(Mockito.any(), Mockito.anyInt(), Mockito.anyString());
    }

    @Test
    void postPrognosisFlashAttributeOnSuccess() throws Exception {
        int gameId = 1;
        InputPrognosisDto validPrognosis = new InputPrognosisDto(2, 1);

        Mockito.doNothing()
                .when(prognosisService)
                .createOrUpdatePrognosis(Mockito.any(), Mockito.anyInt(), Mockito.anyString());

        mockMvc.perform(post("/game/prognosis/{gameId}", gameId)
                        .flashAttr("inputPrognosisDto", validPrognosis)
                        .principal(testPrincipal)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("successMessage"));
    }

    // ############################################
    // GET /game/edit/{gameId}                    #
    // ############################################

    @Test
    void showEditFormGameFound_scoreNotYetEditable() throws Exception {
        int gameId = 1;

        Game futureGame = HelperVariables.futureGame();
        Mockito.when(gameService.getGameById(gameId)).thenReturn(Optional.of(futureGame));

        mockMvc.perform(get("/game/edit/{gameId}", gameId))
                .andExpect(status().isOk())
                .andExpect(view().name("gameEdit"))
                .andExpect(model().attributeExists("inputEditGameDto"))
                .andExpect(model().attributeExists("canEditScore"))
                .andExpect(model().attribute("canEditScore", false));
    }

    @Test
    void showEditFormGameFound_scoreIsEditable() throws Exception {
        int gameId = 1;

        Game pastGame = HelperVariables.provideGameInThePast();
        Mockito.when(gameService.getGameById(gameId)).thenReturn(Optional.of(pastGame));

        mockMvc.perform(get("/game/edit/{gameId}", gameId))
                .andExpect(status().isOk())
                .andExpect(view().name("gameEdit"))
                .andExpect(model().attributeExists("inputEditGameDto"))
                .andExpect(model().attribute("canEditScore", true));
    }

    @Test
    void showEditFormGameNotFound() throws Exception {
        int gameId = 99;
        Mockito.when(gameService.getGameById(gameId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/game/edit/{gameId}", gameId))
                .andExpect(status().isOk())
                .andExpect(view().name("error/error"));
    }

    // ############################################
    // POST /game/edit/{gameId}                   #
    // ############################################

    private static InputEditGameDto provideValidEditDtoWithoutScore() {
        // Scores are null → updatePoints should NOT be called
        return new InputEditGameDto(
                "France",
                "Germany",
                LocalDate.of(2026, 6, 10),
                LocalTime.of(20, 0),
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    private static InputEditGameDto provideValidEditDtoWithScore() {
        return new InputEditGameDto(
                "France",
                "Germany",
                LocalDate.of(2026, 6, 10),
                LocalTime.of(20, 0),
                null,
                null,
                null,
                null,
                2,
                1,
                null
        );
    }


    @Test
    void postEditFormValid_withoutScores() throws Exception {
        int gameId = 1;
        InputEditGameDto dto = provideValidEditDtoWithoutScore();

        Mockito.doNothing().when(gameService).updateGame(Mockito.any(), Mockito.eq(gameId));

        mockMvc.perform(post("/game/edit/{gameId}", gameId)
                        .flashAttr("inputEditGameDto", dto)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/game/allGames"));

        Mockito.verify(gameService, Mockito.never())
                .updatePoints(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(),
                        Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt());

        Mockito.verify(gameService, Mockito.times(1)).updateGame(dto, gameId);
    }

    @Test
    void postEditFormValid_withScores() throws Exception {
        int gameId = 1;
        InputEditGameDto dto = provideValidEditDtoWithScore();

        Mockito.doNothing().when(gameService).updateGame(Mockito.any(), Mockito.eq(gameId));
        Mockito.doNothing().when(gameService)
                .updatePoints(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(),
                        Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt());

        mockMvc.perform(post("/game/edit/{gameId}", gameId)
                        .flashAttr("inputEditGameDto", dto)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/game/allGames"))
                .andExpect(flash().attributeExists("successMessage"));

        Mockito.verify(gameService, Mockito.times(1))
                .updatePoints(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(),
                        Mockito.eq(dto.scoreA()), Mockito.eq(dto.scoreB()), Mockito.eq(gameId));

        Mockito.verify(gameService, Mockito.times(1)).updateGame(dto, gameId);
    }

    @Test
    void postEditFormInvalid() throws Exception {
        int gameId = 1;

        // Validator rejects dto
        Mockito.doAnswer(invocation -> {
            Errors errors = invocation.getArgument(1);
            errors.rejectValue("landA", "error.code", "Land A is required");
            errors.rejectValue("landB", "error.code", "Land B is required");
            return null;
        }).when(gameValidator).validate(Mockito.any(InputEditGameDto.class), Mockito.any(Errors.class));

        InputEditGameDto invalidDto = new InputEditGameDto("", "", null, null, null, null, null, null, null, null, null);

        mockMvc.perform(post("/game/edit/{gameId}", gameId)
                        .flashAttr("inputEditGameDto", invalidDto)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("gameEdit"))
                .andExpect(model().attributeExists("gameId"))
                .andExpect(model().attributeHasFieldErrors("inputEditGameDto", "landA"));

        Mockito.verify(gameService, Mockito.never()).updateGame(Mockito.any(), Mockito.anyInt());
        Mockito.verify(gameService, Mockito.never())
                .updatePoints(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(),
                        Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    void postEditFormInvalid_gameIdPreservedInModel() throws Exception {
        int gameId = 5;

        Mockito.doAnswer(invocation -> {
            Errors errors = invocation.getArgument(1);
            errors.rejectValue("landA", "error.code", "Required");
            return null;
        }).when(gameValidator).validate(Mockito.any(InputEditGameDto.class), Mockito.any(Errors.class));

        InputEditGameDto invalidDto = new InputEditGameDto("", "Germany", null, null, null, null, null, null, null, null, null);

        mockMvc.perform(post("/game/edit/{gameId}", gameId)
                        .flashAttr("inputEditGameDto", invalidDto)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("gameEdit"))
                .andExpect(model().attribute("gameId", gameId));
    }

    @Test
    void postEditFormValid_flashSuccessMessage() throws Exception {
        int gameId = 1;
        InputEditGameDto dto = provideValidEditDtoWithoutScore();

        Mockito.doNothing().when(gameService).updateGame(Mockito.any(), Mockito.eq(gameId));

        mockMvc.perform(post("/game/edit/{gameId}", gameId)
                        .flashAttr("inputEditGameDto", dto)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("successMessage"));
    }


}