package org.calculator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * 요구사항
 * 1. 문자열에서 , 혹은 : 으로 구분된 숫자의 합을 반환한다.
 * 2. //와 \n 사이의 문자를 커스텀 구분자로 지정할 수 있다.
 * 3. 음수는 RuntimeException으로 예외처리된다.
 */

public class StringCalculator {

    /**
     * 하나의 문자열을 구분자로 각 숫자로 나누어 더한다.
     * @param str
     * @return int 수의 합계
     */
    public int add(String str){

        // 빈 문자열 확인
        if (str == null || str.isBlank()) {
            return 0;
        }

        // 구분자로 문자열 분리
        String[] strArr = getStrArr(str);

        // 수의 합계를 구함
        return sumStrArr(strArr);
    }

    /**
     * 문자열을 구분자로 나누어 문자열 배열로 변환한다.
     * @param str
     * @return String[]
     */
    private String[] getStrArr(String str){
        String[] strArr = {};

        //기본 구분자
        String delimiters = ",|:";

        String numberStr = str;
        Matcher m = Pattern.compile("//(.)\n(.*)").matcher(str);
        if (m.find()){
            delimiters += "|" + m.group(1);
            numberStr = m.group(2);
        }

        strArr = numberStr.split(delimiters);

        if (strArr.length == 0) {
            throw new RuntimeException("잘못된 입력입니다.");
        }

        return strArr;
    }

    /**
     * 문자열 배열을 더하여 합계를 정수로 리턴한다.
     * @param strArr
     * @return int
     */
    private int sumStrArr(String[] strArr){
        int sum = 0;
        for(String numStr : strArr){
            sum += getInt(numStr);
        }
        return sum;
    }

    /**
     * 문자열을 정수로 파싱한다.
     * @param numStr
     * @return int
     */
    private int getInt(String numStr){
        int number = Integer.parseInt(numStr);

        if(number < 0) {
            throw new RuntimeException("음수는 입력할 수 없습니다.");
        }

        return number;
    }


}