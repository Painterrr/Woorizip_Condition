package waf.fisa.condition.service;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.transaction.annotation.Transactional;
import waf.fisa.condition.dto.ConditionDto;
import waf.fisa.condition.dto.ConditionReqDto;
import waf.fisa.condition.entity.Condition;
import waf.fisa.condition.repository.ConditionRepository;
import waf.fisa.condition.repository.ConditionRepositoryCustom;
import waf.fisa.grpc.condition.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@GrpcService
@Slf4j
public class GrpcConditionService extends ConditionServiceGrpc.ConditionServiceImplBase {

    private final ConditionRepository conditionRepository;
    private final ConditionRepositoryCustom conditionRepositoryCustom;

    /**
     * isRegistered: 조건 등록 확인
     * @param request: accountId
     * @param responseObserver: boolean
     */
    @Override
    public void isRegistered(ConditionAccountIdReq request, StreamObserver<ConditionIsRegisteredResp> responseObserver) {
        log.info("** in log: isRegistered request: {}", request.toString());

        boolean isRegistered = true;

        Optional<ConditionDto> one = conditionRepositoryCustom.readMyConditions(request.getAccountId());

        if (one.isEmpty()) {
            isRegistered = false;
        }

        responseObserver.onNext(ConditionIsRegisteredResp.newBuilder()
                .setIsRegistered(isRegistered)
                .build());

        responseObserver.onCompleted();
    }

    /**
     * saveCondition: 조건 등록
     * @param request: accountId, location, buildingType, fee, moveInDate, hashtag
     * @param responseObserver: id, accountId, location, buildingType, fee, moveInDate, hashtag
     */
    @Override
    public void saveCondition(ConditionReq request, StreamObserver<ConditionResp> responseObserver) {
        log.info("** in log, SAVE request.toString: {}", request.toString());

        Condition entity = Condition.toEntity(request);

        Condition condition = conditionRepository.save(entity);

        responseObserver.onNext(ConditionResp.newBuilder()
                .setId(condition.getId())
                .setAccountId(condition.getAccountId())
                .setLocation(condition.getLocation())
                .setBuildingType(condition.getBuildingType())
                .setFee(condition.getFee())
                .setMoveInDate(condition.getMoveInDate().toString())
                .setHashtag(condition.getHashtag())
                .build()
        );

        responseObserver.onCompleted();
    }

    /**
     * readMyCondition: 의뢰인 조건 조회
     * @param request: accountId
     * @param responseObserver: id, accountId, location, buildingType, fee, moveInDate, hashtag
     */
    @Override
    public void readMyCondition(ConditionAccountIdReq request, StreamObserver<ConditionResp> responseObserver) {
        log.info("** in log, READ request.toString: {}", request.toString());

        Optional<ConditionDto> one = conditionRepositoryCustom.readMyConditions(request.getAccountId());

        responseObserver.onNext(ConditionResp.newBuilder()
                .setId(one.get().getId())
                .setAccountId(one.get().getAccountId())
                .setLocation(one.get().getLocation())
                .setBuildingType(one.get().getBuildingType())
                .setFee(one.get().getFee())
                .setMoveInDate(one.get().getMoveInDate().toString())
                .setHashtag(one.get().getHashtag())
                .build()
        );

        responseObserver.onCompleted();
    }

    /**
     * readAllCondition: 전체 조건 조회
     * 공인중개사가 사용하는 데이터
     * @param request: empty
     * @param responseObserver: list - id, accountId, location, buildingType, fee, moveInDate, hashtag
     */
    @Override
    public void readAllCondition(Empty request, StreamObserver<ConditionRespList> responseObserver) {
        log.info("** in log, READ ALL request.getAccount: {}");

        List<Condition> list = conditionRepository.findAll();

        log.info("** {}, {}, {}, {}, {}, {}, {}", list.get(0).getId(), list.get(0).getAccountId(), list.get(0).getLocation(),
                list.get(0).getBuildingType(), list.get(0).getFee(), list.get(0).getMoveInDate(), list.get(0).getHashtag());

        if (list.size() != 0) {
            for (Condition ele : list) {
                log.info(ele.toString());
            }
        } else {
            log.info("** list is empty.");
        }

        responseObserver.onNext(ConditionRespList.newBuilder()
                .addAllConditions(convertToRespDtoFromCondition(list))
                .build()
        );

        responseObserver.onCompleted();
    }

    private static List<waf.fisa.grpc.condition.Condition> convertToRespDtoFromCondition(List<Condition> list) {
        return list.stream().map(condition -> waf.fisa.grpc.condition.Condition.newBuilder()
                .setId(condition.getId())
                .setAccountId(condition.getAccountId())
                .setLocation(condition.getLocation())
                .setBuildingType(condition.getBuildingType())
                .setFee(condition.getFee())
                .setMoveInDate(condition.getMoveInDate().toString())
                .setHashtag(condition.getHashtag())
                .build()
        ).toList();
    }

