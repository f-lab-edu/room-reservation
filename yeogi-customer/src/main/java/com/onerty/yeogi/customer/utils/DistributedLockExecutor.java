package com.onerty.yeogi.customer.utils;


import com.onerty.yeogi.common.exception.ErrorType;
import com.onerty.yeogi.common.exception.YeogiException;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class DistributedLockExecutor {

    private final RedissonClient redissonClient;

    public <T> T executeWithLock(String lockKey, long waitTimeSec, long leaseTimeSec, Supplier<T> action) {
        RLock lock = redissonClient.getLock(lockKey);

        try {
            boolean isLocked = lock.tryLock(waitTimeSec, leaseTimeSec, TimeUnit.SECONDS);
            if (!isLocked) {
                throw new YeogiException(ErrorType.LOCK_ACQUISITION_FAILED);
            }

            return action.get();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new YeogiException(ErrorType.LOCK_INTERRUPTED);

        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock(); // 락 해제
            }
        }
    }
}