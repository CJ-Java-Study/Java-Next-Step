package org.calculator;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class StringCalculatorTest {

    private StringCalculator cal;

    @Before
    public void setup() {
        cal = new StringCalculator();

        // 아래 줄 주석처리로 리팩토링 전후 버전 테스트 가능
        cal = new RefactoredStringCalculator();
    }

    @Test
    public void addBlank() {
        assertEquals(0, cal.add(null));
        assertEquals(0, cal.add(""));
        assertEquals(0, cal.add(" "));
    }

    @Test
    public void addSingle() {
        assertEquals(0, cal.add("0"));
        assertEquals(1, cal.add("1"));
        assertEquals(10, cal.add("10"));
    }

    @Test
    public void addWithDefaultDelimiters() {
        assertEquals(3, cal.add("1,2"));
        assertEquals(15, cal.add("4:5:6"));
        assertEquals(24, cal.add("7,8:9"));
        assertEquals(6, cal.add("1,1,1:1,1:1"));
    }

    @Test
    public void addWithCustomDelimiters() {
        assertEquals(6, cal.add("//;\n1;2;3"));
        assertEquals(6, cal.add("//;\n1,2;3")); // 기본 구분자와 섞여도 처리 
        assertEquals(6, cal.add("// \n1,2 3")); // whitespace도 구분자로 처리
        assertEquals(6, cal.add("//4\n1,243")); // 수도 구분자로 처리
    }

    @Test
    public void addNegative() {
        assertThrows(RuntimeException.class, () -> cal.add("-2"));
        assertThrows(RuntimeException.class, () -> cal.add("7,-8:9"));

    }

    @Test
    public void addInvalidString() {
        assertThrows(RuntimeException.class, () -> cal.add("1;2"));
        assertThrows(RuntimeException.class, () -> cal.add("//;\n1;2;aa"));
    }

}