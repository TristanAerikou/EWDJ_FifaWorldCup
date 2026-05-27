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
            "$2a$12$y24ppC/byYCKZPQUyWfySeT4jKUbcoCUZdavLWsZZVoaTuDVMIsV.";
    // fifa - https://bcrypt-generator.com/

    private static final String ADMIN_PASSWORD =
            "$2a$12$0uJ9XULn3l8I2v8FEoKzzu7Py/X0f4R88xkVu7qoSrPKvRKodqLvu";
    private final PrognosisRepository prognosisRepository;
    // Meteor

    @Override
    public void run(String... args) {

        // TEAMS
        Team team1 = new Team("Team1", "abcdefg1");
        Team team2 = new Team("Team2", "abcdefg2");

        team1 = teamRepository.save(team1);
        team2 = teamRepository.save(team2);

        Team team3 = new Team("team3", "abcdefg3");
        Team team4 = new Team("team4", "abcdefg4");
        Team team5 = new Team("team5", "abcdefg5");
        Team team6 = new Team("team6", "abcdefg6");
        Team team7 = new Team("team7", "abcdefg7");
        Team team8 = new Team("team8", "abcdefg8");
        Team team9 = new Team("team9", "abcdefg9");
        Team tea10 = new Team("tea10", "abcdef10");
        Team tea11 = new Team("tea11", "abcdef11");
        Team tea12 = new Team("tea12", "abcdef12");
        Team tea13 = new Team("tea13", "abcdef13");
        Team tea14 = new Team("tea14", "abcdef14");
        Team tea15 = new Team("tea15", "abcdef15");

        team3 = teamRepository.save(team3);
        team4 = teamRepository.save(team4);
        team5 = teamRepository.save(team5);
        team6 = teamRepository.save(team6);
        team7 = teamRepository.save(team7);
        team8 = teamRepository.save(team8);
        team9 = teamRepository.save(team9);
        tea10 = teamRepository.save(tea10);
        tea11 = teamRepository.save(tea11);
        tea12 = teamRepository.save(tea12);
        tea13 = teamRepository.save(tea13);
        tea14 = teamRepository.save(tea14);
        tea15 = teamRepository.save(tea15);


        // USERS
        User user1 = new User("RockFromSpace", ADMIN_PASSWORD, Role.ADMIN, "Tristan", "Aerikou");

        User user2 = new User("Mercy", BCRYPTED_PASSWORD, Role.USER, "Sans", "The Skeleton", team1, null);
        User user3 = new User("Spaghetti", BCRYPTED_PASSWORD, Role.USER, "Papyrus", "The Skeleton", team1, team1);

        User user4 = new User("flowie", BCRYPTED_PASSWORD, Role.USER, "flowie", "The Flower", team2, team2);


        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);
        user3 = userRepository.save(user3);
        user4 = userRepository.save(user4);

        userRepository.save(new User("StrikerGoal99", BCRYPTED_PASSWORD, Role.USER, "Lucas", "Dubois", team3, null));
        userRepository.save(new User("OffsideTrap", BCRYPTED_PASSWORD, Role.USER, "Elena", "Russo", team4, null));
        userRepository.save(new User("TikiTakaMaster", BCRYPTED_PASSWORD, Role.USER, "Mateo", "Silva", team5, null));
        userRepository.save(new User("CleanSheetKing", BCRYPTED_PASSWORD, Role.USER, "Sophia", "Müller", team6, null));
        userRepository.save(new User("NutmegHero", BCRYPTED_PASSWORD, Role.USER, "Oliver", "Hansen", team7, null));
        userRepository.save(new User("CornerKickPro", BCRYPTED_PASSWORD, Role.USER, "Amara", "Diallo", team8, null));
        userRepository.save(new User("MidfieldMaestro", BCRYPTED_PASSWORD, Role.USER, "Liam", "O'Connor", team9, null));
        userRepository.save(new User("VarCheckNoGoal", BCRYPTED_PASSWORD, Role.USER, "Chloe", "Lefevre", tea10, null));
        userRepository.save(new User("YellowCardWarning", BCRYPTED_PASSWORD, Role.USER, "Diego", "Fernandez", tea11, null));
        userRepository.save(new User("HatTrickHero", BCRYPTED_PASSWORD, Role.USER, "Emma", "Smit", tea12, null));
        userRepository.save(new User("CrossbarChallenger", BCRYPTED_PASSWORD, Role.USER, "Jonas", "Novak", tea13, null));
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
        Game game1 = new Game("Sixth Street", "Eighth Street", LocalDate.of(2026, 3, 25), LocalTime.of(12, 30), "The Plaza", "The Stadium", 9797, 250);
        Game game2 = new Game("Ravensburger", "Google", LocalDate.of(2026, 6, 25), LocalTime.of(12, 30), "Disney Land", "The Palace", 9700, 350);

        game1 = gameRepository.save(game1);
        game2 = gameRepository.save(game2);

        gameRepository.save(new Game("Mechelen", "Brugge", LocalDate.of(2026, 5, 28), LocalTime.of(12, 30), "Brugge", "Jan Breydelstadion", 1111, 55));
        gameRepository.save(new Game("Oddysee", "Hogeschool Gent", LocalDate.of(2026, 5, 28), LocalTime.of(12, 30), "Brugge", "Jan Breydelstadion", 1111, 1010));
        gameRepository.save(new Game("Anderlecht", "Standard", LocalDate.of(2026, 6, 15), LocalTime.of(14, 30), "Anderlecht", "Lotto Park", 1234, 750));
        gameRepository.save(new Game("Gent", "Genk", LocalDate.of(2026, 2, 22), LocalTime.of(18, 0), "Gent", "Ghelamco Arena", 4545, 378));
        gameRepository.save(new Game("Antwerp", "Beerschot", LocalDate.of(2026, 2, 10), LocalTime.of(20, 30), "Antwerp", "Bosuilstadion", 2121, 432));
        gameRepository.save(new Game("Union SG", "Cercle Brugge", LocalDate.of(2026, 3, 5), LocalTime.of(16, 0), "Union SG", "Marienstadion", 6666, 658));
        gameRepository.save(new Game("Charleroi", "Kortrijk", LocalDate.of(2026, 4, 12), LocalTime.of(18, 15), "Charleroi", "Stade du Pays de Charleroi", 8080, 213));
        gameRepository.save(new Game("Leuven", "Sint-Truiden", LocalDate.of(2026, 6, 18), LocalTime.of(20, 0), "Leuven", "King Power at Den Dreef", 1212, 989));
        gameRepository.save(new Game("Westerlo", "Eupen", LocalDate.of(2026, 7, 1), LocalTime.of(15, 30), "Westerlo", "Jan Breydelstadion", 6464, 65));
        gameRepository.save(new Game("Cercle Brugge", "Anderlecht", LocalDate.of(2026, 5, 14), LocalTime.of(13, 30), "Cercle Brugge", "Jan Breydelstadion", 8383, 111));
        gameRepository.save(new Game("Standard", "Gent", LocalDate.of(2026, 4, 5), LocalTime.of(20, 45), "Standard", "Stade Maurice Dufrasne", 3737, 97));

        // Prognoses
        // al voorbij
        prognosisRepository.save(new Prognosis(2, 3, game1, user1));
        prognosisRepository.save(new Prognosis(1, 1, game1, user2));

        // nog niet voorbij
        prognosisRepository.save(new Prognosis(5, 1, game2, user2));


    }

}
