package com.onerty.yeogi.customer.room;

import com.onerty.yeogi.common.room.QAccommodation;
import com.onerty.yeogi.common.room.QRoomType;
import com.onerty.yeogi.common.room.QRoomTypeStock;
import com.onerty.yeogi.customer.room.dto.SearchAccommodationRequest;
import com.onerty.yeogi.customer.room.dto.SearchAccommodationResponse;
import com.onerty.yeogi.customer.room.dto.SearchAccommodationRoomRequest;
import com.onerty.yeogi.customer.room.dto.SearchAccommodationRoomResponse;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {

    private final JPAQueryFactory queryFactory;

    public Page<SearchAccommodationResponse> searchAccommodationsPage(
            SearchAccommodationRequest request, Pageable pageable) {

        QAccommodation accommodation = QAccommodation.accommodation;
        QRoomType roomType = QRoomType.roomType;
        QRoomTypeStock stock = QRoomTypeStock.roomTypeStock;

        long daysBetween = ChronoUnit.DAYS.between(request.checkIn(), request.checkOut());

        List<SearchAccommodationResponse> content = queryFactory
                .select(Projections.constructor(SearchAccommodationResponse.class,
                        accommodation.name,
                        accommodation.location,
                        roomType.pricePerNight.min().as("lowestPrice")))
                .from(accommodation)
                .join(accommodation.roomTypes, roomType)
                .join(roomType.stocks, stock)
                .where(accommodation.location.eq(request.location()),
                        roomType.capacity.goe(request.guestCount()),
                        stock.id.date.between(request.checkIn(), request.checkOut().minusDays(1)),
                        stock.stock.gt(0))
                .groupBy(accommodation.id)
                .having(stock.id.date.countDistinct().eq(daysBetween))
                .orderBy(accommodation.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(accommodation.id.countDistinct())
                .from(accommodation)
                .join(accommodation.roomTypes, roomType)
                .join(roomType.stocks, stock)
                .where(accommodation.location.eq(request.location()),
                        roomType.capacity.goe(request.guestCount()),
                        stock.id.date.between(request.checkIn(), request.checkOut().minusDays(1)),
                        stock.stock.gt(0))
                .groupBy(accommodation.id)
                .having(stock.id.date.countDistinct().eq(daysBetween))
                .fetch().size();

        return new PageImpl<>(content, pageable, total);
    }

    public List<SearchAccommodationResponse> searchAccommodationsCursor(
            SearchAccommodationRequest request, Long lastId, int pageSize) {

        QAccommodation accommodation = QAccommodation.accommodation;
        QRoomType roomType = QRoomType.roomType;
        QRoomTypeStock stock = QRoomTypeStock.roomTypeStock;

        long daysBetween = ChronoUnit.DAYS.between(request.checkIn(), request.checkOut());

        BooleanBuilder condition = new BooleanBuilder()
                .and(accommodation.location.eq(request.location()))
                .and(roomType.capacity.goe(request.guestCount()))
                .and(stock.id.date.between(request.checkIn(), request.checkOut().minusDays(1)))
                .and(stock.stock.gt(0));

        if (lastId != null) {
            condition.and(accommodation.id.lt(lastId));
        }

        return queryFactory
                .select(Projections.constructor(SearchAccommodationResponse.class,
                        accommodation.name,
                        accommodation.location,
                        roomType.pricePerNight.min().as("lowestPrice")))
                .from(accommodation)
                .join(accommodation.roomTypes, roomType)
                .join(roomType.stocks, stock)
                .where(condition)
                .groupBy(accommodation.id)
                .having(stock.id.date.countDistinct().eq(daysBetween))
                .orderBy(accommodation.id.desc())
                .limit(pageSize)
                .fetch();
    }

    public List<SearchAccommodationRoomResponse> searchAvailableRoomsByAccommodation(
            SearchAccommodationRoomRequest request) {

        QRoomType roomType = QRoomType.roomType;
        QRoomTypeStock stock = QRoomTypeStock.roomTypeStock;

        long daysBetween = ChronoUnit.DAYS.between(request.checkIn(), request.checkOut());

        return queryFactory
                .select(Projections.constructor(SearchAccommodationRoomResponse.class,
                        roomType.name,
                        roomType.capacity,
                        roomType.pricePerNight.multiply((int) daysBetween).as("price"),
                        roomType.description))
                .from(roomType)
                .join(roomType.stocks, stock)
                .where(roomType.accommodation.id.eq(request.AccommodationId()),
                        roomType.capacity.goe(request.guestCount()),
                        stock.id.date.between(request.checkIn(), request.checkOut().minusDays(1)),
                        stock.stock.gt(0))
                .groupBy(roomType.id)
                .having(stock.id.date.countDistinct().eq(daysBetween))
                .orderBy(roomType.pricePerNight.asc())
                .fetch();
    }

}

