package lv.ewdj.fifaworldcup.config;

import lombok.RequiredArgsConstructor;
import lv.ewdj.fifaworldcup.model.User;
import lv.ewdj.fifaworldcup.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("dev")
public class InitDataConfig implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        userRepository.save(new User("Keters", "Sandra"));
        userRepository.save(new User("Blondeel", "Tania"));
        userRepository.save(new User("Blondeel", "Jurgen"));
        userRepository.save(new User("Blondeels", "Ann"));
    }

}
