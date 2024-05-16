package waf.fisa.condition.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import waf.fisa.condition.dto.ConditionRespDto;
import waf.fisa.grpc.condition.ConditionReq;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Table(name = "filter")
public class Condition {

    /**
     * 무작위로 생성되는 UUID. Condition의 구분값
     */
    @Id
    private String id;

    /**
     * Condition을 등록한 의뢰인
     */
    private String accountId;

    /**
     * 기본 조건: 소재지
     */
    @Column(nullable = false)
    private String location;

    /**
     * 기본 조건: 건물 유형
     */
    @Column(nullable = false)
    private String buildingType;

    /**
     * 상세 조건: 월세 상한가
     */
    @Column(nullable = false)
    private int fee;

    /**
     * 상세 조건: 입주 가능일
     */
    @Column(nullable = false)
    private LocalDate moveInDate;

    /**
     * 상세 조건: 원하는 부가 옵션(해시태그)
     */
    private String hashtag;

    @Builder
    public Condition(String id, String accountId, String location, String buildingType, int fee, LocalDate moveInDate,
                     String hashtag) {
        this.id = id;
        this.accountId = accountId;
        this.location = location;
        this.buildingType = buildingType;
        this.fee = fee;
        this.moveInDate = moveInDate;
        this.hashtag = hashtag;
    }

    public static Condition toEntity (ConditionReq input) {
        return Condition.builder()
                .id(UUID.randomUUID().toString())
                .accountId(input.getAccountId())
                .location(input.getLocation())
                .buildingType(input.getBuildingType())
                .fee(input.getFee())
                .moveInDate(LocalDate.parse(input.getMoveInDate(), DateTimeFormatter.ISO_DATE))
                .hashtag(input.getHashtag())
                .build();
    }

    public ConditionRespDto toConditionRespDto(Condition condition) {
        this.id = condition.getId();
        this.accountId = condition.getAccountId();;
        this.location = condition.getLocation();
        this.buildingType = condition.getBuildingType();
        this.fee = condition.getFee();
        this.moveInDate = condition.getMoveInDate();
        this.hashtag = condition.getHashtag();

        return new ConditionRespDto(condition.getId(), condition.getAccountId(), condition.getLocation()
                , condition.getBuildingType(), condition.getFee(), condition.getMoveInDate(), condition.getHashtag());
    }

    public void updateLocation(String location) {
        this.location = location;
    }

    public void updateBuildingType(String buildingType) {
        this.buildingType = buildingType;
    }

    public void updateFee(int fee) {
        this.fee = fee;
    }

    public void updateMoveInDate(LocalDate moveInDate) {
        this.moveInDate = moveInDate;
    }

    public void updateHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

}
