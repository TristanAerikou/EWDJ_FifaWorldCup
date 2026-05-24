package lv.ewdj.fifaworldcup.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.ewdj.fifaworldcup.dto.InputRegistrationDto;
import lv.ewdj.fifaworldcup.exceptions.UserExistsException;
import lv.ewdj.fifaworldcup.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

//    @GetMapping
//    public String listUser(Model model) {
//        model.addAttribute("allUsers", userService.getAllUsers());
//        return "usersOverview";
//    }

    @GetMapping("register")
    public String registerUser(Model model, InputRegistrationDto inputRegistrationDto, Principal principal) {
        if (principal != null) {
            return "redirect:/";
        }

        return "registerUser";
    }

    @PostMapping("register")
    public String processRegisterUser(Model model,
                                      @Valid InputRegistrationDto inputRegistrationDto,
                                      BindingResult result) {

        if (result.hasErrors()) {
            return "registerUser";
        }

        try {
            userService.registerUser(inputRegistrationDto);
        } catch (UserExistsException e) {
            log.error(e.getMessage());
            model.addAttribute("userExists", true);
            return "registerUser";
        }

        return "redirect:/login";
    }

}
