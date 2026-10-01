package com.onerty.yeogi.batch.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationExpireJobRunner implements CommandLineRunner {

    private final JobLauncher jobLauncher;
    private final Job reservationExpireJob;

    @Override
    public void run(String... args) throws Exception {
        jobLauncher.run(reservationExpireJob, new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters());
    }
}
