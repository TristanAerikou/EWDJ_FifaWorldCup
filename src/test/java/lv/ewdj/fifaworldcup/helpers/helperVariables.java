package lv.ewdj.fifaworldcup.helpers;

import lv.ewdj.fifaworldcup.dto.GameOutputDto;
import lv.ewdj.fifaworldcup.model.Game;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;

public class helperVariables {

    public static List<GameOutputDto> provideExpectedGames() {
        return Stream.of(
                new Game("Mechelen", "Brugge", LocalDate.of(2020, 5, 28), LocalTime.of(12, 30), "Brugge", "Jan Breydelstadion", 1),
                new Game("Anderlecht", "Standard", LocalDate.of(2024, 10, 15), LocalTime.of(14, 30), "Anderlecht", "Lotto Park", 0),
                new Game("Gent", "Genk", LocalDate.of(2024, 11, 22), LocalTime.of(18, 0), "Gent", "Ghelamco Arena", 45),
                new Game("Antwerp", "Beerschot", LocalDate.of(2025, 2, 10), LocalTime.of(20, 30), "Antwerp", "Bosuilstadion", 21),
                new Game("Union SG", "Cercle Brugge", LocalDate.of(2025, 3, 5), LocalTime.of(16, 0), "Union SG", "Marienstadion", 66),
                new Game("Charleroi", "Kortrijk", LocalDate.of(2025, 4, 12), LocalTime.of(18, 15), "Charleroi", "Stade du Pays de Charleroi", 80),
                new Game("Leuven", "Sint-Truiden", LocalDate.of(2025, 6, 18), LocalTime.of(20, 0), "Leuven", "King Power at Den Dreef", 12),
                new Game("Westerlo", "Eupen", LocalDate.of(2025, 8, 24), LocalTime.of(15, 30), "Westerlo", "Het Kuipje", 64),
                new Game("Cercle Brugge", "Anderlecht", LocalDate.of(2025, 9, 14), LocalTime.of(13, 30), "Cercle Brugge", "Jan Breydelstadion", 83),
                new Game("Standard", "Gent", LocalDate.of(2025, 12, 5), LocalTime.of(20, 45), "Standard", "Stade Maurice Dufrasne", 37)
        ).map(GameOutputDto::objToDto).toList();
    }
}
