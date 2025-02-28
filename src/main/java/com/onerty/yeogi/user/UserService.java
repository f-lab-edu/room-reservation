package com.onerty.yeogi.user;

import com.onerty.yeogi.exception.ErrorType;
import com.onerty.yeogi.exception.YeogiException;
import com.onerty.yeogi.term.Term;
import com.onerty.yeogi.term.TermRepository;
import com.onerty.yeogi.term.dto.TermDto;
import com.onerty.yeogi.term.dto.TermResponse;
import com.onerty.yeogi.user.dto.NicknameResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final TermRepository termRepository;
    private final NicknameRepository nicknameRepository;
    private final StringRedisTemplate redisTemplate;

    public TermResponse getTerms() {
        List<Term> terms = termRepository.findTermsWithLatestTermDetail();
        if (terms.isEmpty()) {
            throw new YeogiException(ErrorType.TERMS_NOT_FOUND);
        }

        List<TermDto> termDtos = terms.stream()
                .map(term -> new TermDto(
                        term.getTermId(),
                        term.getTitle(),
                        term.getTermDetails().isEmpty() ? "" : term.getTermDetails().get(0).getContent(),
                        term.isRequired()
                ))
                .collect(Collectors.toList());

        return new TermResponse(termDtos);
    }

    public NicknameResponse generateRandomNicknames() {
        List<String> adjectives1 = nicknameRepository.findByType(NicknameType.ADJECTIVE1)
                .stream()
                .map(Nickname::getValue)
                .toList();

        List<String> adjectives2 = nicknameRepository.findByType(NicknameType.ADJECTIVE2)
                .stream()
                .map(Nickname::getValue)
                .toList();

        List<String> nouns = nicknameRepository.findByType(NicknameType.NOUN)
                .stream()
                .map(Nickname::getValue)
                .toList();

        List<String> nicknames = adjectives1.stream()
                .flatMap(adj1 -> adjectives2.stream()
                        .flatMap(adj2 -> nouns.stream()
                                .map(noun -> adj1 + adj2 + noun)))
                .collect(Collectors.toList());

        Collections.shuffle(nicknames);

        return new NicknameResponse(nicknames.stream()
                .limit(20)
                .collect(Collectors.toList()));
    }

    public String sendCertification(String phoneNumber) {
        String code = String.valueOf((int) (Math.random() * 9000) + 1000);
        redisTemplate.opsForValue().set(phoneNumber, code, 5, TimeUnit.MINUTES);
        return phoneNumber + " 로 인증번호가 발송되었습니다: " + code;
    }

    public void verifyCertification(Map<String, String> payload) {
        String phoneNumber = payload.get("phoneNumber");
        String certificationNumber = payload.get("certificationNumber");
        String storedCode = redisTemplate.opsForValue().get(phoneNumber);

        if (storedCode == null || !storedCode.trim().equals(certificationNumber.trim())) {
            throw new YeogiException(ErrorType.SMS_AUTH_FAILED);
        }

        redisTemplate.delete(phoneNumber);
    }

}
