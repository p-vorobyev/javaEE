package ru.voroby.entity;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserJpaTest extends BaseJpaTest {

    @Test
    void add() {
        var user = new User("Name", 80);
        assertNull(user.getId());
        em.persist(user);
        em.flush();
        assertNotNull(user.getId());
    }

    @Test
    void get() {
        User user = em.createNamedQuery(User.BY_NAME, User.class)
                .setParameter("name", "Poly")
                .getSingleResult();
        assertNotNull(user);
        assertEquals("Poly", user.getName());
    }

    @Test
    void getAll() {
        List<User> users = em.createNamedQuery(User.ALL, User.class).getResultList();
        assertEquals(3, users.size());
        users.forEach(user -> assertNotNull(user.getId()));
    }

}
