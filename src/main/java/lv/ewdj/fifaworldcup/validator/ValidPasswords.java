package lv.ewdj.fifaworldcup.validator;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = PasswordConstraintValidator.class)
@Target(TYPE)
@Retention(RUNTIME)
public @interface ValidPasswords {

    String message() default "password does not match the confirm password";
    Class<?>[] groups() default{};
    Class<? extends Payload>[] payload() default {};


}
