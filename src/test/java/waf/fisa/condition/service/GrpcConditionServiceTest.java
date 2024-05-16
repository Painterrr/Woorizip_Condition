package waf.fisa.condition.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import waf.fisa.condition.dto.ConditionReqDto;
import waf.fisa.condition.repository.ConditionRepository;
import waf.fisa.condition.repository.ConditionRepositoryCustom;
import waf.fisa.grpc.condition.ConditionReq;

@ExtendWith(MockitoExtension.class)
@Getter
@Setter
public class GrpcConditionServiceTest {

    @InjectMocks
    private GrpcConditionService grpcConditionService;

    @Mock
    private ConditionRepository conditionRepository;

    @Mock
    private ConditionRepositoryCustom conditionRepositoryCustom;

    @Spy
    private ObjectMapper om;


    @Test
    void saveCondition() throws Exception {
        // given
        String id = "user01";




    }

    @Test
    void readCondition() throws Exception {
    }

    @Test
    void readAllCondition() throws Exception {
    }

    @Test
    void readByWhereCondition() throws Exception {
    }

    @Test
    void updateCondition() throws Exception {
    }

    @Test
    void deleteCondition() throws Exception {
    }
}
