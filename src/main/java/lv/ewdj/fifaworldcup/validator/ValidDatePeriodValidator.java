package lv.ewdj.fifaworldcup.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
//@NoArgsConstructor //geen no-args constructor want hierdoor vindt springboottest niet de juiste constructor...
public class ValidDatePeriodValidator implements ConstraintValidator<ValidDatePeriod, LocalDate> {

    @Value("${rule.startdate}")
    private String fifaStartDateStr;
    @Value("${rule.enddate}")
    private String fifaEndDateStr;

    public LocalDate fifaStartDate;
    public LocalDate fifaEndDate;

//    @Value("#{messageSource.getMessage('validation.invalidDatePeriod', new String[] {fifaStartDateStr, fifaEndDateStr}, 'en' )}")
//    private String errorMsg;

    MessageSource messageSource;

    public ValidDatePeriodValidator(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public void initialize(ValidDatePeriod constraintAnnotation) {
        this.fifaStartDate = LocalDate.parse(fifaStartDateStr, DateTimeFormatter.ISO_LOCAL_DATE);
        this.fifaEndDate = LocalDate.parse(fifaEndDateStr, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {


        if (value == null)
            return true;

        boolean isValid =
                value.isAfter(fifaStartDate.minusDays(1)) && value.isBefore(fifaEndDate.plusDays(1));

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate
                            (messageSource.getMessage(
                                    "validation.invalidDatePeriod",
                                    new String[]{fifaStartDateStr.replace("-", "/"), fifaEndDateStr.replace("-", "/")},
                                    Locale.ENGLISH)
                            )
                    .addConstraintViolation();
        }

        return isValid;

    }
}
