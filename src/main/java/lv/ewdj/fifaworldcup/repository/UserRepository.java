package lv.ewdj.fifaworldcup.repository;

import lv.ewdj.fifaworldcup.model.Team;
import lv.ewdj.fifaworldcup.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    List<User> findUsersByTeamName(String teamName);

    Optional<User> findUserByOwningTeamName(String owningTeamName);

    List<User> findAllByTeam(Team team);

//    @Query( """
//            SELECT u
//            FROM User u
//            WHERE u.lastname LIKE CONCAT(:str, '%')
//            """)
//    List<User> findByLastnameStartingWith(@Param("str") String str);
//
//    List<User> findByNameStartingWith2(@Param("username") String username);

}
