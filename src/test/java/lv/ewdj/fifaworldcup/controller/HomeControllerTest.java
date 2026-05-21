package lv.ewdj.fifaworldcup.controller;

import lv.ewdj.fifaworldcup.config.SecurityConfig;
import lv.ewdj.fifaworldcup.dto.GameOutputDto;
import lv.ewdj.fifaworldcup.helpers.helperVariables;
import lv.ewdj.fifaworldcup.model.Game;
import lv.ewdj.fifaworldcup.service.FWCUserDetailsService;
import lv.ewdj.fifaworldcup.service.GameService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(HomeController.class)
//@Import(SecurityConfig.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GameService gameService;

//    @MockitoBean
//    private FWCUserDetailsService userDetailsService;

    @Test
//    @WithMockUser
    void getHome() throws Exception {

        List<GameOutputDto> expectedGames = helperVariables.provideExpectedGames();

        Mockito.when(gameService.findAllGames()).thenReturn(expectedGames);

        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("homeScreen"))
                .andExpect(model().attributeExists("allGames"))
                .andExpect(model().attribute("allGames", expectedGames));
    }
}