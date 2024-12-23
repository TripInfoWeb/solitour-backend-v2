package solitour_backend.solitour.media_location.repository;

import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import solitour_backend.solitour.media_location.entity.MediaLocation;
import solitour_backend.solitour.media_location.entity.QMediaLocation;

import java.util.List;

@Repository
public class MediaLocationRepositoryImpl implements MediaLocationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MediaLocationRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<MediaLocation> findLocationByTitle(List<String> titles) {
        return queryFactory
                .selectFrom(QMediaLocation.mediaLocation)
                .where(QMediaLocation.mediaLocation.title.in(titles))
                .fetch();
    }
}
