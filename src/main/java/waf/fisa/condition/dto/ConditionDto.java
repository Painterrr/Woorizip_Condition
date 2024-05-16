package waf.fisa.condition.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import waf.fisa.condition.entity.Condition;

import java.time.LocalDate;

@Getter
public class ConditionDto {

    private String id;

    private String accountId;

    private String location;

    private String buildingType;

    private int fee;

    private LocalDate moveInDate;

    private String hashtag;


    @QueryProjection
    public ConditionDto(String id, String accountId, String location, String buildingType,
                        int fee, LocalDate moveInDate, String hashtag) {
        this.id = id;
        this.accountId = accountId;
        this.location = location;
        this.buildingType = buildingType;
        this.fee = fee;
        this.moveInDate = moveInDate;
        this.hashtag = hashtag;
    }

    public ConditionRespDto toConditionRespDto(String id, String accountId, String location, String buildingType,
                              int fee, LocalDate moveInDate, String hashtag) {

        return new ConditionRespDto(id, accountId, location, buildingType,
        fee, moveInDate, hashtag);
    }

    public Condition toEntity() {
        return Condition.builder()
                .id(id)
                .accountId(accountId)
                .location(location)
                .buildingType(buildingType)
                .fee(fee)
                .moveInDate(moveInDate)
                .hashtag(hashtag)
                .build();
    }
}
