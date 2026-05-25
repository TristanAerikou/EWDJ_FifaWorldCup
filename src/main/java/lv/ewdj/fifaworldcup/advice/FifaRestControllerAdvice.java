package lv.ewdj.fifaworldcup.advice;

import lv.ewdj.fifaworldcup.dto.ErrorResponse;
import lv.ewdj.fifaworldcup.exceptions.GameNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@RestControllerAdvice
public class FifaRestControllerAdvice {


    @ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDateTimeParseException(DateTimeParseException ex) {
        return new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                LocalDate.now().toString()
        );
    }

    @ExceptionHandler(GameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleGameNotFoundException(GameNotFoundException ex) {
        return new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                LocalDate.now().toString()
        );
    }
}
