package net.gb.knox.petopia.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatusModelUnitTest {

    @Test
    public void testStatusModel() {
        var status = new StatusModel();
        status.setId(1);
        status.setHappiness(50);
        status.setCleanliness(50);
        status.setHunger(50);
        status.setTiredness(50);

        assertEquals(1, status.getId());
        assertEquals(50, status.getHappiness());
        assertEquals(50, status.getCleanliness());
        assertEquals(50, status.getHunger());
        assertEquals(50, status.getTiredness());
    }

    @Test
    public void testDefaultStatusModel() {
        var status = new StatusModel();

        assertEquals(100, status.getHappiness());
        assertEquals(100, status.getCleanliness());
        assertEquals(100, status.getHunger());
        assertEquals(100, status.getTiredness());
    }
}
