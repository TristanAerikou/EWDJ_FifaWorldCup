package lv.ewdj.fifaworldcup.controller;

import lv.ewdj.fifaworldcup.advice.GameValidatorAdvice;
import lv.ewdj.fifaworldcup.dto.*;
import lv.ewdj.fifaworldcup.model.Role;
import lv.ewdj.fifaworldcup.model.Team;
import lv.ewdj.fifaworldcup.service.TeamService;
import lv.ewdj.fifaworldcup.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TeamController.class)
class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TeamService teamService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    GameValidatorAdvice  gameValidatorAdvice;

    TestingAuthenticationToken testPrincipal = new TestingAuthenticationToken("testUser", "password", "ROLE_USER");

    private static OutputUserDto provideOwnerDto() {
        return new OutputUserDto(1L, "ownerUser", "Owen", "Owner", null, null, Role.USER, 10);
    }


    private static OutputTeamDto provideOutputTeamDto() {
        return new OutputTeamDto(1, "MyTeam", "INVITEIN");
    }

    private static List<OutputUserDto> provideOutputUserDtos() {
        return List.of(
                new OutputUserDto(1L, "ownerUser",  "Owen",   "Owner",  null, null, Role.USER, 10),
                new OutputUserDto(2L, "memberUser", "Minnie", "Member", null, null, Role.USER,  5)
        );
    }

    private static Team provideTeam() {
        Team team = Mockito.mock(Team.class);
        Mockito.when(team.getName()).thenReturn("BestTeam");
        return team;
    }


    // ##################
    // /team            #
    // ##################

    @Test
    void redirectRouteNoTeam() throws Exception {
        Mockito.when(teamService.getTeamByUsername(testPrincipal.getName())).thenReturn(null);

        mockMvc.perform(get("/team")
                .principal(testPrincipal))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/team/noTeam"));
    }

    @Test
    void redirectRouteInTeam() throws Exception {
        OutputTeamDto team = Mockito.mock(OutputTeamDto.class);
        Mockito.when(teamService.getTeamByUsername(testPrincipal.getName())).thenReturn(team);

        mockMvc.perform(get("/team")
                        .principal(testPrincipal))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/team/myTeam"));
    }

    // ######################
    // GET /team/noTeam     #
    // ######################


    @Test
    void showNoTeam_userHasNoTeam() throws Exception {
        Mockito.when(teamService.getTeamByUsername("testUser")).thenReturn(null);

        mockMvc.perform(get("/team/noTeam")
                        .principal(testPrincipal))
                .andExpect(status().isOk())
                .andExpect(view().name("noTeam"))
                .andExpect(model().attributeExists("inputTeamDto"))
                .andExpect(model().attributeExists("inputInvitecodeDto"));
    }

    @Test
    void showNoTeam_userAlreadyHasTeam_redirectsToMyTeam() throws Exception {
        Mockito.when(teamService.getTeamByUsername("testUser")).thenReturn(provideOutputTeamDto());

        mockMvc.perform(get("/team/noTeam").principal(testPrincipal))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/team/myTeam"));
    }

    // ######################
    // POST /team/create    #
    // ######################

    @Test
    void createTeam_validRequest() throws Exception {
        InputTeamDto validDto = new InputTeamDto("myTeam");

        Mockito.doNothing()
                .when(teamService).createTeam(Mockito.any(InputTeamDto.class), Mockito.eq("testUser"));

        mockMvc.perform(post("/team/create")
                        .flashAttr("inputTeamDto", validDto)
                        .principal(testPrincipal)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/team"));

        Mockito.verify(teamService, Mockito.times(1)).createTeam(validDto, "testUser");
    }

    @Test
    void createTeam_invalidRequest_redirectsToNoTeam() throws Exception {
        InputTeamDto invalidDto = new InputTeamDto("");

        mockMvc.perform(post("/team/create")
                        .flashAttr("inputTeamDto", invalidDto)
                        .principal(testPrincipal)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/team/noTeam"))
                .andExpect(flash().attribute("inputTeamDto", invalidDto))
                // opnieuw, je kunt niet checken in het model() of er errors zijn door de redirect
                .andExpect(flash().attributeExists("org.springframework.validation.BindingResult.inputTeamDto"));

        Mockito.verify(teamService, Mockito.never()).createTeam(Mockito.any(), Mockito.anyString());
    }

    @Test
    void createTeam_invalidRequest_bindingResultAndDtoInFlashAttributes() throws Exception {
        // niet zo fan van deze test; doordat er geredirect wordt bij een fout is het moeilijk om hier goed te testen.
        // Ook kun je hard ingaan op deze test en gaan de redirect volgen om te checken of de juiste velden valdiation errors geven...
        // maar dat gaat verder in SpringBoot logics dan ik aan deze test tijd wil spenderen
        InputTeamDto invalidDto = new InputTeamDto("");

        mockMvc.perform(post("/team/create")
                        .flashAttr("inputTeamDto", invalidDto)
                        .principal(testPrincipal)
                )
                .andExpect(flash().attributeExists("org.springframework.validation.BindingResult.inputTeamDto"))
                .andExpect(flash().attributeExists("inputTeamDto"));
    }

    @Test
    void createTeam_duplicateTeamName_addsFlashMessageAndRedirectsToNoTeam() throws Exception {
        InputTeamDto validDto = new InputTeamDto("ExistingTeam");

        Mockito.doThrow(new DataIntegrityViolationException("Duplicate entry 'ExistingTeam'"))
                .when(teamService).createTeam(Mockito.any(), Mockito.anyString());

        mockMvc.perform(post("/team/create")
                        .flashAttr("inputTeamDto", validDto)
                        .principal(testPrincipal)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/team/noTeam"))
                .andExpect(flash().attributeExists("duplEntryExc"));
    }

    // naar mijn mening slechte test
//    @Test
//    void createTeam_otherDataIntegrityViolation_noFlashMessage() throws Exception {
//        InputTeamDto validDto = new InputTeamDto("SomeTeam");
//
//        Mockito.doThrow(new DataIntegrityViolationException("some other constraint violation"))
//                .when(teamService).createTeam(Mockito.any(), Mockito.anyString());
//
//        mockMvc.perform(post("/team/create")
//                        .flashAttr("inputTeamDto", validDto)
//                        .principal(testPrincipal)
//                )
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/team/noTeam"))
//                .andExpect(flash().attributeDoesNotExist("duplEntryExc"));
//    }

    // ######################
    // POST /team/join      #
    // ######################

    @Test
    void joinTeam_validRequest() throws Exception {
        InputInvitecodeDto validDto = new InputInvitecodeDto("ABC123GH");

        Mockito.doNothing().when(teamService).joinTeam("testUser", "ABC123GH");

        mockMvc.perform(post("/team/join")
                        .flashAttr("inputInvitecodeDto", validDto)
                        .principal(testPrincipal)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/team/myTeam"));

        Mockito.verify(teamService, Mockito.times(1)).joinTeam("testUser", "ABC123GH");
    }

    @Test
    void joinTeam_invalidRequest_redirectsToNoTeam() throws Exception {
        InputInvitecodeDto invalidDto = new InputInvitecodeDto("");

        mockMvc.perform(post("/team/join")
                        .flashAttr("inputInvitecodeDto", invalidDto)
                        .principal(testPrincipal)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/team/noTeam"));

        Mockito.verify(teamService, Mockito.never()).joinTeam(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    void joinTeam_invalidRequest_bindingResultAndDtoInFlashAttributes() throws Exception {
        InputInvitecodeDto invalidDto = new InputInvitecodeDto("");

        mockMvc.perform(post("/team/join")
                        .flashAttr("inputInvitecodeDto", invalidDto)
                        .principal(testPrincipal)
                )
                .andExpect(flash().attributeExists("org.springframework.validation.BindingResult.inputInvitecodeDto"))
                .andExpect(flash().attributeExists("inputInvitecodeDto"));
    }

    // ######################
    // GET /team/myTeam     #
    // ######################

    @Test
    void showMyTeam_userHasTeam() throws Exception {
        OutputTeamDto teamDto = provideOutputTeamDto();

        Mockito.when(teamService.getTeamByUsername("testUser")).thenReturn(teamDto);
        Mockito.when(userService.getUsersByTeamName(teamDto.name())).thenReturn(provideOutputUserDtos());
        Mockito.when(userService.getUserOwningTeam(teamDto.name())).thenReturn(provideOwnerDto());

        mockMvc.perform(get("/team/myTeam")
                        .principal(testPrincipal))
                .andExpect(status().isOk())
                .andExpect(view().name("teamPage"))
                .andExpect(model().attributeExists("team"))
                .andExpect(model().attributeExists("totalScore"));
    }

    @Test
    void showMyTeam_totalScoreIsSumOfUserPoints() throws Exception {
        OutputTeamDto teamDto = provideOutputTeamDto();
        List<OutputUserDto> users = provideOutputUserDtos(); // 10 + 5 = 15

        Mockito.when(teamService.getTeamByUsername("testUser")).thenReturn(teamDto);
        Mockito.when(userService.getUsersByTeamName(teamDto.name())).thenReturn(users);
        Mockito.when(userService.getUserOwningTeam(teamDto.name())).thenReturn(provideOwnerDto());

        int expectedTotal = users.stream().mapToInt(OutputUserDto::points).sum();

        mockMvc.perform(get("/team/myTeam").principal(testPrincipal))
                .andExpect(model().attribute("totalScore", expectedTotal));
    }

    @Test
    void showMyTeam_userHasNoTeam_redirectsToNoTeam() throws Exception {
        Mockito.when(teamService.getTeamByUsername("testUser")).thenReturn(null);

        mockMvc.perform(get("/team/myTeam").principal(testPrincipal))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/team/noTeam"));
    }

    // ############################
    // POST /team/user/remove     #
    // ############################

    @Test
    void removeMember_validRequest() throws Exception {
        InputRemovememberDto dto = new InputRemovememberDto("AwesomeTeam", "memberUser");
        Team team = provideTeam();

        Mockito.when(teamService.getTeamByTeamname("AwesomeTeam")).thenReturn(team);
        Mockito.when(userService.getUserOwningTeam(team.getName())).thenReturn(provideOwnerDto());
        Mockito.doNothing().when(teamService).removeMember("memberUser");

        mockMvc.perform(post("/team/user/remove")
                        .flashAttr("inputRemovememberDto", dto)
                        .principal(testPrincipal)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/team/myTeam"));

        Mockito.verify(teamService, Mockito.times(1)).removeMember("memberUser");

    }

    @Test
    void removeMember_tryToRemoveOwner_illegalArgumentExceptionHandled() throws Exception {
        // username being removed matches the owner → IllegalArgumentException
        InputRemovememberDto dto = new InputRemovememberDto("AwesomeTeam", "ownerUser");
        Team team = provideTeam();

        Mockito.when(teamService.getTeamByTeamname("AwesomeTeam")).thenReturn(team);
        Mockito.when(userService.getUserOwningTeam(team.getName())).thenReturn(provideOwnerDto());

        mockMvc.perform(post("/team/user/remove")
                        .flashAttr("inputRemovememberDto", dto)
                        .principal(testPrincipal)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/team/myTeam"))
                .andExpect(flash().attributeExists("illArgExc"));

        Mockito.verify(teamService, Mockito.never()).removeMember(Mockito.anyString());
    }


}