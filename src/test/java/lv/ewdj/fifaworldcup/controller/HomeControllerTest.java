package lv.ewdj.fifaworldcup.controller;

import lv.ewdj.fifaworldcup.dto.OutputGameDto;
import lv.ewdj.fifaworldcup.helpers.helperVariables;
import lv.ewdj.fifaworldcup.service.GameService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

        List<OutputGameDto> expectedGames = helperVariables.provideExpectedGames();

        Mockito.when(gameService.findAllGames()).thenReturn(expectedGames);

        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("homeScreen"))
                .andExpect(model().attributeExists("allGames"))
                .andExpect(model().attribute("allGames", expectedGames));
    }
}