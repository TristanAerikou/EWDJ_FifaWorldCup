package lv.ewdj.fifaworldcup.controller;

import lombok.RequiredArgsConstructor;
import lv.ewdj.fifaworldcup.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    
    @GetMapping
    public String listUser(Model model) {
        model.addAttribute("allUsers", userService.getAllUsers());
        model.addAttribute("usersByLastname", userService.getUsersByLastname("Blondeel"));
        model.addAttribute("usersByFirstname", userService.getUsersByFirstname("Sandra"));

//        model.addAttribute("usersByLastnameStartingWith", userService.getUserByLastnameStartingWith("blon"));
//        model.addAttribute("usersByLastnameStartingWith2", userService.getUserByLastnameStartingWith2("k"));

        return "usersOverview";
    }
}
