package lv.ewdj.fifaworldcup.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lv.ewdj.fifaworldcup.dto.GameInputDto;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class ValidChecksumValidator implements ConstraintValidator<ValidChecksum, GameInputDto> {

    private int modulo;

    MessageSource messageSource;

    public ValidChecksumValidator(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public void initialize(ValidChecksum constraintAnnotation) {
        modulo = constraintAnnotation.modulo();
    }

    @Override
    public boolean isValid(GameInputDto value, ConstraintValidatorContext context) {
        Integer stadiumCode = value.stadiumCode();
        Integer checksum = value.checksum();
        if (stadiumCode == null)
            return true;

        if (checksum == null) {
            buildValidationError(context,
                    messageSource.getMessage(
                            "validation.noChecksum",
                            null,
                            Locale.ENGLISH
                    ));
            return false;
        }
        if (stadiumCode % modulo != checksum) {
            buildValidationError(context,
                    messageSource.getMessage(
                            "validation.invalidChecksum",
                            new Integer[]{modulo},
                            Locale.ENGLISH
                    ));
            return false;
        }
        return true;
    }

    private void buildValidationError(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
                        message
                )
                .addPropertyNode("checksum")
                .addConstraintViolation()
        ;
    }
}
