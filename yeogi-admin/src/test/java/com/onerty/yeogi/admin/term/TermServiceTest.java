package com.onerty.yeogi.admin.term;

import com.onerty.yeogi.admin.term.dto.RegisterTermRequest;
import com.onerty.yeogi.admin.term.dto.TermResponse;
import com.onerty.yeogi.admin.term.dto.UpdateTermRequest;
import com.onerty.yeogi.common.exception.YeogiException;
import com.onerty.yeogi.common.term.Term;
import com.onerty.yeogi.common.term.TermTitle;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TermServiceTest {

    @Mock
    private TermRepository termRepository;

    @InjectMocks
    private TermService termService;

    private RegisterTermRequest registerTermRequest;
    private UpdateTermRequest updateTermRequest;
    private Term savedTerm;

    @BeforeEach
    void setUp() {
        registerTermRequest = new RegisterTermRequest(TermTitle.PRIVACY_POLICY.name(), true, "개인정보 보호 약관입니다.");
        updateTermRequest = new UpdateTermRequest(TermTitle.PRIVACY_POLICY.name(), "개인정보 보호 약관이 변경되었습니다.");

        savedTerm = Term.builder()
                .termId(1L)
                .title(TermTitle.PRIVACY_POLICY.name())
                .isRequired(true)
                .content("개인정보 보호 약관입니다.")
                .version(1)
                .jpaVersion(0)
                .build();
    }

    @Test
    void registerTerm() {

        // given
        when(termRepository.existsByTitle(registerTermRequest.title())).thenReturn(false);
        when(termRepository.save(any(Term.class))).thenReturn(savedTerm);

        // when
        TermResponse response = termService.registerTerm(registerTermRequest);

        // then
        assertNotNull(response);
        assertEquals(TermTitle.PRIVACY_POLICY.name(), response.title());
        assertEquals("개인정보 보호 약관입니다.", response.content());
        assertEquals(1, response.version());

        verify(termRepository, times(1)).save(any(Term.class));
    }

    @Test
    void updateTermContent_Success() {

        // given
        when(termRepository.findTopByTitleOrderByVersionDesc(updateTermRequest.title()))
                .thenReturn(Optional.of(savedTerm));
        when(termRepository.save(any(Term.class))).thenReturn(Term.builder()
                .termId(2L)
                .title(TermTitle.PRIVACY_POLICY.name())
                .isRequired(true)
                .content(updateTermRequest.content())
                .version(2)
                .build());

        // when
        TermResponse response = termService.updateTermContent(updateTermRequest);

        // then
        assertNotNull(response);
        assertEquals(TermTitle.PRIVACY_POLICY.name(), response.title());
        assertEquals("개인정보 보호 약관이 변경되었습니다.", response.content());
        assertEquals(2, response.version());

        verify(termRepository, times(1)).save(any(Term.class));
    }

    @Test
    void updateTermContent_OptimisticLocking() throws InterruptedException {
        final int numberOfThreads = 2;
        CountDownLatch countDownLatch = new CountDownLatch(numberOfThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        // 스레드 1 (조회)
        executorService.execute(() -> {
            try {
                when(termRepository.findTopByTitleOrderByVersionDesc(updateTermRequest.title()))
                        .thenReturn(Optional.of(savedTerm));

                Term term = termRepository.findTopByTitleOrderByVersionDesc(updateTermRequest.title()).get();
                Thread.sleep(100);

                when(termRepository.save(any(Term.class))).thenThrow(OptimisticLockException.class);

                assertThrows(YeogiException.class, () -> termService.updateTermContent(updateTermRequest));
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                countDownLatch.countDown();
            }
        });

        // 스레드 2 (변경)
        executorService.execute(() -> {
            try {
                when(termRepository.findTopByTitleOrderByVersionDesc(updateTermRequest.title()))
                        .thenReturn(Optional.of(savedTerm));

                Term updatedTerm = Term.builder()
                        .termId(savedTerm.getTermId())
                        .title(savedTerm.getTitle())
                        .isRequired(savedTerm.isRequired())
                        .content(updateTermRequest.content())
                        .version(savedTerm.getVersion() + 1)
                        .jpaVersion(savedTerm.getJpaVersion() + 1)
                        .build();

                when(termRepository.save(any(Term.class))).thenReturn(updatedTerm);

                termService.updateTermContent(updateTermRequest);
            } finally {
                countDownLatch.countDown();
            }
        });

        countDownLatch.await();
        executorService.shutdown();
    }
}
