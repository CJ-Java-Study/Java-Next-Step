package com.nextstep.web.common.util;

import java.util.regex.Pattern;

public class StringCalculator {

    int add(String text) {
        int sum = 0;
        if(text == null || text.isEmpty()) return sum;
        // 커스텀 구분자인지 확인
        boolean customDelimiter = text.matches("^//.\\n.*");

        String numbersPart = text;       // 숫자부분
        String delimiterPattern = ",|:";  // 구분자

        if (customDelimiter) {
            char sep = text.charAt(2);
            numbersPart = text.substring(4);
            delimiterPattern = String.valueOf(sep);
        }

        // 숫자 부분이 비어 있으면 바로 0 반환
        if (numbersPart.isEmpty()) {
            return sum;
        }

        String[] tokens = numbersPart.split(delimiterPattern);
        for (String token : tokens) {
            int num = Integer.parseInt(token);

            if (num < 0) {
                throw new RuntimeException("음수 입력 불가: " + text);
            }

            sum += num;
        }

        return sum;
    }
}
