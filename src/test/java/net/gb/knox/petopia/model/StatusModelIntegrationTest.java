package net.gb.knox.petopia.model;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class StatusModelIntegrationTest {

    @Autowired
    private EntityManager entityManager;

    private void persist(StatusModel status) {
        entityManager.persist(status);
        entityManager.flush();
    }

    @Test
    public void testPersistStatusModel() {
        var status = new StatusModel();

        persist(status);

        assertEquals(1, status.getId());

        var persistedStatus = entityManager.find(StatusModel.class, status.getId());
        assertNotNull(persistedStatus);
    }
}
