package com.onerty.yeogi.batch.room.test;
import com.onerty.yeogi.batch.room.repository.*;
import com.onerty.yeogi.batch.room.repository.projections.ActualRoomProjection;
import com.onerty.yeogi.common.aop.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.springframework.batch.item.ItemReader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.concurrent.atomic.AtomicLong;

import java.util.stream.Collectors;
import java.util.stream.Stream;



//@Component
@RequiredArgsConstructor
@Slf4j
public class RoomQueryTestRunner implements CommandLineRunner {

    private final ActualRoomRepository actualRoomRepository;

    @Timed
    @Override
    @Transactional(readOnly = true)
    public void run(String... args) {
        System.out.println( "⚒️️ ... RoomQueryTestRunner 실행 중");

        try (Stream<ActualRoomProjection> stream = actualRoomRepository.streamAllActualRoom()) {
            long count = stream
                    .count(); // 총 개수 확인
            System.out.println("총 조회 수 = " + count);
        }
    }
}



