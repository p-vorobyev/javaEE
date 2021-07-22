package ru.voroby.repository;

import lombok.extern.slf4j.Slf4j;
import ru.voroby.entity.User;
import ru.voroby.interceptors.UserAudit;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import static ru.voroby.interceptors.UserAction.GET;
import static ru.voroby.interceptors.UserAction.GET_ALL;
import static ru.voroby.interceptors.UserAction.SAVED;


@Slf4j
@TransactionManagement
@Stateless
public class UserStore implements Serializable {

    @PersistenceContext(unitName = "EE")
    private EntityManager em;

    @UserAudit(action = SAVED)
    @TransactionAttribute
    public int addUser(User user) {
        if (user.getId() != null) {
            em.merge(user);
            log.info("User updated: [id: {}]", user.getId());
        } else {
            em.persist(user);
            log.info("User saved: [id: {}]", user.getId());
        }

        return user.getId();
    }

    @UserAudit(action = GET)
    @TransactionAttribute
    public Optional<User> getUser(int id) {
        User user = em.find(User.class, id);
        return user != null ? Optional.of(user) : Optional.empty();
    }

    @UserAudit(action = GET_ALL)
    @TransactionAttribute
    public List<User> getAllUsers() {
        return em.createNamedQuery(User.ALL, User.class).getResultList();
    }

}
