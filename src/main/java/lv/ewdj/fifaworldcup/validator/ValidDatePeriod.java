package lv.ewdj.fifaworldcup.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = ValidDatePeriodValidator.class)
@Target(ElementType.FIELD)
@Documented
@Retention(RUNTIME)
public @interface ValidDatePeriod {

    String message() default "date is not in allowed period";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
