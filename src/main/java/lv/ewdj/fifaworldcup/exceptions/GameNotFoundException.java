package lv.ewdj.fifaworldcup.exceptions;

public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(String message) {
        super(message);
    }
}
