package lv.ewdj.fifaworldcup.repository;

import lv.ewdj.fifaworldcup.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByLastname(String name);
    List<User> findByFirstname(String firstname);

}
