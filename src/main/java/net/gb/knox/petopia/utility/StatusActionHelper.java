package net.gb.knox.petopia.utility;

import net.gb.knox.petopia.exception.InvalidStatusActionException;
import net.gb.knox.petopia.model.PetModel;
import net.gb.knox.petopia.model.StatusModel;
import org.springframework.stereotype.Component;

@Component
public class StatusActionHelper {

    private PetModel pet;
    private StatusModel status;

    private int clamp(int value) {
        return Math.clamp(value, 0, 100);
    }

    private void assertState() throws IllegalStateException {
        if (pet == null) {
            throw new IllegalStateException("Pet is null");
        } else if (status == null) {
            throw new IllegalStateException("Status is null");
        }
    }

    private void assertTiredness() throws InvalidStatusActionException {
        if (status.getTiredness() == 100) {
            throw new InvalidStatusActionException(
                    String.format("%s is too tired, they need to sleep first", pet.getName()));
        }
    }

    private void assertHunger() throws InvalidStatusActionException {
        if (status.getHunger() == 100) {
            throw new InvalidStatusActionException(
                    String.format("%s is too hungry, they need to eat first", pet.getName()));
        }
    }

    private void assertCleanliness() throws InvalidStatusActionException {
        if (status.getCleanliness() == 0) {
            throw new InvalidStatusActionException(
                    String.format("%s is too dirty, they need to be groomed first", pet.getName()));
        }
    }

    public void setPet(PetModel pet) {
        this.pet = pet;
        this.status = pet.getStatus();
    }

    public void feed() throws InvalidStatusActionException {
        assertState();
        assertCleanliness();

        var hunger = clamp(status.getHunger() - 50);
        var cleanliness = clamp(status.getCleanliness() - 10);

        status.setHunger(hunger);
        status.setCleanliness(cleanliness);
    }

    public void play() throws InvalidStatusActionException {
        assertState();
        assertCleanliness();
        assertTiredness();
        assertHunger();

        var happiness = clamp(status.getHappiness() + 20);
        var tiredness = clamp(status.getTiredness() + 10);
        var hunger = clamp(status.getHunger() + 10);

        status.setHappiness(happiness);
        status.setTiredness(tiredness);
        status.setHunger(hunger);
    }

    public void groom() throws InvalidStatusActionException {
        assertState();
        assertTiredness();
        status.setCleanliness(100);
    }

    public void sleep() {
        assertState();
        status.setTiredness(0);
    }
}
