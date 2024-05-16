package waf.fisa.condition.dto;

import lombok.*;
import waf.fisa.condition.entity.Condition;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ConditionReqDto {

    private String id;

    private String accountId;

    private String location;

    private String buildingType;

    private int fee;

    private LocalDate moveInDate;

    private String hashtag;

    @Builder
    public ConditionReqDto(String accountId, String location, String buildingType,
                           int fee, LocalDate moveInDate, String hashtag) {
        this.accountId = accountId;
        this.location = location;
        this.buildingType = buildingType;
        this.fee = fee;
        this.moveInDate = moveInDate;
        this.hashtag = hashtag;
    }

    public Condition toEntity () {
        return Condition.builder()
                .id(UUID.randomUUID().toString())
                .accountId(accountId)
                .location(location)
                .buildingType(buildingType)
                .fee(fee)
                .moveInDate(moveInDate)
                .hashtag(hashtag)
                .build();
    }

    public Condition toEntityReadByWhere () {
        return Condition.builder()
                .location(location)
                .buildingType(buildingType)
                .fee(fee)
                .moveInDate(moveInDate)
                .hashtag(hashtag)
                .build();
    }
}