    /**
     * readByWhere: 조건부 조건 조회
     * @param request: id, accountId, location, buildingType, fee, moveInDate, hashtag
     * @param responseObserver: list - id, accountId, location, buildingType, fee, moveInDate, hashtag
     */
    @Override
    public void readByWhereCondition(ConditionReadByWhereReq request, StreamObserver<ConditionRespList> responseObserver) {
        log.info("** in log, READ BY WHERE request.toString: {}", request.toString());

        LocalDate time = !request.getMoveInDate().isBlank() ? LocalDate.parse(request.getMoveInDate(), DateTimeFormatter.ISO_DATE) : null;

        ConditionReqDto conditionReqDto = ConditionReqDto.builder()
                .location(request.getLocation())
                .buildingType(request.getBuildingType())
                .fee(request.getFee())
                .moveInDate(time)
                .hashtag((request.getHashtag()))
                .build();

        Condition condition = conditionReqDto.toEntity();

        List<ConditionDto> list = conditionRepositoryCustom.readByBuilder(condition);

        if (list.size() != 0) {
            log.info("list ele");
            for (ConditionDto ele : list) {
                log.info("{}, {}, {}, {}, {}", ele.getLocation(), ele.getBuildingType(), ele.getFee(), ele.getMoveInDate(), ele.getHashtag());
            }
        } else {
            log.info("** list is empty.");
        }

        responseObserver.onNext(ConditionRespList.newBuilder()
                .addAllConditions(convertToEntityForReadByWhere(list))
                .build()
        );

        responseObserver.onCompleted();

    }

    private static List<waf.fisa.grpc.condition.Condition> convertToEntityForReadByWhere(List<ConditionDto> list) {
        return list.stream().map(conditionDto -> waf.fisa.grpc.condition.Condition.newBuilder()
                .setId(conditionDto.getId())
                .setAccountId(conditionDto.getAccountId())
                .setLocation(conditionDto.getLocation())
                .setBuildingType(conditionDto.getBuildingType())
                .setFee(conditionDto.getFee())
                .setMoveInDate(conditionDto.getMoveInDate().toString())
                .setHashtag(conditionDto.getHashtag())
                .build()
        ).toList();
    }

    /**
     * updateCondition: 조건 수정
     * @param request: id, accountId, location, buildingType, fee, moveInDate, hashtag
     * @param responseObserver: id, accountId, location, buildingType, fee, moveInDate, hashtag
     */
    @Override
    @Transactional
    public void updateCondition(ConditionReqWithId request, StreamObserver<ConditionResp> responseObserver) {
        log.info("** in log, UPDATE request.toString: {}", request.toString());

        Condition condition = conditionRepository.findById(request.getId()).orElseThrow(EntityNotFoundException::new);

        if (!condition.getLocation().equals(request.getLocation())) {
            condition.updateLocation(request.getLocation());
        }

        if (!condition.getBuildingType().equals(request.getBuildingType())) {
            condition.updateBuildingType(request.getBuildingType());
        }

        if (condition.getFee() != request.getFee()) {
            condition.updateFee(request.getFee());
        }

        LocalDate input = LocalDate.parse(request.getMoveInDate(), DateTimeFormatter.ISO_DATE);
        if (!condition.getMoveInDate().equals(input)) {
            condition.updateMoveInDate(input);
        }

        if (!condition.getHashtag().equals(request.getHashtag())) {
            condition.updateHashtag(request.getHashtag());
        }

        responseObserver.onNext(ConditionResp.newBuilder()
                .setId(condition.getId())
                .setLocation(condition.getLocation())
                .setBuildingType(condition.getBuildingType())
                .setAccountId(condition.getAccountId())
                .setFee(condition.getFee())
                .setMoveInDate(condition.getMoveInDate().toString())
                .setHashtag(condition.getHashtag())
                .build()
        );

        responseObserver.onCompleted();
    }

    /**
     * delete: id로 특정 조건 삭제
     * @param request: id
     * @param responseObserver: msg
     */
    @Override
    @Transactional
    public void deleteCondition(ConditionIdReq request, StreamObserver<ConditionDeleteResp> responseObserver) {
        log.info("** in log, DELETE request.toString: {}", request.toString());

        if (!conditionRepository.existsById(request.getId())) {
            responseObserver.onNext(ConditionDeleteResp.newBuilder()
                    .setMsg("It doesnt exist.")
                    .build());
            responseObserver.onCompleted();
        } else {
            conditionRepository.deleteById(request.getId());
            responseObserver.onNext(ConditionDeleteResp.newBuilder()
                    .setMsg("It was deleted.")
                    .build());
            responseObserver.onCompleted();
        }
    }
}
