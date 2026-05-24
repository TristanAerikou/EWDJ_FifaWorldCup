package lv.ewdj.fifaworldcup.exceptions;

public class UserExistsException extends RuntimeException {
    public UserExistsException() {
        super("This user already exists");
    }
}
