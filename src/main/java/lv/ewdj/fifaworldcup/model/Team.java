package lv.ewdj.fifaworldcup.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "teams")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "name")
@ToString
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String name;

    @Column(unique = true)
    private String inviteCode;

//    private int points;

    public  Team(String name, String inviteCode) {
        this.name = name;
        this.inviteCode = inviteCode;
    }

    public  Team(int id, String name, String inviteCode) {
        this.id = id;
        this.name = name;
        this.inviteCode = inviteCode;
    }

//    public void setPoints(int pointsToAdd) {
//        this.points += pointsToAdd;
//    }
}
