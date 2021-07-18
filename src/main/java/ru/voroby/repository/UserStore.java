package ru.voroby.repository;

import lombok.extern.slf4j.Slf4j;
import ru.voroby.entity.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;


@Slf4j
@Transactional
@Stateless
public class UserStore implements Serializable {

    @PersistenceContext(unitName = "EE")
    private EntityManager em;

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

    public Optional<User> getUser(int id) {
        User user = em.find(User.class, id);
        return user != null ? Optional.of(user) : Optional.empty();
    }

    public List<User> getAllUsers() {
        return em.createNamedQuery(User.ALL, User.class).getResultList();
    }

}
