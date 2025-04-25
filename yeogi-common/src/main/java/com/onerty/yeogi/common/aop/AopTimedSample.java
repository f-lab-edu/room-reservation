package com.onerty.yeogi.common.aop;

import org.springframework.stereotype.Service;

@Service
public class AopTimedSample {

    @Timed
    public void simulateLogic() {
        try {
            Thread.sleep(1000); // 일부러 딜레이
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("샘플 로직 실행 완료 🧪");
    }
}
