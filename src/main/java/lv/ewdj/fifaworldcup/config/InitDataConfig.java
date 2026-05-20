package lv.ewdj.fifaworldcup.config;

import lombok.RequiredArgsConstructor;
import lv.ewdj.fifaworldcup.model.Game;
import lv.ewdj.fifaworldcup.model.Role;
import lv.ewdj.fifaworldcup.model.User;
import lv.ewdj.fifaworldcup.repository.GameRepository;
import lv.ewdj.fifaworldcup.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
@Profile("dev")
public class InitDataConfig implements CommandLineRunner {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    private static final String BCRYPTED_PASSWORD =
            "$2a$12$XUVHZa3gBuw.mpetu.2dmunWbKYjfIChpk9ZXgxAnsXGP1lKYimgy";
    // aha - https://bcrypt-generator.com/

    private static final String ADMIN_PASSWORD =
            "$2a$12$0uJ9XULn3l8I2v8FEoKzzu7Py/X0f4R88xkVu7qoSrPKvRKodqLvu";
    // Meteor

    @Override
    public void run(String... args) {


        // USERS
        userRepository.save(new User("RockFromSpace", ADMIN_PASSWORD, Role.ADMIN, "Tristan", "Aerikou"));

        userRepository.save(new User("Mercy", BCRYPTED_PASSWORD, Role.USER, "Sans", "The Skeleton"));
        userRepository.save(new User("StrikerGoal99", BCRYPTED_PASSWORD, Role.USER, "Lucas", "Dubois"));
        userRepository.save(new User("OffsideTrap", BCRYPTED_PASSWORD, Role.USER, "Elena", "Russo"));
        userRepository.save(new User("TikiTakaMaster", BCRYPTED_PASSWORD, Role.USER, "Mateo", "Silva"));
        userRepository.save(new User("CleanSheetKing", BCRYPTED_PASSWORD, Role.USER, "Sophia", "Müller"));
        userRepository.save(new User("NutmegHero", BCRYPTED_PASSWORD, Role.USER, "Oliver", "Hansen"));
        userRepository.save(new User("CornerKickPro", BCRYPTED_PASSWORD, Role.USER, "Amara", "Diallo"));
        userRepository.save(new User("MidfieldMaestro", BCRYPTED_PASSWORD, Role.USER, "Liam", "O'Connor"));
        userRepository.save(new User("VarCheckNoGoal", BCRYPTED_PASSWORD, Role.USER, "Chloe", "Lefevre"));
        userRepository.save(new User("YellowCardWarning", BCRYPTED_PASSWORD, Role.USER, "Diego", "Fernandez"));
        userRepository.save(new User("HatTrickHero", BCRYPTED_PASSWORD, Role.USER, "Emma", "Smit"));
        userRepository.save(new User("CrossbarChallenger", BCRYPTED_PASSWORD, Role.USER, "Jonas", "Novak"));
        userRepository.save(new User("GafferTactics", BCRYPTED_PASSWORD, Role.USER, "Aria", "Patel"));
        userRepository.save(new User("PitchInvader", BCRYPTED_PASSWORD, Role.USER, "Ethan", "Wright"));
        userRepository.save(new User("UltrasZone", BCRYPTED_PASSWORD, Role.USER, "Maja", "Wojcik"));
        userRepository.save(new User("PanenkaPenalty", BCRYPTED_PASSWORD, Role.USER, "Kaito", "Tanaka"));
        userRepository.save(new User(" FergieTime90", BCRYPTED_PASSWORD, Role.USER, "Isabella", "Costa"));
        userRepository.save(new User("BoxToBoxDynamo", BCRYPTED_PASSWORD, Role.USER, "Noah", "Andersson"));
        userRepository.save(new User("FalseNineExpert", BCRYPTED_PASSWORD, Role.USER, "Zara", "Haddad"));
        userRepository.save(new User("ZonalMarking", BCRYPTED_PASSWORD, Role.USER, "Finn", "Gallagher"));
        userRepository.save(new User("StoppageTimeWinner", BCRYPTED_PASSWORD, Role.USER, "Nina", "Petrov"));

        // GAMES
        gameRepository.save(new Game("Mechelen", "Brugge", LocalDate.of(2020, 5, 28), LocalTime.of(12, 30), "Brugge", "Jan Breydelstadion", 1));
        gameRepository.save(new Game("Anderlecht", "Standard", LocalDate.of(2024, 10, 15), LocalTime.of(14, 30), "Anderlecht", "Lotto Park", 0));
        gameRepository.save(new Game("Gent", "Genk", LocalDate.of(2024, 11, 22), LocalTime.of(18, 0), "Gent", "Ghelamco Arena", 45));
        gameRepository.save(new Game("Antwerp", "Beerschot", LocalDate.of(2025, 2, 10), LocalTime.of(20, 30), "Antwerp", "Bosuilstadion", 21));
        gameRepository.save(new Game("Union SG", "Cercle Brugge", LocalDate.of(2025, 3, 5), LocalTime.of(16, 0), "Union SG", "Marienstadion", 66));
        gameRepository.save(new Game("Charleroi", "Kortrijk", LocalDate.of(2025, 4, 12), LocalTime.of(18, 15), "Charleroi", "Stade du Pays de Charleroi", 80));
        gameRepository.save(new Game("Leuven", "Sint-Truiden", LocalDate.of(2025, 6, 18), LocalTime.of(20, 0), "Leuven", "King Power at Den Dreef", 12));
        gameRepository.save(new Game("Westerlo", "Eupen", LocalDate.of(2025, 8, 24), LocalTime.of(15, 30), "Westerlo", "Het Kuipje", 64));
        gameRepository.save(new Game("Cercle Brugge", "Anderlecht", LocalDate.of(2025, 9, 14), LocalTime.of(13, 30), "Cercle Brugge", "Jan Breydelstadion", 83));
        gameRepository.save(new Game("Standard", "Gent", LocalDate.of(2025, 12, 5), LocalTime.of(20, 45), "Standard", "Stade Maurice Dufrasne", 37));
    }

}
