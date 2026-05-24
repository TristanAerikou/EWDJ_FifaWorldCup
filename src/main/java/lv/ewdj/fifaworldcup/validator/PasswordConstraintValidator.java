package lv.ewdj.fifaworldcup.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lv.ewdj.fifaworldcup.dto.InputRegistrationDto;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPasswords, InputRegistrationDto> {
    @Override
    public boolean isValid(InputRegistrationDto dto, ConstraintValidatorContext context) {
        if (dto.password() == null ||
                dto.confirmPassword() == null) {
            return true;
        }

        boolean isValid = dto.password().equals(dto.confirmPassword());

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("password")
                    .addConstraintViolation()
                    .buildConstraintViolationWithTemplate("Passwords do not match")
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();
        }

        return isValid;    }
}
