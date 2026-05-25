package lv.ewdj.fifaworldcup.dto;

public record ErrorResponse (
        int status,
        String message,
        String timestamp
) {

}
