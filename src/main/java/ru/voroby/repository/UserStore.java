package ru.voroby.repository;

import ru.voroby.entity.User;

import javax.enterprise.context.ApplicationScoped;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class UserStore implements Serializable {

    private final Map<Integer, User> users = new ConcurrentHashMap<>();

    private final AtomicInteger atomic = new AtomicInteger();

    public int addUser(User dto) {
        if (dto.getId() != null) {
            users.put(dto.getId(), new User(dto.getId(), dto.getName(), dto.getAge()));
            return dto.getId();
        } else {
            var user = new User(atomic.incrementAndGet(), dto.getName(), dto.getAge());
            users.put(user.getId(), user);
            return user.getId();
        }
    }

    public Optional<User> getUser(int id) {
        User user = users.get(id);
        return user != null ?
                Optional.of(new User(user.getId(), user.getName(), user.getAge())) :
                Optional.empty();
    }

    public List<User> getAllUsers() {
        return users.values().stream()
                .map(u -> new User(u.getId(), u.getName(), u.getAge()))
                .toList();
    }

}
