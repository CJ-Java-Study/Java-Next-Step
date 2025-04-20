package com.nextstep.calculator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("[계산기]")
class CalculatorTest {
    private Calculator cal;

    @BeforeEach
    void setUp() {
        cal = new Calculator();
        System.out.println("before");
    }

    @AfterEach
    void teardown() {
        System.out.println("teardown");
    }

    @Test
    @DisplayName("덧셈_성공")
    void add_success() {
        assertEquals(9, cal.add(6, 3));
        System.out.println("add");
    }

    @Test
    @DisplayName("뺄셈_성공")
    void subtract_success() {
        assertEquals(3, cal.subtract(6, 3));
        System.out.println("subtract");
    }

    @Test
    @DisplayName("곱셈_성공")
    void multiply_success() {
        assertEquals(18, cal.multiply(6, 3));
        System.out.println("multiply");
    }

    @Test
    @DisplayName("나눗셈_성공")
    void divide_success() {
        assertEquals(2, cal.divide(6, 3));
        System.out.println("divide");
    }
}
