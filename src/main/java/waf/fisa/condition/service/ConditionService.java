package waf.fisa.condition.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import waf.fisa.condition.dto.ConditionDto;
import waf.fisa.condition.dto.ConditionReqDto;
import waf.fisa.condition.dto.ConditionRespDto;
import waf.fisa.condition.entity.Condition;
import waf.fisa.condition.repository.ConditionRepository;
import waf.fisa.condition.repository.ConditionRepositoryCustom;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@NoArgsConstructor
public class ConditionService {

    private ConditionRepository conditionRepository;
    private ConditionRepositoryCustom conditionRepositoryCustom;

    @Autowired
    public ConditionService (ConditionRepository conditionRepository, ConditionRepositoryCustom conditionRepositoryCustom) {
        this.conditionRepository = conditionRepository;
        this.conditionRepositoryCustom = conditionRepositoryCustom;
    }

    /**
     * isRegistered: 특정 의뢰인이 등록한 조건이 있는지 확인
     * @param accountId: 유저 ID
     * condition.isPresent(): true -> 등록된 조건 있음 -> 등록 불가
     * condition.isPresent(): false  -> 등록된 조건 없음 -> 등록 가능
     * @return boolean
     */
    public Boolean isRegistered (String accountId) {
        log.info("[in service]: " + accountId);

        Optional<ConditionDto> condition = conditionRepositoryCustom.readMyConditions(accountId);

        return condition.isPresent();
    }

    /**
     * save: 설정한 조건 등록
     * @param conditionReqDto: condition(id, accountId, location, buildType, fee, moveInDate, hashtag)
     * @return ConditionRespDto: condition(id, accountId, location, buildType, fee, moveInDate, hashtag)
     */
    public ConditionRespDto save (ConditionReqDto conditionReqDto) {
        log.info("[in Service]: " + conditionReqDto.toString());

        Condition condition = conditionRepository.save(conditionReqDto.toEntity());

        return new ConditionRespDto(condition);
    }

    /**
     * read: 자신의 조건만 조회(의뢰인 당 1개의 조건만 등록 가능)
     * @param accountId: 유저 ID
     * @return conditionDto: condition(id, accountId, location, buildType, fee, moveInDate, hashtag)
     */
    public ConditionDto read(String accountId) {
        log.info("[in Service]: " + accountId.toString());

        ConditionDto conditionDto = conditionRepositoryCustom.readMyConditions(accountId)
                .orElseThrow(EntityExistsException::new);

        return conditionDto;
    }

    /**
     * readAll: 공인중개사가 보는 전체 조회
     * @return List<ConditionRespDto>: list<condition(id, accountId, location, buildType, fee, moveInDate, hashtag)>
     */
    public List<ConditionRespDto> readAll() {
        log.info("[in Service]: {}");

        List<Condition> list = conditionRepository.findAll();

        return convertToRespDtoFromCondition(list);
    }

    private static List<ConditionRespDto> convertToRespDtoFromCondition(List<Condition> list) {
        return list.stream().map(condition -> condition.toConditionRespDto(condition)
        ).toList();
    }

    /**
     * readByWhere: 조건 필터링을 통해 받은 객체를 builder로 재구성 후, 해당 builder를 기준으로 조회
     * @param conditionReqDto: condition(location, buildType, fee, moveInDate, hashtag)
     *                       location, buildingType, moveInDate, hashtag == "" 가능
     *                       fee == 0 가능
     * @return List<ConditionRespDto>: list<condition(id, accountId, location, buildType, fee, moveInDate, hashtag)>
     */
    public List<ConditionRespDto> readByWhere(ConditionReqDto conditionReqDto) {
        log.info("[in Service]: " + conditionReqDto.toString());

        Condition condition = conditionReqDto.toEntityReadByWhere();

        List<ConditionDto> list = conditionRepositoryCustom.readByBuilder(condition);

        if (list.size() != 0) {
            log.info("list ele");
            for (ConditionDto ele : list) {
                log.info("{}, {}, {}, {}, {}", ele.getLocation(), ele.getBuildingType(), ele.getFee(), ele.getMoveInDate(), ele.getHashtag());
            }
        } else {
            log.info("** list is empty.");
        }

        return convertToRespDtoFromConditionDto(list);
    }

    private static List<ConditionRespDto> convertToRespDtoFromConditionDto(List<ConditionDto> list) {
        return list.stream().map(conditionDto -> conditionDto.toConditionRespDto(conditionDto.getId()
                ,conditionDto.getAccountId()
                ,conditionDto.getLocation()
                ,conditionDto.getBuildingType()
                ,conditionDto.getFee()
                ,conditionDto.getMoveInDate()
                ,conditionDto.getHashtag())
        ).toList();
    }

    /**
     * update: 조건 컬럼을 받아서 더티 체킹 후 해당 로우 업데이트
     * @param conditionReqDto: condition(accountId, location, buildType, fee, moveInDate, hashtag)
     * @return ConditionRespDto: condition(accountId, location, buildType, fee, moveInDate, hashtag)
     */
    @Transactional
    public ConditionRespDto update(ConditionReqDto conditionReqDto) {
        log.info("[in Service][input]: " + conditionReqDto.toString());

        Condition target = conditionRepository.findById(conditionReqDto.getId())
                .orElseThrow(EntityNotFoundException::new);

        log.info("[in Service][target]: " + target.toString());

        if (!conditionReqDto.getLocation().equals(target.getLocation())) {
            target.updateLocation(conditionReqDto.getLocation());
        }

        if (!conditionReqDto.getBuildingType().equals(target.getBuildingType())) {
            target.updateBuildingType(conditionReqDto.getBuildingType());
        }

        if (conditionReqDto.getFee() != 0) {
            target.updateFee(conditionReqDto.getFee());
        }

        if (!conditionReqDto.getMoveInDate().isEqual(target.getMoveInDate())) {
            target.updateMoveInDate(conditionReqDto.getMoveInDate());
        }

        if (!conditionReqDto.getHashtag().equals(target.getHashtag())) {
            target.updateHashtag(conditionReqDto.getHashtag());
        }

        log.info("[in Service][target]: " + target.toString());

        return new ConditionRespDto(target);
    }


    /**
     * delete: id로 특정 조건 삭제
     * @param id: 등롣된 조건 UUID
     * @return String
     */
    @Transactional
    public String delete(String id) {
        log.info("[in Service]: " + id);

        if (conditionRepository.existsById(id)) {
            conditionRepository.deleteById(id);
            log.info("** in if: target was deleted.");
            return "deleted";
        } else {
            log.info("** in if: target didnt existed.");
            return "fail";
        }
    }
}
