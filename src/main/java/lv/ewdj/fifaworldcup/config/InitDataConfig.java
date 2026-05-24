package lv.ewdj.fifaworldcup.config;

import lombok.RequiredArgsConstructor;
import lv.ewdj.fifaworldcup.model.*;
import lv.ewdj.fifaworldcup.repository.GameRepository;
import lv.ewdj.fifaworldcup.repository.PrognosisRepository;
import lv.ewdj.fifaworldcup.repository.TeamRepository;
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
    private final TeamRepository teamRepository;

    private static final String BCRYPTED_PASSWORD =
            "$2a$12$XUVHZa3gBuw.mpetu.2dmunWbKYjfIChpk9ZXgxAnsXGP1lKYimgy";
    // aha - https://bcrypt-generator.com/

    private static final String ADMIN_PASSWORD =
            "$2a$12$0uJ9XULn3l8I2v8FEoKzzu7Py/X0f4R88xkVu7qoSrPKvRKodqLvu";
    private final PrognosisRepository prognosisRepository;
    // Meteor

    @Override
    public void run(String... args) {

        // TEAMS
        Team team1 = new Team("Team1", "abcdefg1");
        Team team2 = new Team("Team2", "abcdefg2");

        teamRepository.save(team1);
        teamRepository.save(team2);


        // USERS
        User user1 = new User("RockFromSpace", ADMIN_PASSWORD, Role.ADMIN, "Tristan", "Aerikou");

        User user2 = new User("Mercy", BCRYPTED_PASSWORD, Role.USER, "Sans", "The Skeleton", team1, null);
        User user3 = new User("Spaghetti", BCRYPTED_PASSWORD, Role.USER, "Papyrus", "The Skeleton", team1, team1);

        User user4 = new User("flowie", BCRYPTED_PASSWORD, Role.USER, "flowie", "The Flower", team2, team2);


        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);

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
        Game game1 = new Game("Sixth Street", "Eighth Street", LocalDate.of(2026, 3, 25), LocalTime.of(12, 30), "The Plaza", "The Stadium", 9797);
        Game game2 = new Game("Ravensburger", "Google", LocalDate.of(2026, 6, 25), LocalTime.of(12, 30), "Disney Land", "The Palace", 9700);

        gameRepository.save(game1);
        gameRepository.save(game2);

        gameRepository.save(new Game("Mechelen", "Brugge", LocalDate.of(2026, 5, 28), LocalTime.of(12, 30), "Brugge", "Jan Breydelstadion", 1111));
        gameRepository.save(new Game("Anderlecht", "Standard", LocalDate.of(2026, 6, 15), LocalTime.of(14, 30), "Anderlecht", "Lotto Park", 1234));
        gameRepository.save(new Game("Gent", "Genk", LocalDate.of(2026, 2, 22), LocalTime.of(18, 0), "Gent", "Ghelamco Arena", 4545));
        gameRepository.save(new Game("Antwerp", "Beerschot", LocalDate.of(2026, 2, 10), LocalTime.of(20, 30), "Antwerp", "Bosuilstadion", 2121));
        gameRepository.save(new Game("Union SG", "Cercle Brugge", LocalDate.of(2026, 3, 5), LocalTime.of(16, 0), "Union SG", "Marienstadion", 6666));
        gameRepository.save(new Game("Charleroi", "Kortrijk", LocalDate.of(2026, 4, 12), LocalTime.of(18, 15), "Charleroi", "Stade du Pays de Charleroi", 8080));
        gameRepository.save(new Game("Leuven", "Sint-Truiden", LocalDate.of(2026, 6, 18), LocalTime.of(20, 0), "Leuven", "King Power at Den Dreef", 1212));
        gameRepository.save(new Game("Westerlo", "Eupen", LocalDate.of(2026, 7, 1), LocalTime.of(15, 30), "Westerlo", "Het Kuipje", 6464));
        gameRepository.save(new Game("Cercle Brugge", "Anderlecht", LocalDate.of(2026, 5, 14), LocalTime.of(13, 30), "Cercle Brugge", "Jan Breydelstadion", 8383));
        gameRepository.save(new Game("Standard", "Gent", LocalDate.of(2026, 4, 5), LocalTime.of(20, 45), "Standard", "Stade Maurice Dufrasne", 3737));

        // Prognoses
        // al voorbij
        prognosisRepository.save(new Prognosis(2, 3, game1, user1));
        prognosisRepository.save(new Prognosis(1, 1, game1, user2));

        // nog niet voorbij
        prognosisRepository.save(new Prognosis(5, 1, game2, user2));


    }

}
