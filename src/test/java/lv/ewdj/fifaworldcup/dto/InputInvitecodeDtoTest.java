package lv.ewdj.fifaworldcup.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class InputInvitecodeDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @ParameterizedTest
    @ValueSource(strings = {"ABCDEFGH", "heyoheyo", "abcqdqsi", "cookieee"})
    void validateInvitecodeDto(String code) {
        InputInvitecodeDto dto = new InputInvitecodeDto(code);

        Set<ConstraintViolation<InputInvitecodeDto>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();

    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "abcdefg", "abcdefghi", "abcdefghijkmnop", "12345678"})
    void invalidInviteCodes(String code) {
        InputInvitecodeDto dto = new InputInvitecodeDto("code");
        Set<ConstraintViolation<InputInvitecodeDto>> violations = validator.validate(dto);
        assertThat(violations)
                .isNotEmpty()
                .hasSize(1)
                .anyMatch(v -> v.getPropertyPath().toString().equals("inviteCode"));

    }
}