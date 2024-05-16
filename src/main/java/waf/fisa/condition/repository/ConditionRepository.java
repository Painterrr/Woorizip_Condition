package waf.fisa.condition.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import waf.fisa.condition.entity.Condition;

public interface ConditionRepository extends JpaRepository<Condition, String> {

    boolean existsByAccountId(String accountId);

}
