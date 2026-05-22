package lv.ewdj.fifaworldcup.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "games")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(exclude = "id") //TODO CHANGE
@ToString(exclude = "id")
@Getter()
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter(AccessLevel.NONE)
    private int id;

    // De hele applicatie werd in het engels geschreven... buiten deze attributen. Oops, was vergeten dat 'land' geen Engels was.
    private String landA;
    private String landB;

    private LocalDate dateOfGame;
    private LocalTime timeOfGame;

    private String location;
    private String stadium;
    private int stadiumCode;

    public Game(String landA, String landB, LocalDate dateOfGame, LocalTime timeOfGame, String location, String stadium, int stadiumCode) {
        this.landA = landA;
        this.landB = landB;
        this.dateOfGame = dateOfGame;
        this.timeOfGame = timeOfGame;
        this.location = location;
        this.stadium = stadium;
        this.stadiumCode = stadiumCode;
    }

}
