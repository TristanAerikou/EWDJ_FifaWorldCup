package lv.ewdj.fifaworldcup.config;

import lv.ewdj.fifaworldcup.dto.OutputGameDto;
import lv.ewdj.fifaworldcup.helpers.helperVariables;
import lv.ewdj.fifaworldcup.service.GameService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GameService gameService;

    @MockitoBean
    UserDetailsService userDetailsService;

    @Test
    void AccessWithUserRole() throws Exception {

        List<OutputGameDto> expectedGames = helperVariables.provideExpectedGames();

        Mockito.when(gameService.findAllGames()).thenReturn(expectedGames);

        //TODO verander `.with(...)` naar oplossing zoals in vb-project;
        // je kunt namelijk wél `when(userService.findByUsername("user")).thenReturn(normalUser);` doen aangezien de UserDetailsService weliswaar een UserService gebruikt.

        mockMvc.perform(get("/home")
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("homeScreen"))
                .andExpect(model().attributeExists("allGames"))
                .andExpect(model().attribute("allGames", expectedGames));
    }
    @Test

    void noAccessWithWrongRole() throws Exception {
        mockMvc.perform(get("/home")
                        .with(user("user").roles("NO_USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void AdminAccessWithWrongRole() throws Exception {
        mockMvc.perform(get("/game/create")
                        .with(user("user").roles("USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void AdminAccessWithAdminRole() throws Exception {
        mockMvc.perform(get("/game/create")
                        .with(user("user").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    void noAccessAnonymous() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(redirectedUrl("/login"));
    }

}