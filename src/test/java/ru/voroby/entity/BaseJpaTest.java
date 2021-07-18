package ru.voroby.entity;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Slf4j
public abstract class BaseJpaTest {

    private static EntityManagerFactory emf;

    protected EntityManager em;

    @BeforeAll
    static void initEntityManager() {
        emf = Persistence.createEntityManagerFactory("EE-test");
    }

    @AfterAll
    static void closeEntityManagerFactory() {
        emf.close();
    }

    @BeforeEach
    void beginTransaction() {
        em = emf.createEntityManager();
        em.getTransaction().begin();
        log.info("Begin transaction");
    }

    @AfterEach
    void rollbackTransaction() {
        var transaction = em.getTransaction();
        if (transaction.isActive())
            transaction.rollback();

        log.info("Rollback transaction");
        if (em.isOpen())
            em.close();
    }
}
