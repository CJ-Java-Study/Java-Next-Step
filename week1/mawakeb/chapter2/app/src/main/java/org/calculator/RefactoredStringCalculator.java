package org.calculator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * 요구사항
 * 1. 문자열에서 , 혹은 : 으로 구분된 숫자의 합을 반환한다.
 * 2. //와 \n 사이의 문자를 커스텀 구분자로 지정할 수 있다.
 * 3. 음수는 RuntimeException으로 예외처리된다.
 */

public class RefactoredStringCalculator extends StringCalculator {

    /**
     * 하나의 문자열을 구분자로 각 숫자로 나누어 더한다.
     * @param str
     * @return int 수의 합계
     */
    public int add(String str){

        // 빈 문자열 확인
        if (isNullOrBlank(str)) {
            return 0;
        }

        // 구분자로 문자열 분리
        String[] strArr = getStrArr(str);

        // 수의 합계를 구함
        return sumStrArr(strArr);
    }

    /**
     * 문자열이 NULL이거나 빈 문자열인지 확인한다.
     * @param str
     * @return 빈 문자열일 경우 true
     */
    private boolean isNullOrBlank(String str){
        return str == null || str.isBlank();
    }

    /**
     * 문자열을 구분자로 나누어 문자열 배열로 변환한다.
     * @param str
     * @return String[]
     */
    private String[] getStrArr(String str){

        String delimiters = getDelimiters(str);
        String numberStr = getNumbersStr(str);

        String[] strArr = numberStr.split(delimiters);

        if (strArr.length == 0) {
            throw new RuntimeException("잘못된 입력입니다.");
        }

        return strArr;
    }

    /**
     * 기본 구분자와 커스텀 구분자를 | 로 구분한 문자열로 추출한다.
     * @param str
     * @return String
     */
    private String getDelimiters(String str){
        //기본 구분자
        String delimiters = ",|:";
        
        //커스텀 구분자 추가
        Matcher m = Pattern.compile("//(.)\n(.*)").matcher(str);
        if (m.find()){
            delimiters += "|" + m.group(1);
        }

        return delimiters;
    }

    /**
     * 입력 문자열에서 숫자 부분만 추출한다.
     * @param str 입력 문자열
     * @return String
     */
    private String getNumbersStr(String str) {
        Matcher m = Pattern.compile("//(.)\n(.*)").matcher(str);
        if (m.find()) {
            return m.group(2);
        }
        return str;
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
        return getPositive(number);
    }


    /**
     * 양수인 수만 리턴하고 음수일 경우 RuntimeException을 던진다.
     * @param number
     * @return int
     */
    private int getPositive(int number){
        if(number < 0) {
            throw new RuntimeException("음수는 입력할 수 없습니다.");
        }
        return number;
    }


}