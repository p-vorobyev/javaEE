package ru.voroby.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.voroby.entity.User;

import javax.persistence.EntityManager;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserStoreTest {

    @Mock
    private EntityManager em;

    @InjectMocks
    private UserStore userStore;

    @Test
    void addUser() {
        User user = new User( "User", 120);
        user.setId(1);
        when(userStore.addUser(user)).thenReturn(user.getId());
        userStore.addUser(user);
        verify(em, times(1)).merge(user);
    }
}
