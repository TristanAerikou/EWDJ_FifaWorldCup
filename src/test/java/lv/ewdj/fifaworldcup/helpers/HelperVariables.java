package lv.ewdj.fifaworldcup.helpers;

import lv.ewdj.fifaworldcup.dto.OutputGameDto;
import lv.ewdj.fifaworldcup.model.Game;
import lv.ewdj.fifaworldcup.model.Prognosis;
import lv.ewdj.fifaworldcup.model.User;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;

public class HelperVariables {

    public static List<OutputGameDto> provideExpectedGamesAsDtos() {
        return provideGames().map(OutputGameDto::objToDto).toList();
    }

    public static Stream<Game> provideGames() {
        return Stream.of(
                new Game("Mechelen", "Brugge", LocalDate.of(2026, 5, 28), LocalTime.of(12, 30), "Brugge", "Jan Breydelstadion", 1000, 123),
                new Game("Geraardsbergen", "Mont Everest", LocalDate.of(2026, 5, 28), LocalTime.of(12, 30), "Brugge", "Mont Everest Peak", 1000, 123),
                new Game("Backyardigans", "Inazuma Eleven", LocalDate.of(2026, 5, 28), LocalTime.of(12, 30), "Brugge", "Backyard", 1000, 123),
                new Game("Anderlecht", "Standard", LocalDate.of(2026, 10, 15), LocalTime.of(14, 30), "Anderlecht", "Lotto Park", 1001, 250),
                new Game("Gent", "Genk", LocalDate.of(2026, 11, 22), LocalTime.of(18, 0), "Gent", "Ghelamco Arena", 4545, 556),
                new Game("Antwerp", "Beerschot", LocalDate.of(2026, 2, 10), LocalTime.of(20, 30), "Antwerp", "Bosuilstadion", 2121, 456),
                new Game("Union SG", "Cercle Brugge", LocalDate.of(2026, 3, 5), LocalTime.of(16, 0), "Union SG", "Marienstadion", 6666, 879),
                new Game("Charleroi", "Kortrijk", LocalDate.of(2026, 4, 12), LocalTime.of(18, 15), "Charleroi", "Stade du Pays de Charleroi", 8080, 1100),
                new Game("Leuven", "Sint-Truiden", LocalDate.of(2026, 6, 18), LocalTime.of(20, 0), "Leuven", "King Power at Den Dreef", 1212, 65),
                new Game("Westerlo", "Eupen", LocalDate.of(2026, 8, 24), LocalTime.of(15, 30), "Westerlo", "Jan Breydelstadion", 6464, 456),
                new Game("Cercle Brugge", "Anderlecht", LocalDate.of(2026, 9, 14), LocalTime.of(13, 30), "Cercle Brugge", "Jan Breydelstadion", 83, 4548),
                new Game("Standard", "Gent", LocalDate.of(2026, 12, 5), LocalTime.of(20, 45), "Standard", "Stade Maurice Dufrasne", 3737, 879)
        );
    }

    public static Game provideGame() {
        return new Game("Mechelen", "Brugge", LocalDate.of(2026, 5, 28), LocalTime.of(12, 30), "Brugge", "Jan Breydelstadion", 1, 255);
    }

    public static Game futureGame() {
        return new Game("Mechelen", "Brugge", LocalDate.of(2026, 6, 25), LocalTime.of(12, 30), "Brugge", "Jan Breydelstadion", 1, 255);
    }
    public static Game provideGameInThePast() {
        return new Game("Mechelen", "Brugge", LocalDate.of(2026, 2, 25), LocalTime.of(12, 30), "Brugge", "Jan Breydelstadion", 1, 255);
    }

    public static Prognosis providePrognosis() {
        User mockUser = Mockito.mock(User.class);
        return new Prognosis(1, 2, provideGame(), mockUser);
    }

}
