package lv.ewdj.fifaworldcup.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = ValidChecksumValidator.class)
@Target(ElementType.TYPE)
@Documented //TODO needed?
@Retention(RUNTIME) //TODO needed?
public @interface ValidChecksum {
    String message() default "checksum is invalid";
    Class<?>[] groups() default{};
    Class<? extends Payload>[] payload() default {};

    int modulo() default 0;
}
