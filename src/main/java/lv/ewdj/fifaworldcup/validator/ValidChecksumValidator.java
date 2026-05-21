package lv.ewdj.fifaworldcup.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import lv.ewdj.fifaworldcup.dto.InputGameDto;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
//@NoArgsConstructor //geen no-args constructor want hierdoor vindt springboottest niet de juiste constructor...
public class ValidChecksumValidator implements ConstraintValidator<ValidChecksum, InputGameDto> {

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
    public boolean isValid(InputGameDto value, ConstraintValidatorContext context) {
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
