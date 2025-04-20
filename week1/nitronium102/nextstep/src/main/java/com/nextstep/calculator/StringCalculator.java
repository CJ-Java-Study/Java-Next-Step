package com.nextstep.calculator;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 문자열 계산기
 * - 쉼표(,) 또는 콜론(:)을 기본 구분자로 사용하여 숫자를 더함
 * - "//구분자\n숫자" 형태로 사용자 정의 구분자 지원
 * - 음수 입력 시 예외 발생
 */
public class StringCalculator {

    private static final Pattern CUSTOM_PATTERN = Pattern.compile("//(.*)\n(.*)");
    private static final String DEFAULT_DELIMITER = ",|:";

    /**
     * 문자열을 받아 합계를 반환한다.
     * @param text 입력 문자열
     * @return 합계
     */
    public int add(String text) {
        if (isNullOrEmpty(text)) {
            return 0;
        }

        String delimiter = getDelimiter(text);
        String numbersStr = getNumbersString(text);
        String[] numbers = numbersStr.split(delimiter);
        return calculateSum(numbers);
    }

    /**
     * 입력이 null 또는 빈 문자열인지 확인
     */
    private boolean isNullOrEmpty(String text) {
        return text == null || text.isEmpty();
    }

    /**
     * 사용자 정의 구분자 추출. 없으면 기본 구분자 반환
     */
    private String getDelimiter(String text) {
        return extractCustomParts(text)
                .map(parts -> Pattern.quote(parts[0]))
                .orElse(DEFAULT_DELIMITER);
    }

    /**
     * 실제 계산할 숫자 부분 추출. 사용자 정의 구분자가 없으면 전체 문자열 반환
     */
    private String getNumbersString(String text) {
        return extractCustomParts(text)
                .map(parts -> parts[1])
                .orElse(text);
    }

    /**
     * 사용자 정의 구분자와 숫자 부분을 추출
     * @return [0]: 구분자, [1]: 숫자 문자열
     */
    private Optional<String[]> extractCustomParts(String text) {
        Matcher matcher = CUSTOM_PATTERN.matcher(text);
        if (matcher.find()) {
            return Optional.of(new String[]{matcher.group(1), matcher.group(2)});
        }
        return Optional.empty();
    }


    private int calculateSum(String[] numbers) {
        int sum = 0;
        for (String number : numbers) {
            int num = parseNumber(number);
            sum += num;
        }
        return sum;
    }

    private int parseNumber(String number) {
        int num = Integer.parseInt(number.trim());
        checkPositive(num);
        return num;
    }

    private void checkPositive(int number) {
        if (number < 0) {
            throw new RuntimeException("negatives not allowed: " + number);
        }
    }
}

/*
* 원본 코드
* package com.nextstep.calculator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringCalculator {

    public int add(String text) {
        // 빈 문자열 또는 null 값을 입력할 경우 0을 반환
        if (text == null || text.isEmpty()) {
            return 0;
        }

        String delimiter = ",|:"; // 기본 구분자: 쉼표, 콜론
        String numbersStr = text;

        // 커스텀 구분자 처리: "//{구분자}\n{숫자들}" 형식 확인
        if (text.startsWith("//")) {
            Matcher matcher = Pattern.compile("//(.*)\n(.*)").matcher(text);
            if (matcher.find()) {
                // 첫 번째 캡쳐 그룹: 구분자
                // 두 번째 캡쳐 그룹: 개행문자 이후의 모든 문자(실제 계산할 숫자)
                delimiter = Pattern.quote(matcher.group(1));
                numbersStr = matcher.group(2);
            }
        }

        // 구분자로 문자열 분리
        String[] numbers = numbersStr.split(delimiter);

        // 합계 계산 및 음수 확인
        int sum = 0;
        List<Integer> negatives = new ArrayList<>();

        for (String number : numbers) {
            if (!number.trim().isEmpty()) {
                int num = Integer.parseInt(number.trim());
                if (num < 0) {
                    negatives.add(num);
                    continue;
                }
                sum += num;
            }
        }

        // 음수가 있으면 예외 발생
        if (!negatives.isEmpty()) {
            throw new RuntimeException("negatives not allowed: " + negatives);
        }

        return sum;
    }
}
* */