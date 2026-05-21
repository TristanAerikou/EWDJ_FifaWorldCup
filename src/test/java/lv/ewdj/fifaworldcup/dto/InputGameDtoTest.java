package lv.ewdj.fifaworldcup.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lv.ewdj.fifaworldcup.repository.GameRepository;
import lv.ewdj.fifaworldcup.validator.GameValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Waarom @AutoWire op de validators?
 * De validators gebruiken Dependency Injection (beans). Dit zorgt ervoor dat de validators énkel juist kunnen werken
 * binnen de Springboot omgeving; Daarom dat @SpringBootTest ook gebruikt wordt (namelijk ene SpringBoot omgeving
 * opspinnen) zodat de SpringBoot lifecycle kan gebruikt worden binnen de tests.
 *
 * Dit is de reden dat we niet een "normale"/default validator kunnen gebruiken. De default validator wordt niet binnen
 * de SpringBoot lifecycle gehandled, hetgeen de huidige validators wel nodig hebben.
 */
@SpringBootTest(properties = {
        "rule.startdate=2026-02-10",
        "rule.enddate=2026-07-02"
})
class InputGameDtoTest {

    @Autowired
    private Validator validator;

    @Autowired
    private GameValidator gameValidator;

    @MockitoBean
    private GameRepository gameRepository;

    private static final String DEFAULT_LANDA = "France";
    private static final String DEFAULT_LANDB = "Italy";
    private static final LocalDate DEFAULT_DATEOFGAME = LocalDate.of(2026, 5, 23);
    private static final LocalTime DEFAULT_TIMEOFGAME = LocalTime.of(12, 0);

