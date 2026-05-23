package lv.ewdj.fifaworldcup.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(exclude = "id") //TODO CHANGE
@ToString
//@NamedQueries({
//        @NamedQuery(name="User.findByNameStartingWith2",
//                query = """
//       		SELECT u
//       		FROM User u
//       		WHERE u.lastname LIKE CONCAT(:username,'%')
//       		""")
//})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Getter(AccessLevel.NONE)
    @ToString.Exclude
    private Long id;

    @Column(unique = true, nullable = false) //TODO wat als iemand een bestaande username wilt
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private String lastname;
    @Column(nullable = false)
    private String firstname;

    @ManyToOne
    @ToString.Exclude
    @Setter
    private Team team;

    @OneToOne
    @ToString.Exclude
    @Setter
    private Team owningTeam;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Prognosis> prognoses;

    @Setter(AccessLevel.NONE)
    int points;

    public User(
            String username,
            String password,
            Role role,

            String firstname,
            String lastname
    ) {
        this.username = username;
        this.password = password;
        this.role = role;

        this.lastname = lastname;
        this.firstname = firstname;

        this.team = null;
        this.owningTeam = null;
    }

    public User(
            String username,
            String password,
            Role role,

            String firstname,
            String lastname,

            Team team,
            Team owningTeam
    ) {
        this.username = username;
        this.password = password;
        this.role = role;

        this.lastname = lastname;
        this.firstname = firstname;

        this.team = team;
        this.owningTeam = owningTeam;
    }

    public User(long id) {
        this.id = id;
    }

    public void addPoints(int pointsToAdd) {
        this.points += pointsToAdd;
    }
}
