package com.nextstep.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("[문자열계산기]")
class StringCalculatorTest {

    private StringCalculator cal;

    @BeforeEach
    public void setUp() {
        cal = new StringCalculator();
    }

    @Test
    @DisplayName("[add] 빈 문자열 또는 null값 입력 시 0 반환_성공")
    void add_null_또는_빈_문자_성공() {
        assertEquals(0, cal.add(null));
        assertEquals(0, cal.add(""));
    }

    @Test
    @DisplayName("[add] 숫자 하나 입력 시 해당 숫자 반환_성공")
    void add_숫자_하나_성공() {
        assertEquals(1, cal.add("1"));
        assertEquals(2, cal.add("2"));
        assertEquals(3, cal.add("3"));
    }

    @Test
    @DisplayName("[add] 숫자 두 개 쉼표 구분자로 입력 시 합 반환_성공")
     void add_숫자_두개_쉼표_성공() {
        assertEquals(3, cal.add("1,2"));
        assertEquals(5, cal.add("2,3"));
        assertEquals(7, cal.add("3,4"));
    }

    @Test
    @DisplayName("[add] 숫자 두 개 콜론 구분자로 입력 시 합 반환_성공")
     void add_쉼표_또는_콜론_구분자_성공() {
        assertEquals(3, cal.add("1:2"));
        assertEquals(5, cal.add("2,3"));
        assertEquals(6, cal.add("1,2:3"));
    }

    @Test
    @DisplayName("[add] //n과 \n 문자 사이에 커스텀 구분자를 지정할 수 있다_성공")
    void add_custom_구분자_성공()  {
        assertEquals(3, cal.add("//;\n1;2"));
        assertEquals(5, cal.add("//:\n2:3"));
    }

    @Test
    @DisplayName("[add] //n과 \n 문자 사이에 커스텀 구분자를 지정할 수 있다_실패")
    void add_custom_구분자_실패() {
        assertThrows(RuntimeException.class, () -> cal.add("//;\n1;2:3"));
    }

    @Test
    @DisplayName("[add] 음수 값이 포함된 경우 예외 발생_성공")
    void add_음수_성공(){
        assertThrows(RuntimeException.class, () -> cal.add("-1,2,3"));
    }

}
