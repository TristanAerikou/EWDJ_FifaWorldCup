package lv.ewdj.fifaworldcup.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(exclude = "id") //TODO CHANGE
@ToString(exclude = "id")
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
    @Getter(AccessLevel.NONE)
    private Long id;

    private String username;
    private String password;
    private Role role;

    private String lastname;
    private String firstname;


    public User(
            String username,
            String password,
            Role role,

            String lastname,
            String firstname
    ) {
        this.username = username;
        this.password = password;
        this.role = role;

        this.lastname = lastname;
        this.firstname = firstname;
    }
}
