package lv.ewdj.fifaworldcup.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lv.ewdj.fifaworldcup.model.Game;
import lv.ewdj.fifaworldcup.model.Prognosis;
import lv.ewdj.fifaworldcup.model.User;
import org.jspecify.annotations.Nullable;

public record InputPrognosisDto(
        @Min(0)
        @NotNull
        Integer goalsTeamsA,
        @Min(0)
        @NotNull
        Integer goalsTeamsB

//        @NotNull Integer gameId,
//        @NotNull String username

) {
    public static InputPrognosisDto objToDto(Prognosis pr) {
        return new InputPrognosisDto(
                pr.getGoalsTeamA(),
                pr.getGoalsTeamB()
//                pr.getGame().getId(),
//                pr.getUser().getUsername()
        );
    }

//    public static Prognosis DtoToObj (InputPrognosisDto pr) {
//        return new Prognosis(
//                pr.goalsTeamsA(),
//                pr.goalsTeamsB(),
//                pr.gameId,
//                pr.username
//        );
//    }
}
