import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StringCalculatorTest {
    private StringCalculator cal;

    @BeforeEach
    public void setup() {
        cal = new StringCalculator();
    }

    @Test
    public void add_null_or_empty_string(){
        assertEquals(0,cal.add(null));
        assertEquals(0,cal.add(""));
    }

    @Test
    public void add_length_1_string(){
        assertEquals(3,cal.add("3"));
    }

    @Test
    public void add_comma_splitter_contains_string(){
        assertEquals(5,cal.add("3,2"));
    }
    // 테스트 코드 작성하니까 리펙토링하고 기존 기능 유지하는가를 확인하는게 간편하네 머리로 복잡하게 생각안해도되고

    @Test
    public void add_comma_or_colon_splitter_contains_string(){
        assertEquals(6, cal.add("1,2:3"));
    }

    @Test
    public void add_custom_splitter_contains_string(){
        assertEquals(6, cal.add("//;\n1;2;3"));
    }

    @Test
    public void add_minus_contains_string(){
        assertEquals(3, cal.add("-3,6,0"));
    }
}
