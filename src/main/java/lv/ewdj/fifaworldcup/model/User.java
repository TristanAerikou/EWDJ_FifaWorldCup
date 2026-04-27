package lv.ewdj.fifaworldcup.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(exclude= "id") //TODO CHANGE
@ToString(exclude = "id")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter(AccessLevel.NONE)
    private Long id;

    private String lastname;
    private String firstname;

    public User(String lastname, String firstname) {
        this.lastname = lastname;
        this.firstname = firstname;
    }
}
