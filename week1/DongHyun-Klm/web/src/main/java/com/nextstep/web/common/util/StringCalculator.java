package com.nextstep.web.common.util;

import java.util.regex.Pattern;

public class StringCalculator {
    private static final String DEFAULT_DELIMITER_PATTERN = ",|:";

    int add(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }

        boolean custom = hasCustomDelimiter(text);
        String delimiterPattern = custom ? getCustomDelimiterPattern(text) : DEFAULT_DELIMITER_PATTERN;
        String numbersPart = custom ? getNumbersPart(text) : text;

        if (numbersPart.isEmpty()) {
            return 0;
        }

        String[] tokens = splitNumbers(numbersPart, delimiterPattern);
        int[] values = parseTokens(tokens);
        validateNoNegatives(values, text);

        return sum(values);
    }

    private boolean hasCustomDelimiter(String text) {
        return text.matches("^//.\\n.*");
    }

    private String getCustomDelimiterPattern(String text) {
        return String.valueOf(text.charAt(2));
    }

    private String getNumbersPart(String text) {
        return text.substring(4);
    }

    private String[] splitNumbers(String numbersPart, String delimiterPattern) {
        return numbersPart.split(delimiterPattern);
    }

    private int[] parseTokens(String[] tokens) {
        int[] nums = new int[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            nums[i] = Integer.parseInt(tokens[i]);
        }
        return nums;
    }

    private void validateNoNegatives(int[] numbers, String originalText) {
        for (int num : numbers) {
            if (num < 0) {
                throw new RuntimeException("음수 입력 불가: " + originalText);
            }
        }
    }

    private int sum(int[] numbers) {
        int total = 0;
        for (int num : numbers) {
            total += num;
        }
        return total;
    }
}
