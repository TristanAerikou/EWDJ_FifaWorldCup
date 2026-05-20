package lv.ewdj.fifaworldcup.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(exclude = "id") //TODO CHANGE
@ToString(exclude = "id")
@Getter()
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter(AccessLevel.NONE)
    private int id;

    private String landA;
    private String landB;

    private LocalDate dateOfGame;
    private LocalTime timeOfGame;

    private String Location;
    private String stadium;
    private int stadiumCode;

    public Game(String landA, String landB, LocalDate dateOfGame, LocalTime timeOfGame, String location, String stadium, int stadiumCode) {
        this.landA = landA;
        this.landB = landB;
        this.dateOfGame = dateOfGame;
        this.timeOfGame = timeOfGame;
        this.Location = location;
        this.stadium = stadium;
        this.stadiumCode = stadiumCode;
    }

}
