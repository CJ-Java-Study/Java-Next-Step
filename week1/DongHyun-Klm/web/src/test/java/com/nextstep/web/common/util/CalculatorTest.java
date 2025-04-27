package com.nextstep.web.common.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Calculator 기본 연산 테스트")
class CalculatorTest {
    private Calculator cal;

    @BeforeEach
    @DisplayName("테스트 시작 전 Calculator 인스턴스 생성")
    void setup() {
        cal = new Calculator();
    }

    @Test
    @DisplayName("add(): 6과 3을 더하면 9를 반환한다")
    void add() {
        assertEquals(9, cal.add(6, 3));
    }

    @Test
    @DisplayName("subtract(): 6에서 3을 빼면 3을 반환한다")
    void subtract() {
        assertEquals(3, cal.subtract(6, 3));
    }

    @Test
    @DisplayName("multiply(): 6과 3을 곱하면 18을 반환한다")
    void multiply() {
        assertEquals(18, cal.multiply(6, 3));
    }

    @Test
    @DisplayName("divide(): 6을 3으로 나누면 2를 반환한다")
    void divide() {
        assertEquals(2, cal.divide(6, 3));
    }

    @AfterEach
    @DisplayName("각각 테스트 종료 후 실행")
    void teardown() {
        System.out.println("teardown");
    }
}
