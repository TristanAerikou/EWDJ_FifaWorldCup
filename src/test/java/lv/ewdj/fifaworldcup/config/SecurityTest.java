package lv.ewdj.fifaworldcup.config;

import lv.ewdj.fifaworldcup.controller.GameController;
import lv.ewdj.fifaworldcup.controller.HomeController;
import lv.ewdj.fifaworldcup.dto.GameOutputDto;
import lv.ewdj.fifaworldcup.helpers.helperVariables;
import lv.ewdj.fifaworldcup.service.FWCUserDetailsService;
import lv.ewdj.fifaworldcup.service.GameService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {HomeController.class, GameController.class})
@Import(SecurityConfig.class)
class SecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GameService gameService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @WithMockUser
    @Test
    void AccessWithUserRole() throws Exception {

        List<GameOutputDto> expectedGames = helperVariables.provideExpectedGames();

        Mockito.when(gameService.findAllGames()).thenReturn(expectedGames);

        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("homeScreen"))
                .andExpect(model().attributeExists("allGames"))
                .andExpect(model().attribute("allGames", expectedGames));
    }

    @Test
    @WithMockUser(roles = "USER")
    void AdminAccessWithWrongRole() throws Exception {
        mockMvc.perform(get("/game/create"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void AdminAccessWithAdminRole() throws Exception {
        mockMvc.perform(get("/game/create"))
                .andExpect(status().isOk());
    }


}