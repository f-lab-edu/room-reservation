package com.onerty.yeogi.common.term;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum TermTitle {
    TERMS_OF_SERVICE("이용약관", true),
    PRIVACY_POLICY("개인정보 수집 이용 동의", true),
    MARKETING_CONSENT("마케팅 수집 동의", true),
    LOCATION_SERVICE_CONSENT("위치기반 서비스 이용약관 동의", true),

    NEW_TERM("테스트 약관", false),
    UNKNOWN("알 수 없음", false);

    private final String koreanTitle;
    private final boolean isOfficial;

    public static TermTitle fromKoreanTitle(String koreanTitle) {
        return Arrays.stream(values())
                .filter(t -> t.koreanTitle.equals(koreanTitle))
                .findFirst()
                .orElse(UNKNOWN);
    }

    public static boolean notExists(String title) {
        return Arrays.stream(values())
                .noneMatch(t -> t.name().equals(title));
    }

}
