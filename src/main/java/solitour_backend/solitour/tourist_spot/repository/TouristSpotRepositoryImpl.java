package solitour_backend.solitour.tourist_spot.repository;

import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import solitour_backend.solitour.tourist_spot.entity.QTouristSpot;
import solitour_backend.solitour.tourist_spot.entity.TouristSpot;
import solitour_backend.solitour.tourist_spot.spot_type.SpotType;

import java.util.List;

@Repository
public class TouristSpotRepositoryImpl extends QuerydslRepositorySupport implements TouristSpotRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public TouristSpotRepositoryImpl(JPAQueryFactory queryFactory) {
        super(TouristSpot.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public List<TouristSpot> findTouristSpotBySpotType(List<SpotType> type) {
        return queryFactory
                .selectFrom(QTouristSpot.touristSpot)
                .where(QTouristSpot.touristSpot.spotType.in(type))
                .fetch();
    }
}
