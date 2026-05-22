package lv.ewdj.fifaworldcup.advice;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Collections;
import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("username")
    public String populateUsername(Authentication authentication) {
        return authentication == null ? "" : authentication.getName();
    }

    @ModelAttribute("role")
    public List<String> populateAuthorities(Authentication authentication) {
        return authentication == null ? Collections.emptyList() : authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
    }

}
