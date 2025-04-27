package com.nextstep.calculator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookStringCalculator {
    public int add(String text) {
        if (isBlank(text)) {
            return 0;
        }

        return sum(toInts(split(text)));
    }

    /**
     * 입력이 null 또는 빈 문자열인지 확인
     */
    private boolean isBlank(String text) {
        return text == null || text.isEmpty();
    }

    /*
    * 1) 커스텀 구분자 추출 및 분리
    * 2) 기본 구분자 분리
    * */
    private String[] split(String text) {
        Matcher matcher = Pattern.compile("//(.*)\n(.*)").matcher(text);
        if (matcher.find()) {
            String delimiter = matcher.group(1);
            String numbers = matcher.group(2);
            return numbers.split(delimiter);
        }
        return text.split(",|:");
    }

    private int[] toInts(String[] values) {
        int[] numbers = new int[values.length];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = toPositive(values[i]);
        }
        return numbers;
    }

    private int toPositive(String number) {
        int num = Integer.parseInt(number.trim());
        if (num < 0) {
            throw new RuntimeException("negatives not allowed: " + number);
        }
        return num;
    }

    private int sum(int[] numbers) {
        int sum = 0;
        for (int number : numbers) {
            sum += number;
        }
        return sum;
    }
}
