package lv.ewdj.fifaworldcup.client;

import lv.ewdj.fifaworldcup.model.Game;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;


public class RestClientDemo {

    private final String SERVER_URI = "http://localhost:8080/rest";

    private final WebClient webClient = WebClient.builder()
            .baseUrl(SERVER_URI)
            .build();


    public RestClientDemo() throws Exception {
        System.out.println("\n------- GET ALL -------");
        getAllGames()
                .doOnNext(System.out::println)
                .blockLast();

        System.out.println("\n------- GET THE TWO GAMES WITH DATE 28-05-2026 -------");
        getGamesByDate("28-05-2026")
                .doOnNext(System.out::println)
                .blockLast();

        System.out.println("\n------- GET ALL STADIUMS -------");
        getAllStadiums()
                .doOnNext(System.out::println)
                .blockLast();
    }

    private Flux<Game> getAllGames() {
        return webClient.get()
                .uri("/games")
                .retrieve()
                .bodyToFlux(Game.class);
    }

    private Flux<Game> getGamesByDate(String date) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/games/{date}").build(date))
                .retrieve()
                .bodyToFlux(Game.class);
    }

    private Flux<String> getAllStadiums() {
        return webClient.get()
                .uri("/stadiums")
                .retrieve()
                .bodyToFlux(String.class);
    }

}
