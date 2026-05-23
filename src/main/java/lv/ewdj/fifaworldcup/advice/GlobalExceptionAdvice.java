package lv.ewdj.fifaworldcup.advice;

import lv.ewdj.fifaworldcup.exceptions.InvitecodeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionAdvice {
    @ExceptionHandler(InvitecodeException.class)
    public String handleInvitecodeException(InvitecodeException ex, Model model) {
        model.addAttribute("exception", ex);
        return "error/inviteCodeError";
    }

    @ExceptionHandler(RuntimeException.class)
    public String handleException(RuntimeException ex, Model model) {
        model.addAttribute("exception", ex);
        return "error/generalException";
    }
}
