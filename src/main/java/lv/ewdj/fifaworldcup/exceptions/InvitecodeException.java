package lv.ewdj.fifaworldcup.exceptions;

import lombok.Getter;

public class InvitecodeException extends RuntimeException {

    @Getter
    private final String inviteCode;

    public InvitecodeException(String inviteCode) {
        super("Something was wrong with the invite code %s".formatted(inviteCode));
        this.inviteCode = inviteCode;
    }


    public InvitecodeException(String message, String inviteCode) {
        super(message);
        this.inviteCode = inviteCode;
    }
}
