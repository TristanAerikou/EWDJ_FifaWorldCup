package lv.ewdj.fifaworldcup.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import lv.ewdj.fifaworldcup.util.LocalDateDeserializer;
import lv.ewdj.fifaworldcup.util.LocalDateSerializer;
import lv.ewdj.fifaworldcup.util.LocalTimeDeserializer;
import lv.ewdj.fifaworldcup.util.LocalTimeSerializer;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "games")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(exclude = "id") //TODO CHANGE
@ToString(exclude = "id")
@Getter()
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Getter(AccessLevel.NONE)
    @Setter
    @JsonProperty("game_id")
    private int id;

    // De hele applicatie werd in het engels geschreven... buiten deze attributen. Oops, was vergeten dat 'land' geen Engels was.
    private String landA;
    private String landB;

    private int scoreA;
    private int scoreB;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate dateOfGame;
    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime timeOfGame;

    private String location;
    private String stadium;
    private int stadiumCode;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "game")
    @JsonIgnore
    private List<Prognosis> prognoses;

    public Game(String landA, String landB, LocalDate dateOfGame, LocalTime timeOfGame, String location, String stadium, int stadiumCode) {
        this.landA = landA;
        this.landB = landB;
        this.dateOfGame = dateOfGame;
        this.timeOfGame = timeOfGame;
        this.location = location;
        this.stadium = stadium;
        this.stadiumCode = stadiumCode;

        this.scoreA = -1;
        this.scoreB = -1;
    }

    public Game(String landA, String landB, LocalDate dateOfGame, LocalTime timeOfGame, String location, String stadium, int stadiumCode, int scoreA, int scoreB) {
        this.landA = landA;
        this.landB = landB;
        this.dateOfGame = dateOfGame;
        this.timeOfGame = timeOfGame;
        this.location = location;
        this.stadium = stadium;
        this.stadiumCode = stadiumCode;

        this.scoreA = scoreA;
        this.scoreB = scoreB;
    }

}
