package lv.ewdj.fifaworldcup.controller;

import lv.ewdj.fifaworldcup.advice.GameValidatorAdvice;
import lv.ewdj.fifaworldcup.dto.InputRegistrationDto;
import lv.ewdj.fifaworldcup.exceptions.UserExistsException;
import lv.ewdj.fifaworldcup.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.View;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockitoBean
    private GameValidatorAdvice gameValidatorAdvice;

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    TestingAuthenticationToken testPrincipal = new TestingAuthenticationToken("testUser", "password", "ROLE_USER");
    private View view;


    // ######################
    // get register         #
    // ######################

    @Test
    void showRegisterForm_WithoutPrincipal() throws Exception {

        mockMvc.perform(get("/user/register"))
                .andExpect(view().name("registerUser"))
                .andExpect(model().attributeExists("inputRegistrationDto"));

    }

    @Test
    void showRegisterForm_WithPrincipal() throws Exception {

        mockMvc.perform(get("/user/register")
                        .principal(testPrincipal))
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void postValidRegisterForm() throws Exception {

        InputRegistrationDto dto = new InputRegistrationDto(
                "myUsername",
                "myPassword",
                "myPassword",
                "myfname",
                "mylname"
        );

        mockMvc.perform(post("/user/register")
                .flashAttr("inputRegistrationDto", dto)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(model().attributeHasNoErrors());

        Mockito.verify(userService).registerUser(dto);

    }

    @Test
    void postInvalidRegisterForm() throws Exception {

        InputRegistrationDto dto = new InputRegistrationDto(
                "",
                "",
                "",
                "",
                ""
        );

        mockMvc.perform(post("/user/register")
                        .flashAttr("inputRegistrationDto", dto))
                .andExpect(status().isOk())
                .andExpect(view().name("registerUser"))
                .andExpect(model().attributeHasFieldErrors("inputRegistrationDto", "username", "password", "confirmPassword", "firstname", "lastname"));

        Mockito.verify(userService, Mockito.never()).registerUser(dto);
    }

    @Test
    void postRegisterForm_existingUser() throws Exception {


        InputRegistrationDto dto = new InputRegistrationDto(
                "myUsername",
                "myPassword",
                "myPassword",
                "myfname",
                "mylname"
        );

        Mockito.doThrow(UserExistsException.class)
                        .when(userService).registerUser(dto);

        mockMvc.perform(post("/user/register")
                        .flashAttr("inputRegistrationDto", dto))
                .andExpect(status().isOk())
                .andExpect(view().name("registerUser"))
                .andExpect(model().attribute("userExists", true));

        Mockito.verify(userService).registerUser(dto);
    }

}