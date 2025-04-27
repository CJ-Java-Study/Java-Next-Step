package com.nextstep.web.common.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringCalculatorTest {

    private StringCalculator cal;

    @Test
    void 개행문자_char() {
        String s = "ABC\nDEF";
        char c = s.charAt(3);
        assertEquals(c, '\n');
    }

    @BeforeEach
    public void setup() {
        cal = new StringCalculator();
    }

    @Test
    public void add_null_또는_빈문자() {
        assertEquals(0, cal.add(null));
        assertEquals(0, cal.add(""));
    }

    @Test
    public void add_숫자하나() {
        assertEquals(1, cal.add("1"));
    }

    @Test
    public void add_쉼표구분자() {
        assertEquals(3, cal.add("1,2"));
    }

    @Test
    public void add_쉼표_또는_콜론_구분자() {
        assertEquals(6, cal.add("1,3:2"));
    }

    @Test
    public void add_custom_구분자() {
        assertEquals(6, cal.add("//#\n1#2#3"));
    }

    @Test
    public void add_음수전달_예외처리() {
        assertThrows(RuntimeException.class, () -> cal.add("1,-3,5"));
    }

    @Test
    public void add_custom_구분자가_음수인경우() {
        assertEquals(6, cal.add("//-\n1-2-3"));
    }

    @Test
    public void add_custom_구분자가_마이너스인데_음수전달() {
        assertThrows(RuntimeException.class, () -> cal.add("//-\n-1-2-3"));
    }

    @Test
    public void add_custom_구분자_빈문자인경우() {
        assertEquals(0, cal.add("//#\n"));
    }
}