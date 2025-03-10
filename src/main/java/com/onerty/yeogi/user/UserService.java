package com.onerty.yeogi.user;

import com.onerty.yeogi.exception.ErrorType;
import com.onerty.yeogi.exception.YeogiException;
import com.onerty.yeogi.term.*;
import com.onerty.yeogi.term.dto.TermDto;
import com.onerty.yeogi.term.dto.TermResponse;
import com.onerty.yeogi.user.dto.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AgreementRepository agreementRepository;
    private final TermRepository termRepository;
    private final NicknameRepository nicknameRepository;
    private final StringRedisTemplate redisTemplate;

    public UserSignupResponse registerUser(UserSignupRequest signupDto) {
        validateDuplicateUserAttributes(signupDto.uid(), signupDto.nick());
        validateLatestAndRequiredTerms(signupDto.agreements());

        User user = new User(signupDto);
        userRepository.save(user);
        saveAgreements(user, signupDto.agreements());

        boolean isMarketingAgreed = agreementRepository.findIsAgreedByUserAndTitle(
                user, TermTitle.MARKETING_CONSENT.name()
        ).orElse(false);

        return new UserSignupResponse(user.getUserId().toString(), isMarketingAgreed);
    }

    private void validateDuplicateUserAttributes(String email, String nickname) {

        if (userRepository.existsByUserIdentifier(email)) {
            throw new YeogiException(ErrorType.DUPLICATE_EMAIL);
        }

        if (userRepository.existsByNickname(nickname)) {
            throw new YeogiException(ErrorType.DUPLICATE_NICKNAME);
        }
    }

    private void validateLatestAndRequiredTerms(List<UserTermsAgreementStatus> agreements) {
        if (agreements == null || agreements.isEmpty()) {
            throw new YeogiException(ErrorType.TERMS_NOT_FOUND);
        }

        List<Term> latestTerms = termRepository.findTermsWithLatestTermDetail();

        // 1. 최신 약관이 아닌 ID가 포함되었는지 검증
        Set<Long> latestTermIds = latestTerms.stream()
                .map(Term::getTermId)
                .collect(Collectors.toSet());
        System.out.println(latestTermIds);

        Set<Long> providedTermIds = agreements.stream()
                .map(UserTermsAgreementStatus::termId)
                .collect(Collectors.toSet());
        System.out.println(providedTermIds);
        if (!providedTermIds.containsAll(latestTermIds)) {
            throw new YeogiException(ErrorType.SIGNUP_INVALID_TERM_ID);
        }

        // 2. 필수 약관이 모두 동의되었는지 검증
        Map<Long, Boolean> agreementMap = agreements.stream()
                .collect(Collectors.toMap(UserTermsAgreementStatus::termId, UserTermsAgreementStatus::isAgreed));

        boolean allRequiredTermsAgreed = latestTerms.stream()
                .filter(Term::isRequired)
                .allMatch(term -> Boolean.TRUE.equals(agreementMap.get(term.getTermId())));

        if (!allRequiredTermsAgreed) {
            throw new YeogiException(ErrorType.SIGNUP_REQUIRED_TERMS_NOT_ACCEPTED);
        }
    }

    private void saveAgreements(User user, List<UserTermsAgreementStatus> agreements) {
        List<Term> terms = termRepository.findTermsWithLatestTermDetail();

        Map<Long, Boolean> agreementMap = agreements.stream()
                .collect(Collectors.toMap(UserTermsAgreementStatus::termId, UserTermsAgreementStatus::isAgreed));

        List<Agreement> agreementList = terms.stream()
                .map(term -> new Agreement(
                        new AgreementId(user, term.getTitle(), term.getVersion()),
                        agreementMap.getOrDefault(term.getTermId(), false)
                ))
                .collect(Collectors.toList());

        agreementRepository.saveAll(agreementList);
    }

    public TermResponse getTerms() {
        List<Term> terms = termRepository.findTermsWithLatestTermDetail();
        if (terms.isEmpty()) {
            throw new YeogiException(ErrorType.TERMS_NOT_FOUND, log::error);
        }

        List<TermDto> termDtos = terms.stream()
                .map(term -> new TermDto(
                        term.getTermId(),
                        TermTitle.valueOf(term.getTitle()).getKoreanTitle(),
                        term.getContent(),
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

    public void verifyCertification(VerifyCertificationRequest request) {
        String phoneNumber = request.phoneNumber();
        String certificationNumber = request.certificationNumber();
        String storedCode = redisTemplate.opsForValue().get(phoneNumber);

        if (storedCode == null || !storedCode.trim().equals(certificationNumber.trim())) {
            throw new YeogiException(ErrorType.SMS_AUTH_FAILED);
        }

        redisTemplate.delete(phoneNumber);
    }

}