    // ######################
    // Basic Annotations    #
    // ######################
    public static Stream<Arguments> ProvideValidMinimalGame() {
        return Stream.of(
                Arguments.of("abcd", "dcba", LocalDate.of(2026, 5, 25), LocalTime.of(12, 0)),
                Arguments.of("abcdabc", "dcbacba", LocalDate.of(2026, 7, 2), LocalTime.of(18, 59)),
                Arguments.of("aaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "dcbabbbbbbbbbbbbbbbbbbbbbbcba", LocalDate.of(2026, 7, 2), LocalTime.of(18, 59)),
                Arguments.of("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "dcbabbbbbbbbbbbbbbbbbbbbbbbcba", LocalDate.of(2026, 2, 10), LocalTime.of(18, 59))
        );
    }

    @ParameterizedTest
    @MethodSource("ProvideValidMinimalGame")
    void validMinimalGame(String landA, String landB, LocalDate dateOfGame, LocalTime timeOfGame) {

        InputGameDto validGame = new InputGameDto(
                landA, landB,
                dateOfGame, timeOfGame,
                null, null,
                null, null
        );
        Set<ConstraintViolation<InputGameDto>> violations = validator.validate(validGame);

        assertThat(violations).isEmpty();
    }

    public static Stream<Arguments> ProvideInvalidMinimalGame() {
        return Stream.of(
                Arguments.of("", "", null, null, List.of("landA", "landB", "dateOfGame", "timeOfGame")),
                Arguments.of("   ", "   ", LocalDate.of(2026, 5, 25), LocalTime.of(12, 0), List.of("landA", "landB")),
                Arguments.of("\t   ", "\t   ", LocalDate.of(2026, 5, 25), LocalTime.of(12, 0), List.of("landA", "landB")),
                Arguments.of("\t", "\t", LocalDate.of(2026, 5, 25), LocalTime.of(12, 0), List.of("landA", "landB")),
                Arguments.of("aaa", "aaa", null, null, List.of("landA", "landB", "dateOfGame", "timeOfGame")),
                Arguments.of("aaaa", "zzzz", null, null, List.of("dateOfGame", "timeOfGame")),
                Arguments.of("aaaa", "zzzz", LocalDate.of(2025, 5, 25), null, List.of("dateOfGame", "timeOfGame")),
                Arguments.of("aaaa", "zzzz", LocalDate.of(2027, 5, 25), LocalTime.of(12, 0), List.of("dateOfGame")),
                Arguments.of(null, null, LocalDate.of(2026, 5, 25), LocalTime.of(12, 0), List.of("landA", "landB"))
        );
    }

    @ParameterizedTest
    @MethodSource("ProvideInvalidMinimalGame")
    void invalidMinimalGame(String landA, String landB, LocalDate dateOfGame, LocalTime timeOfGame, List<String> expected) {
        InputGameDto invalidGame = new InputGameDto(
                landA, landB,
                dateOfGame, timeOfGame,
                null, null,
                null, null
        );

        Set<ConstraintViolation<InputGameDto>> violations = validator.validate(invalidGame);
        assertThat(violations)
                .isNotEmpty()
                .allMatch(violation -> expected.contains(violation.getPropertyPath().toString()));
    }

    // ########################
    // StadiumCode & checksum #
    // ########################

    @ParameterizedTest
    @CsvSource({"9797, 0", "9700, 0", "9701, 1", "4587, 28", "1000, 30", "1001, 31", "9998, 7", "9999, 8"})
    void validStadiumCodeAndChecksum(int stadiumCode, int checksum) {
        InputGameDto validGame = new InputGameDto(
                DEFAULT_LANDA, DEFAULT_LANDA, DEFAULT_DATEOFGAME, DEFAULT_TIMEOFGAME,
                null, null,
                stadiumCode, checksum
        );
        Set<ConstraintViolation<InputGameDto>> violations = validator.validate(validGame);

        assertThat(violations).isEmpty();
    }

    public static Stream<Arguments> ProvideInvalidStadiumCodeAndChecksum() {
        return Stream.of(
                Arguments.of(5, null, List.of("stadiumCode", "checksum")),
                Arguments.of(5555, null, List.of("checksum")),
                Arguments.of(97, 0, List.of("stadiumCode")),
                Arguments.of(979, 55, List.of("stadiumCode", "checksum")),
                Arguments.of(229, 55, List.of("stadiumCode", "checksum")),
                Arguments.of(749, 55, List.of("stadiumCode", "checksum")),
                Arguments.of(100, 55, List.of("stadiumCode", "checksum")),
                Arguments.of(2525, 0, List.of("checksum")),
                Arguments.of(9797, 1, List.of("checksum")),
                Arguments.of(9797, 2, List.of("checksum")),
                Arguments.of(9797, 97, List.of("checksum")),
                Arguments.of(9797, 96, List.of("checksum"))
        );
    }

    @ParameterizedTest
    @MethodSource("ProvideInvalidStadiumCodeAndChecksum")
    void invalidStadiumCodeAndChecksum(Integer stadiumCode, Integer checksum, List<String> expected) {
        InputGameDto invalidGame = new InputGameDto(
                DEFAULT_LANDA, DEFAULT_LANDA, DEFAULT_DATEOFGAME, DEFAULT_TIMEOFGAME,
                null, null,
                stadiumCode, checksum
        );
        Set<ConstraintViolation<InputGameDto>> violations = validator.validate(invalidGame);

        assertThat(violations)
                .isNotEmpty()
                .extracting(violation -> violation.getPropertyPath().toString())
                .containsAll(expected);
    }

    // ######################
    // Land A & Land B     #
    // ######################

    @Test
    void invalid_landaEqualLandb() {

        InputGameDto invalidGame = new InputGameDto(
                "abcdef", "abcdef",
                DEFAULT_DATEOFGAME, DEFAULT_TIMEOFGAME,
                null, null,
                null, null
        );

        Errors errors = new BeanPropertyBindingResult(invalidGame, "invalidGame");

        gameValidator.validate(invalidGame, errors);

        assertThat(errors.hasErrors()).isTrue();
        assertThat(errors.getFieldError("landB")).isNotNull();

    }

    //TODO test datum & locatie


}