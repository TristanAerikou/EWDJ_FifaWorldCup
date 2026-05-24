package lv.ewdj.fifaworldcup.dto;

public record InputRegistrationDto(
        String username,

        String password,
        String confirmPassword,

        String firstname,
        String lastname
) {

}
