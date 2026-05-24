package lv.ewdj.fifaworldcup.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class InputRegistrationDtoTest {

    private Validator validator;

    public static Stream<Arguments> ProvideValidRegistrations() {
        return Stream.of(
                Arguments.of(new InputRegistrationDto(
                        "MyUsername",
                        "mypassword",
                        "mypassword",
                        "MyName",
                        "NotYourName"
                ))
        );
    }

    public static Stream<Arguments> ProvideInvalidRegistrations() {
        return Stream.of(
                Arguments.of(new InputRegistrationDto(
                                "my",
                                "myPassword",
                                "myPassword",
                                "MyName",
                                "NotYourName"
                        ), List.of("username")
                ),
                Arguments.of(new InputRegistrationDto(
                                "myu",
                                "myPassword",
                                "myPassword",
                                "MyName",
                                "NotYourName"
                        ), List.of("username")
                ),
                Arguments.of(new InputRegistrationDto(
                                "MyUsernameMyUsernameMyUsernameMyUsernameMyUsernameMyUsername",
                                "myPassword",
                                "myPassword",
                                "MyName",
                                "NotYourName"
                        ), List.of("username")
                ),
                Arguments.of(new InputRegistrationDto(
                                "MyUsern4me",
                                "myPassword",
                                "myPassword",
                                "MyName",
                                "NotYourName"
                        ), List.of("username")
                ),
                Arguments.of(new InputRegistrationDto(
                                "MyUsername@",
                                "myPassword",
                                "myPassword",
                                "MyName",
                                "NotYourName"
                        ), List.of("username")
                ),
                Arguments.of(new InputRegistrationDto(
                                "MyUsername",
                                "myPassword",
                                "myPassword",
                                "MyName{<",
                                "NotYourN45ame"
                        ), List.of("firstname", "lastname")
                ),
                Arguments.of(new InputRegistrationDto(
                                "MyUsername",
                                "ayo",
                                "ayo",
                                "myname",
                                "NotYourName"
                        ), List.of("password")
                ),
                Arguments.of(new InputRegistrationDto(
                                "MyUsername",
                                "mypasswordmypasswordmypasswordmypassword",
                                "mypasswordmypasswordmypasswordmypassword",
                                "myname",
                                "NotYourName"
                        ), List.of("password")
                ),
                Arguments.of(new InputRegistrationDto(
                                "MyUsername",
                                "",
                                "",
                                "myname",
                                "NotYourName"
                        ), List.of("password", "confirmPassword")
                ),
                Arguments.of(new InputRegistrationDto(
                                "MyUsername",
                                "RightPass",
                                "WrongPass",
                                "myname",
                                "NotYourName"
                        ), List.of("password", "confirmPassword")
                )
        );
    }

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @ParameterizedTest
    @MethodSource("ProvideValidRegistrations")
    void validRegistration(InputRegistrationDto dto) {
        Set<ConstraintViolation<InputRegistrationDto>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("ProvideInvalidRegistrations")
    void invalidRegistration(InputRegistrationDto dto, List<String> errors) {
        Set<ConstraintViolation<InputRegistrationDto>> violations = validator.validate(dto);
        assertThat(violations)
                .isNotEmpty()
                .allMatch(violation -> errors.contains(violation.getPropertyPath().toString()));
        ;
    }

}