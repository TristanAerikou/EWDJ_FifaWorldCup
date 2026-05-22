package lv.ewdj.fifaworldcup.exceptions;

import lombok.Getter;

public class UserNotFoundException extends RuntimeException {

    @Getter
    private final String username;

    public UserNotFoundException(String username) {
        super("User with name %s not found".formatted(username));
        this.username = username;
    }

    public UserNotFoundException(String message, String username) {
        super(message);
        this.username = username;
    }
}
