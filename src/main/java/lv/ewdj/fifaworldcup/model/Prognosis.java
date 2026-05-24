package lv.ewdj.fifaworldcup.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "prognoses")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
@ToString
public class Prognosis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Getter(AccessLevel.NONE)
    @ToString.Exclude
    private Long id;

    @Column(nullable = false)
    int goalsTeamA;

    @Column(nullable = false)
    int goalsTeamB;

    @ManyToOne()
    Game game;

    @ManyToOne
    User user;

    public Prognosis(int goalsTeamA, int goalsTeamB, Game game, User user) {
        this.goalsTeamA = goalsTeamA;
        this.goalsTeamB = goalsTeamB;
        this.game = game;
        this.user = user;
    }

    public Prognosis(long id, int goalsTeamA, int goalsTeamB, Game game, User user) {
        this.id = id;
        this.goalsTeamA = goalsTeamA;
        this.goalsTeamB = goalsTeamB;
        this.game = game;
        this.user = user;
    }
}
