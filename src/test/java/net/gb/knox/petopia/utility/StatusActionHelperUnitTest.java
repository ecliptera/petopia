package net.gb.knox.petopia.utility;

import net.gb.knox.petopia.exception.InvalidStatusActionException;
import net.gb.knox.petopia.model.PetModel;
import net.gb.knox.petopia.model.StatusModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StatusActionHelperUnitTest {

    private final StatusActionHelper statusActionHelper = new StatusActionHelper();

    private StatusModel setUp() {
        var petModel = new PetModel();
        petModel.setStatus(new StatusModel());

        statusActionHelper.setPet(petModel);

        return petModel.getStatus();
    }

    @Test
    public void testFeed() throws InvalidStatusActionException {
        var statusModel = setUp();
        statusModel.setHunger(100);
        statusModel.setCleanliness(100);

        statusActionHelper.feed();

        assertEquals(50, statusModel.getHunger());
        assertEquals(90, statusModel.getCleanliness());
    }

    @Test
    public void testFeedThrows() {
        var statusModel = setUp();
        statusModel.setCleanliness(0);
        assertThrows(InvalidStatusActionException.class, statusActionHelper::feed);
    }

    @Test
    public void testPlay() throws InvalidStatusActionException {
        var statusModel = setUp();
        statusModel.setHunger(50);
        statusModel.setTiredness(50);
        statusModel.setHappiness(50);

        statusActionHelper.play();

        assertEquals(70, statusModel.getHappiness());
        assertEquals(60, statusModel.getTiredness());
        assertEquals(60, statusModel.getHunger());
    }

    @Test
    public void testPlayThrows() {
        var statusModel = setUp();

        statusModel.setCleanliness(0);
        assertThrows(InvalidStatusActionException.class, statusActionHelper::play);

        statusModel.setCleanliness(100);
        statusModel.setTiredness(100);
        assertThrows(InvalidStatusActionException.class, statusActionHelper::play);

        statusModel.setTiredness(0);
        statusModel.setHunger(100);
        assertThrows(InvalidStatusActionException.class, statusActionHelper::play);
    }

    @Test
    public void testGroom() throws InvalidStatusActionException {
        var statusModel = setUp();
        statusModel.setCleanliness(40);

        statusActionHelper.groom();

        assertEquals(100, statusModel.getCleanliness());
    }

    @Test
    public void testGroomThrows() {
        var statusModel = setUp();
        statusModel.setTiredness(100);
        assertThrows(InvalidStatusActionException.class, statusActionHelper::groom);
    }

    @Test
    public void testSleep() {
        var statusModel = setUp();
        statusModel.setTiredness(90);

        statusActionHelper.sleep();

        assertEquals(0, statusModel.getTiredness());
    }
}
