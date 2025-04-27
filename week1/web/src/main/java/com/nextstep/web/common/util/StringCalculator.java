package com.nextstep.web.common.util;

public class StringCalculator {

    int add(String text) {
        int sum = 0;
        if(text == null || text.isEmpty()) return sum;
        if(text.contains("-")) throw new RuntimeException("음수 입력 불가: " + text);

        // 커스텀 구분자 case
        if(text.startsWith("//") && text.charAt(3) == '\n') {
            Character separate = text.charAt(2);
            String[] numbers = text.substring(4).split(String.valueOf(separate));

            for(String i : numbers) {
                sum += Integer.parseInt(i);
            }
            return sum;
        }

        String[] numbers = text.split(",|:");
        for(String i : numbers) {
            sum += Integer.parseInt(i);
        }

        return sum;
    }
}
