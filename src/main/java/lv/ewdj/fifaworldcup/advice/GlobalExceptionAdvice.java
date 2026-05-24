package lv.ewdj.fifaworldcup.advice;

import lv.ewdj.fifaworldcup.exceptions.InvitecodeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;

@ControllerAdvice
public class GlobalExceptionAdvice {
    @ExceptionHandler(InvitecodeException.class)
    public String handleInvitecodeException(InvitecodeException ex, Model model) {
        ex.printStackTrace();
        model.addAttribute("exception", ex);
        return "error/inviteCodeError";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        ex.printStackTrace();
        model.addAttribute("exception", ex.getMessage());
        return "error/error";
    }
}
