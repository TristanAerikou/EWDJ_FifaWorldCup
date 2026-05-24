package lv.ewdj.fifaworldcup.config;

import lv.ewdj.fifaworldcup.dto.OutputGameDto;
import lv.ewdj.fifaworldcup.helpers.helperVariables;
import lv.ewdj.fifaworldcup.model.Game;
import lv.ewdj.fifaworldcup.service.GameService;
import lv.ewdj.fifaworldcup.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

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

    @ParameterizedTest
    @CsvSource({
            "/game/allGames, homeScreen",
            "/public, publicRanking",
    })
    void AccessWithUserRole(String url, String expectedView) throws Exception {
        mockMvc.perform(get(url)
                        .with(user("user").roles("USER"))
                )
                .andExpect(status().isOk())
                .andExpect(view().name(expectedView));
    }

    @ParameterizedTest
    @CsvSource({
            "/game/allGames, homeScreen",
            "game/prognosis/2, prognosisView"
    })
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
                        .with(user("user").roles("ADMIN"))
                )
                .andExpect(status().isOk());
    }

    @Test
    void noAccessAnonymous() throws Exception {
        mockMvc.perform(get("/game/allGames"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

}