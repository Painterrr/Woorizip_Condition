package waf.fisa.condition.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCondition is a Querydsl query type for Condition
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCondition extends EntityPathBase<Condition> {

    private static final long serialVersionUID = 1765993422L;

    public static final QCondition condition = new QCondition("condition");

    public final StringPath accountId = createString("accountId");

    public final StringPath buildingType = createString("buildingType");

    public final NumberPath<Integer> fee = createNumber("fee", Integer.class);

    public final StringPath hashtag = createString("hashtag");

    public final StringPath id = createString("id");

    public final StringPath location = createString("location");

    public final DatePath<java.time.LocalDate> moveInDate = createDate("moveInDate", java.time.LocalDate.class);

    public QCondition(String variable) {
        super(Condition.class, forVariable(variable));
    }

    public QCondition(Path<? extends Condition> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCondition(PathMetadata metadata) {
        super(Condition.class, metadata);
    }

}

