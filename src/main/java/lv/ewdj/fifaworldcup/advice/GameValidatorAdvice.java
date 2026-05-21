package lv.ewdj.fifaworldcup.advice;

import lombok.RequiredArgsConstructor;
import lv.ewdj.fifaworldcup.controller.GameController;
import lv.ewdj.fifaworldcup.validator.GameValidator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice(assignableTypes = GameController.class)
@RequiredArgsConstructor
public class GameValidatorAdvice {

    private final GameValidator  gameValidator;

    @InitBinder
    public void initBinder(WebDataBinder binder) {binder.addValidators(gameValidator);}

}
