package lv.ewdj.fifaworldcup.controller;

import lv.ewdj.fifaworldcup.advice.GameValidatorAdvice;
import lv.ewdj.fifaworldcup.dto.OutputPublicTeamDto;
import lv.ewdj.fifaworldcup.dto.OutputUserDto;
import lv.ewdj.fifaworldcup.model.Role;
import lv.ewdj.fifaworldcup.model.Team;
import lv.ewdj.fifaworldcup.repository.TeamRepository;
import lv.ewdj.fifaworldcup.service.TeamService;
import lv.ewdj.fifaworldcup.service.UserService;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.hamcrest.beans.HasProperty;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;


@WebMvcTest(PublicRankingController.class)
class PublicRankingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TeamRepository teamRepository;
    @MockitoBean
    private TeamService teamService;
    @MockitoBean
    private UserService userService;

    @MockitoBean
    GameValidatorAdvice  gameValidatorAdvice;

    @Test
    void showPublicRanking() throws Exception {

        //arrange

        Team mockTeamA = Mockito.mock(Team.class);
        Mockito.when(mockTeamA.getName()).thenReturn("FC Java");

        Team mockTeamB = Mockito.mock(Team.class);
        ;
        Mockito.when(mockTeamB.getName()).thenReturn("Thymeleaf Unite");

        List<OutputUserDto> usersTeamA = List.of(
                new OutputUserDto(1, "firstuser", "first", "username", mockTeamA, mockTeamA, Role.USER, 10),
                new OutputUserDto(2, "seconduser", "second", "username", mockTeamA, null, Role.USER, 20)
        );

        // Team B has 1 member (15 total points)
        List<OutputUserDto> usersTeamB = List.of(
                new OutputUserDto(3, "thirduser", "third", "username", mockTeamA, null, Role.USER, 15)
        );

        Mockito.when(teamService.getTopTenTeams()).thenReturn(List.of(mockTeamA, mockTeamB));
        Mockito.when(userService.getUsersByTeamName("FC Java")).thenReturn(usersTeamA);
        Mockito.when(userService.getUsersByTeamName("Thymeleaf Unite")).thenReturn(usersTeamB);


        MvcResult mvcResult = mockMvc.perform(get("/public"))
                .andExpect(view().name("publicRanking"))
                .andExpect(model().attributeExists("teams"))
                .andExpect(model().attribute("teams", Matchers.instanceOf(List.class)))
                .andExpect(model().attribute("teams", Matchers.hasSize(2)))
                .andReturn();
//                .andExpect(model().attribute("teams", Matchers.hasItem(
//                        Matchers.hasProperty("name", Matchers.is("FC Java"))
//                )))
//                .andExpect(model().attribute("teams", Matchers.hasItem(
//                        Matchers.hasProperty("totalPoints", Matchers.is(30))
//                )))
//                .andExpect(model().attribute("teams", Matchers.hasItem(
//                        Matchers.hasProperty("amountOfMembers", Matchers.is(2))
//                )))
        ;

        List<OutputPublicTeamDto> teams = (List<OutputPublicTeamDto>) mvcResult.getModelAndView().getModel().get("teams");
        assertThat(teams).hasSize(2);
        assertThat(teams.get(0))
                .hasFieldOrPropertyWithValue("name", "FC Java")
                .hasFieldOrPropertyWithValue("totalPoints", 30)
                .hasFieldOrPropertyWithValue("amountOfMembers", 2)
        ;

        Mockito.verify(teamService, Mockito.times(1)).getTopTenTeams();
        Mockito.verify(userService, Mockito.times(1)).getUsersByTeamName("FC Java");
        Mockito.verify(userService, Mockito.times(1)).getUsersByTeamName("Thymeleaf Unite");


    }

}