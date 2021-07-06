import org.junit.Test;

import static org.junit.Assert.*;

public class TriggerTest {

    @Test
    public void whenTrigger() {
        Trigger trigger = new Trigger();
        int expect = 4;
        assertEquals(expect, trigger.checkMulti(2));
    }

}