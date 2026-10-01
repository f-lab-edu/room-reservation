package com.onerty.yeogi.common.aop;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//@Component // 주석 해제 시 common 애플리케이션 실행 시 자동 실행
@RequiredArgsConstructor
public class AopTimedSampleRunner implements CommandLineRunner {

    private final AopTimedSample aopTimedSample;

    @Override
    public void run(String... args) throws Exception {
        aopTimedSample.simulateLogic();
    }
}
