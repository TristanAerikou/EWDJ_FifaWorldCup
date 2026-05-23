package lv.ewdj.fifaworldcup.dto;

import lv.ewdj.fifaworldcup.model.Prognosis;

public record OutputPrognosisDto (
        Long id,
        int goalsTeamsA,
        int goalsTeamsB,
        OutputGameDto game,
        OutputUserDto user
        ){

    public static OutputPrognosisDto objToDto(Prognosis prognosis){
        return new OutputPrognosisDto(
                prognosis.getId(),
                prognosis.getGoalsTeamA(),
                prognosis.getGoalsTeamB(),
                OutputGameDto.objToDto(prognosis.getGame()),
                OutputUserDto.objToDto(prognosis.getUser())
        );
    }
}
