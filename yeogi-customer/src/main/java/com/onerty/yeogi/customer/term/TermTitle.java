package com.onerty.yeogi.customer.term;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum TermTitle {
    TERMS_OF_SERVICE("이용약관"),
    PRIVACY_POLICY("개인정보 수집 이용 동의"),
    MARKETING_CONSENT("마케팅 수집 동의"),
    LOCATION_SERVICE_CONSENT("위치기반 서비스 이용약관 동의");

    private final String koreanTitle;

    public static TermTitle fromKoreanTitle(String koreanTitle) {
        return Arrays.stream(values())
                .filter(t -> t.koreanTitle.equals(koreanTitle))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown title: " + koreanTitle));
    }

}
