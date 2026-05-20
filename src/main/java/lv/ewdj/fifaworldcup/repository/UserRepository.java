package lv.ewdj.fifaworldcup.repository;

import lv.ewdj.fifaworldcup.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByLastname(String name);
    List<User> findByFirstname(String firstname);

    User findByFirstnameAndLastname(String firstname, String lastname);

    Optional<User> findByUsername(String username);

//    @Query( """
//            SELECT u
//            FROM User u
//            WHERE u.lastname LIKE CONCAT(:str, '%')
//            """)
//    List<User> findByLastnameStartingWith(@Param("str") String str);
//
//    List<User> findByNameStartingWith2(@Param("username") String username);

}
